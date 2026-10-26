package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("im_friend")
public class FriendEntity {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 用户id（当前用户） */
    private Long userId;

    /** 好友id */
    private Long friendId;

    /** 好友昵称 */
    private String friendNickName;

    /** 好友头像 */
    private String friendHeadImage;

    /** 创建时间 */
    private LocalDateTime createdTime;
}