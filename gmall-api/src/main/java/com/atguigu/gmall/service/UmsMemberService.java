package com.atguigu.gmall.service;


import com.atguigu.gmall.bean.UmsMember;

import java.util.List;

public interface UmsMemberService {
    List<UmsMember> getAllUser();
    List<UmsMember> getAllUserMapper();
}
