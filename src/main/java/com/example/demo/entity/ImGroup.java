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
 * 群
 * </p>
 *
 * @author author
 * @since 2026-10-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("im_group")
//@ApiModel(value="ImGroup对象", description="群")
public class ImGroup implements Serializable {

    private static final long serialVersionUID = 1L;

//    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

//    @ApiModelProperty(value = "群名字")
    @TableField("name")
    private String name;

    //@ApiModelProperty(value = "群主id")
    @TableField("owner_id")
    private Long ownerId;

//    @ApiModelProperty(value = "群头像")
    @TableField("head_image")
    private String headImage;

//    @ApiModelProperty(value = "群头像缩略图")
    @TableField("head_image_thumb")
    private String headImageThumb;

//    @ApiModelProperty(value = "群公告")
    @TableField("notice")
    private String notice;

//    @ApiModelProperty(value = "是否被封禁 0:否 1:是")
    @TableField("is_banned")
    private Boolean isBanned;

//    @ApiModelProperty(value = "被封禁原因")
    @TableField("reason")
    private String reason;

//    @ApiModelProperty(value = "是否已解散")
    @TableField("dissolve")
    private Boolean dissolve;

//    @ApiModelProperty(value = "创建时间")
    @TableField("created_time")
    private LocalDateTime createdTime;


}
