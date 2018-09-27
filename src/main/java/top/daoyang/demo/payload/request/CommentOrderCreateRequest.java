package top.daoyang.demo.payload.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Setter
@Getter
public class CommentOrderCreateRequest {
    @NotNull
    private Long orderNo;

    @NotNull
    private Integer commentId;
}
