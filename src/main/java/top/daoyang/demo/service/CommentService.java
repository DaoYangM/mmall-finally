package top.daoyang.demo.service;

import top.daoyang.demo.entity.Comment;
import top.daoyang.demo.entity.CommentOrder;
import top.daoyang.demo.payload.request.CommentCreateRequest;
import top.daoyang.demo.payload.request.CommentOrderCreateRequest;
import top.daoyang.demo.security.WXUserDetails;
import top.daoyang.demo.tree.CommentTree;

import javax.servlet.http.HttpServletRequest;

public interface CommentService {
    CommentTree getCommentTreeByProductId(Integer productId);

    boolean createCommentImg(HttpServletRequest request, String userId);

    Comment createComment(String userId, CommentCreateRequest commentCreateRequest);

    CommentOrder createCommentOrder(WXUserDetails wxUserDetails, CommentOrderCreateRequest commentOrderCreateRequest);
}
