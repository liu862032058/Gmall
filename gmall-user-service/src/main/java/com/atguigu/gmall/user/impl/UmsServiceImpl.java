package com.atguigu.gmall.user.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.bean.UmsMember;
import com.atguigu.gmall.service.UmsMemberService;
import com.atguigu.gmall.user.mapper.UmsMemberMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class UmsServiceImpl implements UmsMemberService {

    @Autowired
    UmsMemberMapper umsMemberMapper;


    @Override
    public List<UmsMember> getAllUser() {
        List<UmsMember> memberList= umsMemberMapper.selectAllMember();
        return memberList;
    }

    @Override
    public List<UmsMember> getAllUserMapper() {
        List<UmsMember> members = umsMemberMapper.selectAllMember();
        return members;
    }
}
