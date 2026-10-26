package com.example.demo.service;

import com.example.demo.dto.PrivateMessageDto;
import com.example.demo.vo.PrivateMessageVO;

import java.util.List;
import java.util.Map;

public interface PrivateMessageService {

    PrivateMessageVO sendMessage(Long sendId, PrivateMessageDto dto);

    List<PrivateMessageVO> history(Long sendId, Long friendId);


    Map<Long, Long> unreadCount(Long userId);


    void markRead(Long userId, Long friendId);
}
