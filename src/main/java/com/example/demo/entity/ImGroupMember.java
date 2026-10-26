package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 群成员
 * </p>
 *
 * @author author
 * @since 2026-10-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("im_group_member")

public class ImGroupMember implements Serializable {

    private static final long serialVersionUID = 1L;

//    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

//    @ApiModelProperty(value = "群id")
    @TableField("group_id")
    private Long groupId;

//    @ApiModelProperty(value = "用户id")
    @TableField("user_id")
    private Long userId;

//    @ApiModelProperty(value = "用户昵称")
    @TableField("user_nick_name")
    private String userNickName;

//    @ApiModelProperty(value = "显示昵称备注")
    @TableField("remark_nick_name")
    private String remarkNickName;

//    @ApiModelProperty(value = "用户头像")
    @TableField("head_image")
    private String headImage;

//    @ApiModelProperty(value = "显示群名备注")
    @TableField("remark_group_name")
    private String remarkGroupName;

//    @ApiModelProperty(value = "免打扰标识(do not disturb)  0:关闭   1:开启")
    @TableField("is_dnd")
    private Boolean isDnd;

//    @ApiModelProperty(value = "是否置顶会话")
    @TableField("is_top")
    private Boolean isTop;

//    @ApiModelProperty(value = "是否已退出")
    @TableField("quit")
    private Boolean quit;

//    @ApiModelProperty(value = "退出时间")
    @TableField("quit_time")
    private LocalDateTime quitTime;

//    @ApiModelProperty(value = "创建时间")
    @TableField("created_time")
    private LocalDateTime createdTime;

//    @ApiModelProperty(value = "版本号")
    @TableField("version")
    private Long version;

//    @ApiModelProperty(value = "已读消息最大序号")
    @TableField("read_seq_no")
    private Integer readSeqNo;

}