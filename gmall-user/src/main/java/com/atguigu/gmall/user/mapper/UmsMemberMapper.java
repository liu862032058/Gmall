package com.atguigu.gmall.user.mapper;

import com.atguigu.gmall.bean.UmsMember;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;


public interface UmsMemberMapper extends Mapper<UmsMember> {

    List<UmsMember> selectAllMember();
    List<UmsMember> selectAllMemberMapper();

}
