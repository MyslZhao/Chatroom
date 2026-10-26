package com.example.demo.service;

import com.example.demo.config.JWTConfig;
import com.example.demo.dto.LoginDto;
import com.example.demo.entity.UserEntity;
import com.example.demo.mapper.UserMapper;
import com.example.demo.utils.JWTUntils;
import com.example.demo.utils.ResultUntils;
import com.example.demo.vo.LoginVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.exception.BusinessException;

@Service
public class LoginImpl implements LoginService {
    @Autowired
    UserMapper userMapper;

    @Autowired
    JWTConfig jwtConfig;
    @Autowired
    JWTUntils jwtUntils;

    @Override
    public LoginVo login(LoginDto loginDto) {
        LoginVo loginVo = new LoginVo();


        UserEntity userEntity = userMapper.selectOne(new LambdaQueryWrapper<UserEntity>()
                .eq(UserEntity::getUserName, loginDto.getUserName())
                .eq(UserEntity::getPassword, loginDto.getPassword()));
        if(userEntity == null){
           throw new BusinessException(ResultUntils.FAIL_CODE, "用户名或密码错误");
        }
        loginVo.setUserId(userEntity.getId());
        loginVo.setMessage("登录成功");
        loginVo.setToken(jwtUntils.generateToken(userEntity.getId(), userEntity.getUserName()));
        loginVo.setUserName(userEntity.getUserName());



        return loginVo;
    }

}