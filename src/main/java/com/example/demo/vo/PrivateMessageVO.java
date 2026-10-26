package com.example.demo.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PrivateMessageVO {
    private Long id;
    private Long sendId;
    private Long recvId;
    private String content;
    private Integer type;
    private LocalDateTime sendTime;
}
