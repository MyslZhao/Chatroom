package com.example.demo.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.demo.entity.FriendEntity;
import com.example.demo.entity.UserEntity;
import com.example.demo.exception.BusinessException;
import com.example.demo.mapper.FriendMapper;
import com.example.demo.mapper.UserMapper;
import com.example.demo.utils.ResultUntils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.demo.service.PrivateMessageImpl.AI_FRIEND_ID;

@Service
public class FriendImpl implements FriendService {
    @Autowired
    private FriendMapper friendMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ResultUntils resultUtils;


    @Override
    public List<FriendEntity> list(Long userId) {
        List<FriendEntity> friends = friendMapper.selectList(
                new LambdaQueryWrapper<FriendEntity>()
                        .eq(FriendEntity::getUserId, userId)
                        .orderByDesc(FriendEntity::getCreatedTime)
        );

        // 追加 AI 好友（不存 im_friend，动态拼一条）
        if (!userId.equals(AI_FRIEND_ID)) {
            UserEntity ai = userMapper.selectById(AI_FRIEND_ID);
            if (ai != null) {
                FriendEntity aiFriend = new FriendEntity();
                aiFriend.setUserId(userId);
                aiFriend.setFriendId(AI_FRIEND_ID);
                aiFriend.setFriendNickName(ai.getNickName());
                aiFriend.setFriendHeadImage(ai.getHeadImage());
                aiFriend.setCreatedTime(LocalDateTime.now());
                friends.add(0, aiFriend);   // 放到列表最前面
            }
        }
        return friends;
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addFriend(Long userId, Long friendId) {



        if (userId.equals(friendId)) {
            throw new BusinessException(ResultUntils.FAIL_CODE, "不能添加自己为好友");
        }

        UserEntity friend = userMapper.selectById(friendId);
        if (friend == null) {
            throw new BusinessException(ResultUntils.FAIL_CODE, "用户不存在");
        }

        Long count = friendMapper.selectCount(
                new LambdaQueryWrapper<FriendEntity>()
                        .eq(FriendEntity::getUserId, userId)
                        .eq(FriendEntity::getFriendId, friendId)
        );
        if (count > 0) {
            throw new BusinessException(ResultUntils.FAIL_CODE, "已经是好友了");
        }

        LocalDateTime now = LocalDateTime.now();

        FriendEntity mySide = new FriendEntity();
        mySide.setUserId(userId);
        mySide.setFriendId(friendId);
        mySide.setFriendNickName(friend.getNickName());
        mySide.setFriendHeadImage(friend.getHeadImage());
        mySide.setCreatedTime(now);
        friendMapper.insert(mySide);

        UserEntity me = userMapper.selectById(userId);
        FriendEntity otherSide = new FriendEntity();
        otherSide.setUserId(friendId);
        otherSide.setFriendId(userId);
        otherSide.setFriendNickName(me.getNickName());
        otherSide.setFriendHeadImage(me.getHeadImage());
        otherSide.setCreatedTime(now);
        friendMapper.insert(otherSide);
    }
}


