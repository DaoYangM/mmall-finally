package top.daoyang.demo.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.daoyang.demo.entity.Comment;
import top.daoyang.demo.mapper.CommentMapper;
import top.daoyang.demo.service.CommentService;
import top.daoyang.demo.tree.CommentTree;

import java.util.List;

@Service

public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Override
    public CommentTree getCommentTreeByProductId(Integer productId) {
        return getCommentTree(productId, 0);
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
