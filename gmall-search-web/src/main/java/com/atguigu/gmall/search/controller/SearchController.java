package com.atguigu.gmall.search.controller;


import com.atguigu.gmall.bean.PmsBaseAttrInfo;
import com.atguigu.gmall.bean.PmsSearchParam;
import com.atguigu.gmall.bean.PmsSearchSkuInfo;
import com.atguigu.gmall.bean.PmsSkuAttrValue;
import com.atguigu.gmall.service.AttrService;
import com.atguigu.gmall.service.SearchService;
import jdk.nashorn.internal.ir.annotations.Reference;


import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class SearchController {

    @Reference
    SearchService searchService;


    @Reference
    AttrService attrService;

    @RequestMapping("list.html")
    public String search(ModelMap map, PmsSearchParam pmsSearchParam){


        List<PmsSearchSkuInfo> pmsSearchSkuInfoList =   searchService.search(pmsSearchParam);

//        将查询的结果 属性值 去 重之后抽出来 到集合中   属性值
        Set<String> valueIdSet =new HashSet<>();
        for (PmsSearchSkuInfo pmsSearchSkuInfo : pmsSearchSkuInfoList) {
            List<PmsSkuAttrValue> skuAttrValueList = pmsSearchSkuInfo.getSkuAttrValueList();

            for (PmsSkuAttrValue pmsSkuAttrValue : skuAttrValueList) {
                String attrId = pmsSkuAttrValue.getAttrId();
                valueIdSet.add(attrId);
            }
        }

//          根据属性值查询页面属性和属性列表
       List<PmsBaseAttrInfo >  pmsBaseAttrInfos =attrService.search(valueIdSet);



        map.put("attrList",pmsBaseAttrInfos);
        map.put("skuLsInfoList",pmsSearchSkuInfoList);
        return "list";
    }
}
