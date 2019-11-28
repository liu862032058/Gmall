package com.atguigu.gmall.item.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.atguigu.gmall.bean.*;
import com.atguigu.gmall.service.SkuService;
import com.atguigu.gmall.service.SpuService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ItemController {

    @Reference
    SkuService skuService;
    @Reference
    SpuService spuService;

    @RequestMapping("spuSaleAttrValueJson")
    @ResponseBody
    public String spuSaleAttrValueJson(String spuId){
        List<PmsSkuInfo> pmsSkuInfos = skuService.skuSaleAttrValueListBySpu(spuId);
        Map<String ,String > mapJson =new HashMap<>();

        for (PmsSkuInfo pmsSkuInfo : pmsSkuInfos) {
            List<PmsSkuSaleAttrValue> skuSaleAttrValueList = pmsSkuInfo.getSkuSaleAttrValueList();
            String skuAttrValueKey ="";
            for (PmsSkuSaleAttrValue pmsSkuSaleAttrValue : skuSaleAttrValueList) {
                skuAttrValueKey =skuAttrValueKey+ "|"+pmsSkuSaleAttrValue.getSaleAttrValueId();
            }
            mapJson.put(skuAttrValueKey,pmsSkuInfo.getId());
        }

        // 生成一份静态的json文件存储起来()
        String json =JSON.toJSONString(mapJson);
        File file = new File("d:/spu_"+spuId+".json");
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(json.getBytes());
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }


    @RequestMapping("test")
    public String test(ModelMap map){
        String hello ="hello thymeleaf !";
        map.put("hello",hello);
        List<Integer> list =new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            list.add(i);
        }
        map.put("list",list);

        map.put("v" ,123);
        map.put("a" ,"a");

        return "a";
    }

    /**
     * 显示商品的详情页
     * @param skuId
     * @param map
     * @return
     */
    @RequestMapping("{skuId}.html")
    public String item(@PathVariable String skuId, ModelMap map){
//        查询商品详情
        PmsSkuInfo pmsSkuInfo =skuService.item(skuId);
//
        String spuId = pmsSkuInfo.getSpuId();
        // 获取属性列表
//        List<PmsProductSaleAttr> pmsProductSaleAttrs =   spuService.spuSaleAttrList(spuId);
         List<PmsProductSaleAttr> pmsProductSaleAttrs =  spuService.spuSaleAttrListCheckBySku(spuId,skuId);
        map.put("spuSaleAttrListCheckBySku",pmsProductSaleAttrs);

//        查询spu对应的组合列表  ---》一个sku 的hash表的json串
            List<PmsSkuInfo> pmsSkuInfosForSaleAttrValues =   skuService.skuSaleAttrValueListBySpu(spuId);

            Map<String ,String > mapJson =new HashMap<>();


        for (PmsSkuInfo pmsSkuInfosForSaleAttrValue : pmsSkuInfosForSaleAttrValues) {
            List<PmsSkuSaleAttrValue> skuSaleAttrValueList = pmsSkuInfosForSaleAttrValue.getSkuSaleAttrValueList();
            String saleAttrValueKey = "";
            for (PmsSkuSaleAttrValue pmsSkuSaleAttrValue : skuSaleAttrValueList) {
                saleAttrValueKey = saleAttrValueKey+"|"+pmsSkuSaleAttrValue.getSaleAttrValueId();
            }
            mapJson.put(saleAttrValueKey,pmsSkuInfosForSaleAttrValue.getId());
        }
        map.put("skuSaleAttrValueJson", JSON.toJSONString(mapJson));

        map.put("skuInfo",pmsSkuInfo);

        return "item";
    }
}
