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
 * 群消息
 * </p>
 *
 * @author author
 * @since 2026-10-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("im_group_message")

public class ImGroupMessage implements Serializable {

    private static final long serialVersionUID = 1L;

//    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

//    @ApiModelProperty(value = "业务id,由前端生成")
    @TableField("local_id")
    private String localId;

//    @ApiModelProperty(value = "群id")
    @TableField("group_id")
    private Long groupId;

//    @ApiModelProperty(value = "序列号,单个会话消息的序号连续递增")
    @TableField("seq_no")
    private Integer seqNo;

//    @ApiModelProperty(value = "发送用户id")
    @TableField("send_id")
    private Long sendId;

//    @ApiModelProperty(value = "发送用户昵称")
    @TableField("send_nick_name")
    private String sendNickName;

//    @ApiModelProperty(value = "发送内容")
    @TableField("content")
    private String content;

//    @ApiModelProperty(value = "被@的用户id列表，逗号分隔")
    @TableField("at_user_ids")
    private String atUserIds;

//    @ApiModelProperty(value = "是否回执消息")
    @TableField("receipt")
    private Boolean receipt;

//    @ApiModelProperty(value = "回执消息是否完成")
    @TableField("receipt_ok")
    private Boolean receiptOk;

//    @ApiModelProperty(value = "消息类型 0:文字 1:图片 2:文件 3:语音 4:视频 21:提示")
    @TableField("type")
    private Integer type;

//    @ApiModelProperty(value = "状态 0:未发出  2:撤回 ")
    @TableField("status")
    private Integer status;

//    @ApiModelProperty(value = "发送时间")
    @TableField("send_time")
    private LocalDateTime sendTime;


}
