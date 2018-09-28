package top.daoyang.demo.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import top.daoyang.demo.entity.Comment;
import top.daoyang.demo.entity.CommentOrder;
import top.daoyang.demo.entity.WxUser;
import top.daoyang.demo.enums.ExceptionEnum;
import top.daoyang.demo.exception.CommentException;
import top.daoyang.demo.exception.UserNotFoundException;
import top.daoyang.demo.mapper.CommentMapper;
import top.daoyang.demo.mapper.CommentOrderMapper;
import top.daoyang.demo.mapper.WxUserMapper;
import top.daoyang.demo.payload.reponse.CommentResponse;
import top.daoyang.demo.payload.request.CommentCreateRequest;
import top.daoyang.demo.payload.request.CommentOrderCreateRequest;
import top.daoyang.demo.security.WXUserDetails;
import top.daoyang.demo.service.CommentService;
import top.daoyang.demo.tree.CommentTree;
import top.daoyang.demo.util.FtpUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CommentServiceImpl implements CommentService {
    @Value("${app.ftp.username}")
    private String ftpUserName;

    @Value("${app.ftp.host}")
    private String ftpHost;

    @Value("${app.ftp.password}")
    private String ftpPassword;

    @Value("${app.ftp.uploadCIF}")
    private String uploadCIF;

    @Value("${app.ftp.responseCIFold}")
    private String responseCIFold;

    @Value("${app.imageHost}")
    private String imageHost;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private CommentOrderMapper commentOrderMapper;

    @Autowired
    private WxUserMapper wxUserMapper;

    @Override
    public PageInfo getCommentTreeByProductId(Integer productId, int page, int size) {
//        return getCommentTree(productId, 0);
        PageHelper.startPage(page, size);
        List<Comment> commentList = commentMapper.getOutComment(productId);

        List<CommentResponse> commentResponseList = commentList.stream().map(comment -> assembleCommentResponse(comment)).collect(Collectors.toList());
        PageInfo pageInfo = new PageInfo<>(commentList);
        pageInfo.setList(commentResponseList);

        return pageInfo;
    }

    @Override
    public boolean createCommentImg(HttpServletRequest request, String userId) {
        MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;

        String commentId = multipartHttpServletRequest.getParameter("commentId");
        String localUploadPath = request.getServletContext().getRealPath("upload");
        File localFold = new File(localUploadPath);
        if (!localFold.exists()) {
            localFold.setWritable(true);
            localFold.mkdirs();
            log.info("Creating fold {}", localUploadPath);
        }
        MultipartFile multipartFile = multipartHttpServletRequest.getFile("file");
        UUID uuid = UUID.randomUUID();
        String imgName = uuid + ".jpg";
        File file = new File(localFold.getAbsolutePath(), imgName);

        try {
            multipartFile.transferTo(file);

            Comment comment = Optional.ofNullable(commentMapper.getCommentByUserIdAndCommentId(userId, Integer.parseInt(commentId)))
                    .orElseThrow(() -> new CommentException(ExceptionEnum.COMMENT_DOSE_NOT_EXIST));
            try {
                FtpUtils.upload(ftpHost,imgName, new FileInputStream(file), ftpUserName, ftpPassword, uploadCIF);
                if (file.setWritable(true)) {
                    log.info("Setting imgFile writable true");
                }
                file.setExecutable(true);
                if (file.delete()) {
                    log.info("Local image deleted");
                } else {
                    log.info("Local image deleted failure");
                }
            } catch (SftpException | FileNotFoundException | JSchException e) {
                e.printStackTrace();
            }

            log.info("All processing of alipay has been finished");
            String serverImgName = imageHost + "/" +  responseCIFold +"/" + imgName;
            if (StringUtils.hasText(comment.getImage())) {
                comment.setImage(comment.getImage() + ", " + serverImgName);
            }
            else {
                comment.setImage(serverImgName);
            }
            return commentMapper.updateByPrimaryKeyWithBLOBs(comment) == 1;
        } catch (IOException e) {
            log.error("Image write to local failure");
            return false;
        }
    }

    @Override
    public Comment createComment(String userId, CommentCreateRequest commentCreateRequest) {

        Comment comment = new Comment();
        BeanUtils.copyProperties(commentCreateRequest, comment);
        comment.setUserId(userId);

        if (commentMapper.insertSelective(comment) != 1) {
            throw new CommentException(ExceptionEnum.COMMENT_CREATE_ERROR);
        }

        return comment;
    }

    @Override
    public CommentOrder createCommentOrder(WXUserDetails wxUserDetails, CommentOrderCreateRequest commentOrderCreateRequest) {
        CommentOrder commentOrder = new CommentOrder();
        BeanUtils.copyProperties(commentOrderCreateRequest, commentOrder);
        commentOrder.setUserId(wxUserDetails.getId());

        if (commentOrderMapper.insertSelective(commentOrder) == 1) {
            return commentOrder;
        } else {
            throw new CommentException(ExceptionEnum.COMMENT_ORDER_CREATE_ERROR);
        }
    }

    @Override
    public CommentResponse upComment(String userId, Integer commentId) {
        Comment comment = Optional.ofNullable(commentMapper.getCommentByUserIdAndCommentId(userId, commentId))
                .orElseThrow(() -> new CommentException(ExceptionEnum.COMMENT_DOSE_NOT_EXIST));

        if (comment.getUp() == null) {
            comment.setUp(0);
        }
        comment.setUp(comment.getUp() + 1);
        if (commentMapper.updateByPrimaryKeyWithBLOBs(comment) == 1) {
            return assembleCommentResponse(comment);
        } else {
            throw new CommentException(ExceptionEnum.COMMENT_ORDER_CREATE_ERROR);
        }
    }

    public CommentTree getCommentTree(Integer productId, Integer pid) {
        CommentTree commentTree0 = new CommentTree();
        if (pid != 0) {
            Comment comment1 = commentMapper.selectByPrimaryKey(pid);
            if (comment1 != null) {
                commentTree0 = new CommentTree(comment1);
            }
        }
        List<Comment> commentList = commentMapper.getCommentByPIDAndProductId(productId, pid);
        for (Comment comment : commentList) {
            commentTree0.getChild().add(getCommentTree(productId, comment.getId()));

        }
        return commentTree0;
    }

    private CommentResponse assembleCommentResponse(Comment comment) {
        CommentResponse commentResponse = new CommentResponse();
        BeanUtils.copyProperties(comment, commentResponse);
        WxUser wxUser = Optional.ofNullable(wxUserMapper.getByOpenId(comment.getUserId()))
                .orElseThrow(() -> new UserNotFoundException(ExceptionEnum.USER_DOES_NOT_EXIST));
        commentResponse.setNickName(wxUser.getNickName());
        commentResponse.setAvatar(wxUser.getAvatar());

        String commentImages = comment.getImage();
        if (StringUtils.hasText(commentImages)) {
            List<String> comImages = Arrays.asList(commentImages.split(","));
            commentResponse.setCommentImages(comImages);
        }

        return commentResponse;
    }
}
