package com.example.demo.utils;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.example.demo.entity.FriendEntity;
import com.example.demo.entity.ImGroup;
import com.example.demo.entity.ImGroupMember;
import com.example.demo.entity.UserEntity;
import com.example.demo.mapper.FriendMapper;
import com.example.demo.mapper.ImGroupMapper;
import com.example.demo.mapper.ImGroupMemberMapper;
import com.example.demo.mapper.UserMapper;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.chat.model.ToolContext;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class AiTools {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private FriendMapper friendMapper;
    @Autowired
    private ImGroupMemberMapper groupMemberMapper;
    @Autowired
    private ImGroupMapper groupMapper;
//    @Autowired
//    private UserMapper userMapper;
//    @Autowired
//    private ImGroupMapper groupMapper;
//    @Autowired
//    private ImGroupMemberMapper groupMemberMapper;

    @Tool(description = "查看当前登录用户的ID和基本信息（昵称、头像）")
    public String getMyInfo(ToolContext toolContext) {
        Long userId = (Long) toolContext.getContext().get("userId");
        if (userId == null) {
            return "无法获取当前用户ID";
        }
        UserEntity user=userMapper.selectById(userId);
        if (user==null){
            return "用户不存在";
        }
        return "用户ID: " + userId + ", 昵称: " + user.getNickName();
    }

    @Tool(description = "统计当前用户的好友和群聊数量及名称")
    public String stats(ToolContext toolContext) {
        Long userId = (Long) toolContext.getContext().get("userId");
        if (userId == null) return "无法获取当前用户信息";

        // 好友
        List<FriendEntity> friends = friendMapper.selectList(
                new LambdaQueryWrapper<FriendEntity>()
                        .eq(FriendEntity::getUserId, userId)
        );
        List<String> friendNames = friends.stream()
                .map(FriendEntity::getFriendNickName)
                .collect(Collectors.toList());

        // 群聊
        List<ImGroupMember> memberships = groupMemberMapper.selectList(
                new LambdaQueryWrapper<ImGroupMember>()
                        .eq(ImGroupMember::getUserId, userId)
        );
        List<String> groupNames = memberships.isEmpty() ? List.of() :
                groupMapper.selectBatchIds(
                        memberships.stream()
                        .map(ImGroupMember::getGroupId)
                        .collect(Collectors.toList())
                ).stream().map(ImGroup::getName).collect(Collectors.toList());

        return String.format("你有 %d 个好友：%s；%d 个群聊：%s",
                friendNames.size(),
                friendNames.isEmpty() ? "暂无" : String.join("、", friendNames),
                groupNames.size(),
                groupNames.isEmpty() ? "暂无" : String.join("、", groupNames));
    }

    @Tool(description = "查看指定群聊的成员列表，参数 groupName 是群名称，根据名称查群成员")
    public String getGroupMembers(
            @ToolParam(description = "群名称") String groupName,
            ToolContext toolContext) {
        Long userId = (Long) toolContext.getContext().get("userId");
        if (userId == null) return "无法获取当前用户信息，请先登录。";

        // 1. 按名称查群（当前用户加入的群）
        List<ImGroupMember> memberships = groupMemberMapper.selectList(
                new LambdaQueryWrapper<ImGroupMember>()
                        .eq(ImGroupMember::getUserId, userId)
        );
        if (memberships.isEmpty()) return "你还没有加入任何群聊。";

        List<Long> groupIds = memberships.stream()
                .map(ImGroupMember::getGroupId)
                .collect(Collectors.toList());

        List<ImGroup> groups = groupMapper.selectList(
                new LambdaQueryWrapper<ImGroup>()
                        .in(ImGroup::getId, groupIds)
                        .like(ImGroup::getName, groupName)
        );
        if (groups.isEmpty()) return "没有找到名称包含「" + groupName + "」的群。";

        ImGroup group = groups.get(0);

        // 2. 查群成员
        List<ImGroupMember> members = groupMemberMapper.selectList(
                new LambdaQueryWrapper<ImGroupMember>()
                        .eq(ImGroupMember::getGroupId, group.getId())
        );

        List<String> names = new ArrayList<>();
        for (ImGroupMember m : members) {
            UserEntity u = userMapper.selectById(m.getUserId());
            names.add(u != null ? u.getNickName() : "未知用户");
        }

        return String.format("群「%s」有 %d 个成员：%s",
                group.getName(), names.size(), String.join("、", names));
    }

    @Tool(description = "修改当前用户的昵称，参数 newNickName 是新的昵称")
    public String updateNickName(
            @ToolParam(description = "新的昵称") String newNickName,
            ToolContext toolContext) {
        Long userId = (Long) toolContext.getContext().get("userId");
        if (userId == null) return "无法获取当前用户信息，请先登录。";
        if (newNickName == null || newNickName.trim().isEmpty()) {
            return "昵称不能为空。";
        }
        if (newNickName.trim().length() > 20) {
            return "昵称不能超过 20 个字符。";
        }

        String nick = newNickName.trim();

        userMapper.update(null, new LambdaUpdateWrapper<UserEntity>()
                .eq(UserEntity::getId, userId)
                .set(UserEntity::getNickName, nick));

        friendMapper.update(null, new LambdaUpdateWrapper<FriendEntity>()
                .eq(FriendEntity::getFriendId, userId)
                .set(FriendEntity::getFriendNickName, nick));

        groupMemberMapper.update(null, new LambdaUpdateWrapper<ImGroupMember>()
                .eq(ImGroupMember::getUserId, userId)
                .set(ImGroupMember::getUserNickName, nick));

        return "昵称已修改为：" + nick;
    }
}