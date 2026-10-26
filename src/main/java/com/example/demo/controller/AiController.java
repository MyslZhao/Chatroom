package com.example.demo.controller;

import com.example.demo.entity.PrivateMessageEntity;
import com.example.demo.mapper.PrivateMessageMapper;
import com.example.demo.service.AIService;
import com.example.demo.utils.AiuserContext;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/ai")
public class AiController {
    private static final Long AI_FRIEND_ID = 9999L;

    @Autowired
    private AIService aiService;

    @Autowired
    private PrivateMessageMapper privateMessageMapper;

    @CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*", allowCredentials = "true")
    @GetMapping(value = "/chat/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter stream(@RequestParam String message, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");

        // 不设置超时时间
        SseEmitter emitter = new SseEmitter();
        StringBuilder fullReply = new StringBuilder();

        aiService.replyStream(message, userId)

                .doOnNext(chunk -> {
                    fullReply.append(chunk);
                    try {
                        // 每个片段立即 flush 出去，保证前端能逐字收到
                        emitter.send(chunk);
                    } catch (Exception e) {
                        emitter.completeWithError(e);
                    }
                })
                .doOnComplete(() -> {
                    // 流结束，存 AI 回复
                    saveAiReply(userId, fullReply.toString());
                    emitter.complete();
                })
                .doOnError(err -> {
                    // 出错时也存一条兜底回复，避免历史缺一条
                    saveAiReply(userId, "抱歉，AI 服务暂时不可用，请稍后再试。");
                    emitter.completeWithError(err);
                })
                .subscribe();

        return emitter;
    }

    private void saveAiReply(Long userId, String content) {
        PrivateMessageEntity aiMsg = new PrivateMessageEntity();
        aiMsg.setSendId(AI_FRIEND_ID);
        aiMsg.setRecvId(userId);
        aiMsg.setContent(content);
        aiMsg.setType(0);
        aiMsg.setStatus(0);
        aiMsg.setSendTime(LocalDateTime.now());
        privateMessageMapper.insert(aiMsg);
    }
}