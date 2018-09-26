package top.daoyang.demo.service.impl;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import top.daoyang.demo.entity.Comment;
import top.daoyang.demo.enums.ExceptionEnum;
import top.daoyang.demo.exception.CommentException;
import top.daoyang.demo.mapper.CommentMapper;
import top.daoyang.demo.payload.ServerResponse;
import top.daoyang.demo.payload.request.CommentCreateRequest;
import top.daoyang.demo.service.CommentService;
import top.daoyang.demo.tree.CommentTree;
import top.daoyang.demo.util.FtpUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

    @Override
    public CommentTree getCommentTreeByProductId(Integer productId) {
        return getCommentTree(productId, 0);
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
        comment.setUserId(userId);
        comment.setComment(commentCreateRequest.getComment());
        comment.setProductId(commentCreateRequest.getProductId());

        if (commentMapper.insertSelective(comment) != 1) {
            throw new CommentException(ExceptionEnum.COMMENT_CREATE_ERROR);
        }

        return comment;
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
}
