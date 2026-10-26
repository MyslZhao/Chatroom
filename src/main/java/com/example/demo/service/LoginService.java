package com.example.demo.service;

import com.example.demo.dto.LoginDto;
import com.example.demo.vo.LoginVo;
import org.springframework.stereotype.Service;
@Service
public interface LoginService {
    LoginVo login(LoginDto loginDto);
}