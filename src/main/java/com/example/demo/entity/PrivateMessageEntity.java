package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;


@Data
    @TableName("im_private_message")
    public class PrivateMessageEntity {

        @TableId(type = IdType.AUTO)
        private Long id;

        private Long seqNo = 0L;

        private String convKey = "";

        private Long localId = 0L;

        /** 发送者id */
        private Long sendId;

        /** 接收者id */
        private Long recvId;

        /** 消息内容 */
        private String content;

        /** 消息类型 0:文本 1:图片 2:文件 3:语音 4:视频 */
        private Integer type;

        /** 状态 0:未读 1:已读 */
        private Integer status = 0;

        /** 发送时间 */
        private LocalDateTime sendTime;
    }