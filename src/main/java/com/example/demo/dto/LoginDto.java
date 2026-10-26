package com.example.demo.dto;

import lombok.Data;
import jakarta.validation.constraints.NotEmpty;
import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
public class LoginDto {

    @NotEmpty(message = "用户名不可为空")
    @JsonProperty("username")
    private String userName;

    @NotEmpty(message = "用户密码不可为空")
    private String password;
}