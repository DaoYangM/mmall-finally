package top.daoyang.demo.payload.request;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class JwtAuthenticationRequest {

    @NotBlank
    private String username;

    @NotBlank
    private String password;
}
