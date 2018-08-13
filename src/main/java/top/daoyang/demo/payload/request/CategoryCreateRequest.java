package top.daoyang.demo.payload.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class CategoryCreateRequest {

    @NotBlank
    private String parentId;

    @NotBlank
    private String name;
}
