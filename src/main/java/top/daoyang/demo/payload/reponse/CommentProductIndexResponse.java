package top.daoyang.demo.payload.reponse;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CommentProductIndexResponse {

    private Integer commentCount;

    private CommentResponse comment;
}
