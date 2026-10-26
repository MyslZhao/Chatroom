package com.example.demo.vo;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
public class LoginVo {
    private String token;
    @JsonProperty("username")
    private String userName;
    private String password;
    private Long userId;
    private String message;
    

}