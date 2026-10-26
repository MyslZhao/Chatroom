package com.example.demo.controller;


import com.example.demo.dto.GroupCreateDto;
import com.example.demo.service.IImGroupService;
import com.example.demo.utils.Result;
import com.example.demo.utils.ResultUntils;
import com.example.demo.vo.GroupMemberVO;
import com.example.demo.vo.GroupVO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 群 前端控制器
 * </p>
 *
 * @author author
 * @since 2026-10-24
 */
@RestController
@RequestMapping("/group")
public class ImGroupController {
    @Autowired
    private IImGroupService imGroupService;
    
    @RequestMapping("/create")
    public Result<GroupVO> create(@RequestBody GroupCreateDto dto, HttpServletRequest request) {
        // 1. 从请求头中获取token
        Long userId = (Long) request.getAttribute("userId");
        
        // 2. 转换为Long类型
        // 3. 调用服务创建群
        GroupVO groupVO = imGroupService.create(userId, dto);
        return ResultUntils.success(groupVO);
    }


    @RequestMapping("/list")
    public Result<List<GroupVO>> list(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        List<GroupVO> groupList = imGroupService.myGroups(userId);
        return ResultUntils.success(groupList);
    }


    @RequestMapping("/member")
    public Result<List<GroupMemberVO>> member(@RequestParam Long groupId, HttpServletRequest request) {

        List<GroupMemberVO> groupMemberList = imGroupService.members(groupId);
        return ResultUntils.success(groupMemberList);
    }

    @RequestMapping("/read")
    public Result<Void> read(@RequestParam Long groupId, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        imGroupService.markRead(userId, groupId);

        return ResultUntils.success();
    }
}