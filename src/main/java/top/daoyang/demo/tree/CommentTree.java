package top.daoyang.demo.tree;

import lombok.Getter;
import lombok.Setter;
import top.daoyang.demo.payload.reponse.CommentResponse;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CommentTree {

    private CommentResponse data;

    private List<CommentTree> child = new ArrayList<>();

    public CommentTree(CommentResponse data, List<CommentTree> child) {
        this.data = data;
        this.child = child;
    }

    public CommentTree(CommentResponse data) {
        this.data = data;
    }

    public CommentTree() {
    }

    @Override
    public String toString() {
        return data.getComment();
    }
}
