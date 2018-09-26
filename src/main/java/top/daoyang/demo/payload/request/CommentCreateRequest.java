package top.daoyang.demo.payload.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentCreateRequest {
    private Integer commentId;

    private String comment;

    private Integer productId;
}
