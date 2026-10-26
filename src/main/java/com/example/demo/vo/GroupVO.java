package com.example.demo.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GroupVO {
    private Long id;
    private String name;
    private Long ownerId;
    private String headImage;
    private LocalDateTime createdTime;
    private Integer unreadCount;
}