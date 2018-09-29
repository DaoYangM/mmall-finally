package top.daoyang.demo.service;

import com.github.pagehelper.PageInfo;
import top.daoyang.demo.entity.Comment;
import top.daoyang.demo.entity.CommentOrder;
import top.daoyang.demo.payload.reponse.CommentResponse;
import top.daoyang.demo.payload.reponse.SubCommentResponse;
import top.daoyang.demo.payload.request.CommentCreateRequest;
import top.daoyang.demo.payload.request.CommentOrderCreateRequest;
import top.daoyang.demo.payload.request.SubCommentCreateRequest;
import top.daoyang.demo.security.WXUserDetails;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface CommentService {
    PageInfo getCommentTreeByProductId(Integer productId, int page, int size);

    boolean createCommentImg(HttpServletRequest request, String userId);

    Comment createComment(String userId, CommentCreateRequest commentCreateRequest);

    CommentOrder createCommentOrder(WXUserDetails wxUserDetails, CommentOrderCreateRequest commentOrderCreateRequest);

    CommentResponse upComment(String userId, Integer commentId);

    List<SubCommentResponse> getSubCommentDetail(Integer commentId);

    CommentResponse getCommentDetail(Integer commentId);

    List<SubCommentResponse> createSubComment(String userId, SubCommentCreateRequest subCommentCreateRequest);
}
