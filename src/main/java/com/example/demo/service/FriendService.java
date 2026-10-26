package com.example.demo.service;

import com.example.demo.entity.FriendEntity;

import java.util.List;

public interface FriendService {
    List<FriendEntity> list(Long userId);

    /** 添加好友（双向写入） */
    void addFriend(Long userId, Long friendId);
}
