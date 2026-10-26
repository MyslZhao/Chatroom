package com.example.demo.vo;

import java.time.LocalDateTime;

import lombok.Data;
@Data
public class GroupMessageVO {
    private Long id;
    private Long sendId;
    private String sendNickName;
    private Long groupId;
    private String content;
    private Integer type;
    private LocalDateTime sendTime;


}
