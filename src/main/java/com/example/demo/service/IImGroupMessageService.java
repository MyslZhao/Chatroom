package com.example.demo.service;

import com.example.demo.dto.GroupMessageDTO;
import com.example.demo.entity.ImGroupMessage;
import com.example.demo.vo.GroupMessageVO;

import java.util.List;


/**
 * <p>
 * 群消息 服务类
 * </p>
 *
 * @author author
 * @since 2026-10-24
 */
public interface IImGroupMessageService {
    GroupMessageVO send(Long sendId, GroupMessageDTO dto);

    List<GroupMessageVO> history(Long userId, Long groupId);

}
