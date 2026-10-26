package com.example.demo.service;

import com.example.demo.dto.GroupCreateDto;
import com.example.demo.entity.ImGroup;
import com.example.demo.vo.GroupMemberVO;
import com.example.demo.vo.GroupVO;

import java.util.List;


/**
 * <p>
 * 群 服务类
 * </p>
 *
 * @author author
 * @since 2026-10-24
 */
public interface IImGroupService {

    GroupVO create(Long ownerId, GroupCreateDto dto);

    /** 查我加入的群列表 */
    List<GroupVO> myGroups(Long userId);

    /** 查群成员 */
    List<GroupMemberVO> members(Long groupId);

    /** 标记已读 */
    void markRead(Long userId, Long groupId);

}