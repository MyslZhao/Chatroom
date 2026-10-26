package com.example.demo.controller;

import com.example.demo.entity.FriendEntity;
import com.example.demo.service.FriendService;
import com.example.demo.utils.Result;
import com.example.demo.utils.ResultUntils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/friends")
public class FriendController  {
    @Autowired
    private FriendService friendService;
    @Autowired
    private ResultUntils resultUtils;

    @GetMapping("/list")
    public Result<List<FriendEntity>> list(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return ResultUntils.success(friendService.list(userId));
    }

    /** 添加好友 */
    @PostMapping("/add/{friendId}")
    public Result<String> add(@PathVariable Long friendId, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        friendService.addFriend(userId, friendId);
        return ResultUntils.success(null);
    }



}