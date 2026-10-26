package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("im_user")
public class UserEntity {
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 用户名，登录用，唯一索引 */
    private String userName;

    /** 用户昵称 */
    private String nickName;

    /** 用户头像 */
    private String headImage;

    /** 用户头像缩略图 */
    private String headImageThumb;

    /** 密码（BCrypt 密文） */
    private String password;

    /** 性别 0:男 1:女 */
    private Integer sex;

    /** 是否被封禁 0:否 1:是 */
    private Integer isBanned;

    /** 被封禁原因 */
    private String reason;

    /** 用户类型 1:普通用户 2:审核账户 */
    private Integer type;

    /** 个性签名 */
    private String signature;

    /** 最后登录时间 */
    private LocalDateTime lastLoginTime;

    /** 创建时间 */
    private LocalDateTime createdTime;
}