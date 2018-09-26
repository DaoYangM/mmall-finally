package top.daoyang.demo.tree;

import lombok.Getter;
import lombok.Setter;
import top.daoyang.demo.entity.Comment;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CommentTree {

    private Comment data;

    private List<CommentTree> child = new ArrayList<>();

    public CommentTree(Comment data, List<CommentTree> child) {
        this.data = data;
        this.child = child;
    }

    public CommentTree(Comment data) {
        this.data = data;
    }

    public CommentTree() {
    }

    @Override
    public String toString() {
        return data.getComment();
    }
}
