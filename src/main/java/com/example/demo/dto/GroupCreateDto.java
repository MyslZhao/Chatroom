package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;

import java.util.List;
@Data
public class GroupCreateDto {

    @NotBlank(message = "群名称不能为空")
    private String name;

    /** 初始成员ID列表（不含群主自己，群主会自动加入） */
    private List<Long> memberIds;

}
