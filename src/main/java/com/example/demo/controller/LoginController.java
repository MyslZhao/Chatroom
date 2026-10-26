package com.example.demo.controller;

import com.example.demo.dto.LoginDto;
import com.example.demo.service.LoginService;
import com.example.demo.utils.Result;
import com.example.demo.utils.ResultUntils;
import com.example.demo.vo.LoginVo;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class LoginController  {
    @Autowired
    private LoginService loginService;
    @PostMapping
    public Result<LoginVo> login(@Valid @RequestBody LoginDto loginDto) {
        LoginVo loginVo = loginService.login(loginDto);
        return ResultUntils.success(loginVo);
    }


}