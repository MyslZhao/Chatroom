package com.example.demo.config;

import com.example.demo.utils.JWTUntils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import com.example.demo.exception.BusinessException;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Autowired
    private JWTUntils jwtUntils;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String authHeader = request.getHeader("Authorization");

        if (!StringUtils.hasText(authHeader) || !authHeader.startsWith("Bearer ")) {
            throw new BusinessException(401, "未登录或登录已过期");
        }

        String token = authHeader.substring(7);

        try {
            Long userId = jwtUntils.getUserId(token);
            request.setAttribute("userId", userId);
        } catch (Exception e) {
            throw new BusinessException(401, "登录已过期，请重新登录");
        }

        return true;
    }

}