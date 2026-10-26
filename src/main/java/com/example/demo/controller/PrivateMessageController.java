package com.example.demo.controller;

import com.example.demo.dto.PrivateMessageDto;
import com.example.demo.service.PrivateMessageService;
import com.example.demo.utils.Result;
import com.example.demo.utils.ResultUntils;
import com.example.demo.vo.PrivateMessageVO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping("/message/private")
@RestController
public class PrivateMessageController {

    @Autowired
    private PrivateMessageService privateMessageService;

    @PostMapping("/send")
    public Result<PrivateMessageVO> send(@Valid @RequestBody PrivateMessageDto dto,
                                         HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return ResultUntils.success(privateMessageService.sendMessage(userId, dto));
    }

    @GetMapping("/history")
    public Result<List<PrivateMessageVO>> history(@RequestParam Long friendId,
                                                  HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return ResultUntils.success(privateMessageService.history(userId, friendId));
    }

    @GetMapping("/unread")
    public Result<Map<Long, Long>> unread(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return ResultUntils.success(privateMessageService.unreadCount(userId));
    }

    @PostMapping("/read")
    public Result<Void> read(@RequestParam Long friendId, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        privateMessageService.markRead(userId, friendId);
        return ResultUntils.success();
    }

}
