package com.atguigu.gmall.manage.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.bean.PmsBaseAttrInfo;
import com.atguigu.gmall.bean.PmsBaseAttrValue;
import com.atguigu.gmall.bean.PmsProductInfo;
import com.atguigu.gmall.service.AttrService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Controller
@CrossOrigin
public class AttrController {

    @Reference
    AttrService attrService;

    @RequestMapping("getAttrValueList")
    @ResponseBody
    public PmsBaseAttrInfo getAttrValueList(String attrId){
        PmsBaseAttrInfo pmsBaseAttrInfo =attrService.getAttrValueList(attrId);
        return pmsBaseAttrInfo;
    }


        /**?=61
         * 显示信息*
         * @param catalog3Id
         * @return
         */
    @RequestMapping("attrInfoList")
    @ResponseBody
    public List<PmsBaseAttrInfo> attrInfoList(String catalog3Id){

        List<PmsBaseAttrInfo> pmsBaseAttrInfos= attrService.attrInfoList(catalog3Id);

        return  pmsBaseAttrInfos;
    }


    /**
     * 保存信息
     * @RequestBody PmsBaseAttrInfo
     * @return
     */
    @RequestMapping("saveAttrInfo")
    @ResponseBody
    public String saveAttrInfo(@RequestBody PmsBaseAttrInfo pmsBaseAttrInfo){
            attrService.saveAttrInfo(pmsBaseAttrInfo);
        return "success";
    }




}
