package com.atguigu.gmall.user.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.bean.UmsMember;
import com.atguigu.gmall.service.UmsMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class UserController {

    @Reference
    UmsMemberService umsMemberService;

    @RequestMapping("index")
    @ResponseBody
    public String index(){

        return "index";
    }

    @RequestMapping("getAllUser")
    @ResponseBody
    public List<UmsMember> getAllUser(){
       List<UmsMember> list= umsMemberService.getAllUser();
        return list;
    }

    @RequestMapping("getAllUserMapper")
    @ResponseBody
    public List<UmsMember> getAllUserMapper(){
        List<UmsMember> list= umsMemberService.getAllUserMapper();
        return list;
    }
}
