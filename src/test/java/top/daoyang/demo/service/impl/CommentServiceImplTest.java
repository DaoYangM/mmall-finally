package top.daoyang.demo.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import top.daoyang.demo.service.CommentService;
import top.daoyang.demo.tree.CommentTree;

import static org.junit.Assert.*;

@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class CommentServiceImplTest {

    @Autowired
    private CommentServiceImpl commentService;

    @Test
    public void getCommentTreeByProductId() {

    }

    @Test
    public void getCommentTree() {
        CommentTree commentTree = commentService.getCommentTree(28, 0);
        log.info(commentTree.toString());
    }
}