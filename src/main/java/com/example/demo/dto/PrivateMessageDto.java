package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
@Data
public class PrivateMessageDto {

    @NotNull(message = "接收者id不能为空")
    private Long recvId;

    @NotBlank(message = "消息内容不能为空")
    private String content;

    /** 消息类型，默认文本 */
    private Integer type = 0;
}
