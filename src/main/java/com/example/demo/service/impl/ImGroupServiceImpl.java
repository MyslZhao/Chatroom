package com.example.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.demo.dto.GroupCreateDto;
import com.example.demo.entity.ImGroup;
import com.example.demo.entity.ImGroupMember;
import com.example.demo.entity.ImGroupMessage;
import com.example.demo.entity.UserEntity;
import com.example.demo.mapper.ImGroupMapper;
import com.example.demo.mapper.ImGroupMemberMapper;
import com.example.demo.mapper.ImGroupMessageMapper;
import com.example.demo.mapper.UserMapper;
import com.example.demo.service.IImGroupService;

import com.example.demo.vo.GroupMemberVO;
import com.example.demo.vo.GroupVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 群 服务实现类
 * </p>
 *
 * @author author
 * @since 2026-10-24
 */
@Service
public class ImGroupServiceImpl implements IImGroupService {
    @Autowired
    private ImGroupMapper imGroupMapper;
    @Autowired
    private ImGroupMemberMapper imGroupMemberMapper;
    @Autowired
    private ImGroupMessageMapper imGroupMessageMapper;
    @Autowired
    private UserMapper userMapper;


    @Override
    public GroupVO create(Long ownerId, GroupCreateDto dto) {

        ImGroup group = new ImGroup();
        group.setName(dto.getName());
        group.setOwnerId(ownerId);
        group.setHeadImage("");
        group.setCreatedTime(LocalDateTime.now());
        imGroupMapper.insert(group);
        UserEntity owner = userMapper.selectById(ownerId);



        addMember(group.getId(), ownerId, owner.getNickName());



        // 3. 初始成员加入
        if (dto.getMemberIds() != null) {
            for (Long memberId : dto.getMemberIds()) {
                if (memberId.equals(ownerId)) continue;   // 群主已加过
                UserEntity user = userMapper.selectById(memberId);
                if (user == null) continue;               // 用户不存在就跳过
                addMember(group.getId(), memberId, user.getNickName());
            }
        }

        return toVO(group);
    }
    private void addMember(Long groupId, Long userId, String nickName) {
        ImGroupMember member = new ImGroupMember();
        member.setGroupId(groupId);
        member.setUserId(userId);
        member.setUserNickName(nickName);
        imGroupMemberMapper.insert(member);   // 插入 im_group_member
    }

    private GroupVO toVO(ImGroup group) {
        GroupVO vo = new GroupVO();
        vo.setId(group.getId());
        vo.setName(group.getName());
        vo.setOwnerId(group.getOwnerId());
        vo.setHeadImage(group.getHeadImage());
        vo.setCreatedTime(group.getCreatedTime());
        return vo;
    }


    @Override
    public List<GroupVO> myGroups(Long userId) {
        // 1. 查我加入了哪些群（含已读序号）
        List<ImGroupMember> memberships = imGroupMemberMapper.selectList(
                new LambdaQueryWrapper<ImGroupMember>()
                        .eq(ImGroupMember::getUserId, userId)
        );
        if (memberships.isEmpty()) {
            return new ArrayList<>();
        }

        // 2. 拿到群ID列表
        List<Long> groupIds = memberships.stream()
                .map(ImGroupMember::getGroupId)
                .toList();

        // 3. 批量查群信息
        List<ImGroup> groups = imGroupMapper.selectBatchIds(groupIds);

        // 4. 转 VO，计算每个群的未读数
        return groups.stream().map(group -> {
            GroupVO vo = toVO(group);
            // 找到该用户在此群的成员记录，获取已读序号
            ImGroupMember myMembership = memberships.stream()
                    .filter(m -> m.getGroupId().equals(group.getId()))
                    .findFirst().orElse(null);
            int readSeq = (myMembership != null && myMembership.getReadSeqNo() != null)
                    ? myMembership.getReadSeqNo() : 0;
            // 查询该群中 seq_no > readSeq 的消息数量
            Long unread = imGroupMessageMapper.selectCount(
                    new LambdaQueryWrapper<ImGroupMessage>()
                            .eq(ImGroupMessage::getGroupId, group.getId())
                            .gt(ImGroupMessage::getSeqNo, readSeq)
            );
            vo.setUnreadCount(unread.intValue());
            return vo;
        }).toList();
    }
    @Override
    public List<GroupMemberVO> members(Long groupId) {
        // 1. 查群成员
        List<ImGroupMember> members = imGroupMemberMapper.selectList(
                new LambdaQueryWrapper<ImGroupMember>()
                        .eq(ImGroupMember::getGroupId, groupId)
        );
        if (members.isEmpty()) {
            return new ArrayList<>();
        }
        return members.stream().map(member -> {
            GroupMemberVO vo = new GroupMemberVO();
            vo.setUserId(member.getUserId());
            vo.setNickName(member.getUserNickName());
            vo.setHeadImage(member.getHeadImage());
            return vo;
        }).toList();
    }

    @Override
    public void markRead(Long userId, Long groupId) {
        // 查该群最新消息的最大 seq_no
        LambdaQueryWrapper<ImGroupMessage> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ImGroupMessage::getGroupId, groupId)
                .orderByDesc(ImGroupMessage::getSeqNo)
                .last("LIMIT 1");
        ImGroupMessage latestMsg = imGroupMessageMapper.selectOne(wrapper);
        int latestSeq = (latestMsg != null && latestMsg.getSeqNo() != null)
                ? latestMsg.getSeqNo() : 0;

        // 更新该成员在此群的已读序号
        LambdaQueryWrapper<ImGroupMember> memberWrapper = new LambdaQueryWrapper<>();
        memberWrapper.eq(ImGroupMember::getGroupId, groupId)
                .eq(ImGroupMember::getUserId, userId);
        ImGroupMember member = imGroupMemberMapper.selectOne(memberWrapper);
        if (member != null && (member.getReadSeqNo() == null || member.getReadSeqNo() < latestSeq)) {
            member.setReadSeqNo(latestSeq);
            imGroupMemberMapper.updateById(member);
        }
    }
}