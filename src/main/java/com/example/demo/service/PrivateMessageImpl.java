package com.example.demo.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.example.demo.Serve.IMHandler;
import com.example.demo.dto.PrivateMessageDto;
import com.example.demo.entity.PrivateMessageEntity;
import com.example.demo.mapper.PrivateMessageMapper;
import com.example.demo.vo.PrivateMessageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSON;

@Service
public class PrivateMessageImpl implements PrivateMessageService {

    static final Long AI_FRIEND_ID = 9999L;
    @Autowired
    private PrivateMessageMapper privateMessageMapper;
    @Autowired
    private AIService aiService;

    @Override
    public PrivateMessageVO sendMessage(Long sendId, PrivateMessageDto dto) {
        if (dto.getRecvId().equals(AI_FRIEND_ID)) {
            return  handleAiMessage(sendId, dto);
        }

        PrivateMessageEntity entity = new PrivateMessageEntity();
        entity.setSendId(sendId);
        entity.setRecvId(dto.getRecvId());
        entity.setContent(dto.getContent());
        entity.setType(dto.getType());
        entity.setSendTime(LocalDateTime.now());
        privateMessageMapper.insert(entity);

        PrivateMessageVO vo = new PrivateMessageVO();
        vo.setId(entity.getId());
        vo.setSendId(entity.getSendId());
        vo.setRecvId(entity.getRecvId());
        vo.setContent(entity.getContent());
        vo.setType(entity.getType());
        vo.setSendTime(entity.getSendTime());

        IMHandler.push(dto.getRecvId(), JSON.toJSONString(vo));
        return vo;
    }

    private PrivateMessageVO handleAiMessage(Long sendId, PrivateMessageDto dto) {
        PrivateMessageEntity userMsg = new PrivateMessageEntity();
        userMsg.setSendId(sendId);
        userMsg.setRecvId(AI_FRIEND_ID);
        userMsg.setContent(dto.getContent());
        userMsg.setType(dto.getType() == null ? 0 : dto.getType());
        userMsg.setStatus(1);
        userMsg.setSendTime(LocalDateTime.now());
        privateMessageMapper.insert(userMsg);




        // 4. 推给用户

        // 5. 返回用户自己发的那条
        PrivateMessageVO userVo = new PrivateMessageVO();
        userVo.setId(userMsg.getId());
        userVo.setSendId(sendId);
        userVo.setRecvId(AI_FRIEND_ID);
        userVo.setContent(dto.getContent());
        userVo.setType(userMsg.getType());
        userVo.setSendTime(userMsg.getSendTime());
        return userVo;













    }


    @Override
    public List<PrivateMessageVO> history(Long userId, Long friendId) {
        LambdaQueryWrapper<PrivateMessageEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.and(w -> w
                        .eq(PrivateMessageEntity::getSendId, userId)
                        .eq(PrivateMessageEntity::getRecvId, friendId))
                .or(w -> w
                        .eq(PrivateMessageEntity::getSendId, friendId)
                        .eq(PrivateMessageEntity::getRecvId, userId))
                .orderByAsc(PrivateMessageEntity::getSendTime);

        List<PrivateMessageEntity> entities = privateMessageMapper.selectList(wrapper);

        return entities.stream().map(e -> {
            PrivateMessageVO vo = new PrivateMessageVO();
            vo.setId(e.getId());
            vo.setSendId(e.getSendId());
            vo.setRecvId(e.getRecvId());
            vo.setContent(e.getContent());
            vo.setType(e.getType());
            vo.setSendTime(e.getSendTime());
            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    public Map<Long, Long> unreadCount(Long userId) {
        QueryWrapper<PrivateMessageEntity> wrapper = new QueryWrapper<>();
        wrapper.select("send_id", "COUNT(*) AS cnt")
                .eq("recv_id", userId)
                .eq("status", 0)
                .groupBy("send_id");

        List<Map<String, Object>> rows = privateMessageMapper.selectMaps(wrapper);

        Map<Long, Long> result = new HashMap<>();
        for (Map<String, Object> row : rows) {
            Long sendId = ((Number) row.get("send_id")).longValue();
            Long cnt = ((Number) row.get("cnt")).longValue();
            result.put(sendId, cnt);
        }
        return result;
    }

    @Override
    public void markRead(Long userId, Long friendId) {
        privateMessageMapper.update(null,
                new LambdaUpdateWrapper<PrivateMessageEntity>()
                        .eq(PrivateMessageEntity::getSendId, friendId)   // 对方发的
                        .eq(PrivateMessageEntity::getRecvId, userId)     // 发给我的
                        .eq(PrivateMessageEntity::getStatus, 0)          // 未读的
                        .set(PrivateMessageEntity::getStatus, 1)         // 改成已读
        );



    }

}