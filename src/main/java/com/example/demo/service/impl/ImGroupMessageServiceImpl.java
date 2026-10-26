package com.example.demo.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.demo.Serve.IMHandler;
import com.example.demo.dto.GroupMessageDTO;
import com.example.demo.entity.ImGroupMember;
import com.example.demo.entity.ImGroupMessage;
import com.example.demo.entity.UserEntity;
import com.example.demo.exception.BusinessException;
import com.example.demo.mapper.ImGroupMemberMapper;
import com.example.demo.mapper.ImGroupMessageMapper;
import com.example.demo.mapper.UserMapper;
import com.example.demo.service.IImGroupMessageService;

import com.example.demo.utils.ResultUntils;
import com.example.demo.vo.GroupMessageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 群消息 服务实现类
 * </p>
 *
 * @author author
 * @since 2026-10-24
 */
@Service
public class ImGroupMessageServiceImpl implements IImGroupMessageService {
    @Autowired
    private ImGroupMessageMapper imGroupMessageMapper;
    @Autowired
    private ImGroupMemberMapper imGroupMemberMapper;
    @Autowired
    private UserMapper userMapper;


    @Override
    public GroupMessageVO send(Long sendId, GroupMessageDTO dto) {
        // 查询当前群的最大序号
        LambdaQueryWrapper<ImGroupMessage> seqWrapper = new LambdaQueryWrapper<>();
        seqWrapper.eq(ImGroupMessage::getGroupId, dto.getGroupId())
                .orderByDesc(ImGroupMessage::getSeqNo)
                .last("LIMIT 1");
        ImGroupMessage lastMsg = imGroupMessageMapper.selectOne(seqWrapper);
        int nextSeq = (lastMsg != null && lastMsg.getSeqNo() != null) ? lastMsg.getSeqNo() + 1 : 1;

        ImGroupMessage entity = new ImGroupMessage();
        entity.setSendId(sendId);
        entity.setGroupId(dto.getGroupId());
        entity.setSeqNo(nextSeq);
        entity.setContent(dto.getContent());
        entity.setType(dto.getType() == null ? 0 : dto.getType());
        entity.setStatus(0);
        entity.setSendTime(LocalDateTime.now());
        imGroupMessageMapper.insert(entity);



        UserEntity sender = userMapper.selectById(sendId);
        GroupMessageVO vo = new GroupMessageVO();
        vo.setId(entity.getId());
        vo.setSendId(sendId);
        vo.setSendNickName(sender != null ? sender.getNickName() : "");
        vo.setGroupId(dto.getGroupId());
        vo.setContent(dto.getContent());
        vo.setType(entity.getType());
        vo.setSendTime(entity.getSendTime());







        Long groupId = dto.getGroupId();
        String content = dto.getContent();
        Integer type = dto.getType();
        //根据群id查询群成员
        LambdaQueryWrapper<ImGroupMember> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ImGroupMember::getGroupId, groupId);
        List<ImGroupMember> groupMembers = imGroupMemberMapper.selectList(wrapper);
        List<ImGroupMember> members = imGroupMemberMapper.selectList(
                new LambdaQueryWrapper<ImGroupMember>()
                        .eq(ImGroupMember::getGroupId, dto.getGroupId())
        );
        String json = JSON.toJSONString(vo);
        for (ImGroupMember member : members) {
            if (!member.getUserId().equals(sendId)) {
                IMHandler.push(member.getUserId(), json);
            }
        }
        return vo;

    }

    @Override
    public List<GroupMessageVO> history(Long userId, Long groupId) {
        // 1. 校验当前用户是否在群里
        Long count = imGroupMemberMapper.selectCount(
                new LambdaQueryWrapper<ImGroupMember>()
                        .eq(ImGroupMember::getGroupId, groupId)
                        .eq(ImGroupMember::getUserId, userId)
        );
        if (count == 0) {
            throw new BusinessException(ResultUntils.FAIL_CODE, "你不在这个群里");
        }

        // 2. 查群消息，按时间正序
        List<ImGroupMessage> entities = imGroupMessageMapper.selectList(
                new LambdaQueryWrapper<ImGroupMessage>()
                        .eq(ImGroupMessage::getGroupId, groupId)
                        .orderByAsc(ImGroupMessage::getSendTime)
        );

        // 3. 转 VO，补 sendNickName
        List<GroupMessageVO> result = new ArrayList<>();
        for (ImGroupMessage e : entities) {
            UserEntity sender = userMapper.selectById(e.getSendId());
            GroupMessageVO vo = new GroupMessageVO();
            vo.setId(e.getId());
            vo.setSendId(e.getSendId());
            vo.setSendNickName(sender != null ? sender.getNickName() : "");
            vo.setGroupId(e.getGroupId());
            vo.setContent(e.getContent());
            vo.setType(e.getType());
            vo.setSendTime(e.getSendTime());
            result.add(vo);
        }
        return result;
    }
}