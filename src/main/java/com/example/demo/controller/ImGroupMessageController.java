package com.example.demo.controller;


import com.example.demo.dto.GroupMessageDTO;
import com.example.demo.service.IImGroupMessageService;
import com.example.demo.utils.Result;
import com.example.demo.utils.ResultUntils;
import com.example.demo.vo.GroupMessageVO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 群消息 前端控制器
 * </p>
 *
 * @author author
 * @since 2026-10-24
 */
@Validated
@RestController
@RequestMapping("/message/group")
public class ImGroupMessageController {

    @Autowired
    private IImGroupMessageService groupMessageService;

    /** 发送群消息 */
    @PostMapping("/send")
    public Result<GroupMessageVO> send(@Valid @RequestBody GroupMessageDTO dto,
                                       HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return ResultUntils.success(groupMessageService.send(userId, dto));
    }


    @GetMapping("/history")
    public Result<List<GroupMessageVO>> history(@RequestParam Long groupId,
                                                HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return ResultUntils.success(groupMessageService.history(userId, groupId));
    }


}