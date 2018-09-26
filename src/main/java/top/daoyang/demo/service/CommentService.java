package top.daoyang.demo.service;

import com.github.pagehelper.PageInfo;
import top.daoyang.demo.tree.CommentTree;

public interface CommentService {
    CommentTree getCommentTreeByProductId(Integer productId);
}
