package top.daoyang.demo.payload.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Setter
@Getter
public class SubCommentCreateRequest {

    private Integer subCommentId;

    @NotBlank
    private String content;

    @NotNull
    private Integer commentId;
}
