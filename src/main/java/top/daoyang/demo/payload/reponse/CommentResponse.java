package top.daoyang.demo.payload.reponse;

import lombok.Getter;
import lombok.Setter;
import top.daoyang.demo.entity.Comment;

import java.util.List;

@Setter
@Getter
public class CommentResponse extends Comment {
    private String nickName;

    private String avatar;

    private Integer commentCount;

    private List<String> commentImages;
}
