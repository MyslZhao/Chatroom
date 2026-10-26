package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
@Data
public class GroupMessageDTO {

    @NotNull(message = "群id不能为空")
    private Long groupId;

    @NotBlank(message = "消息内容不能为空")
    private String content;

    /** 消息类型，默认文本 */
    private Integer type = 0;
}
