package com.atguigu.gmall.manage.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.bean.PmsBaseSaleAttr;
import com.atguigu.gmall.bean.PmsProductImage;
import com.atguigu.gmall.bean.PmsProductInfo;
import com.atguigu.gmall.bean.PmsProductSaleAttr;
import com.atguigu.gmall.service.SpuService;
import com.atguigu.gmall.util.MyUploadUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@CrossOrigin
public class SpuController {

    @Reference
    SpuService spuService;


    /**
     *显示销售属性
     * @param spuId
     * @return
     */
    @ResponseBody
    @RequestMapping("spuSaleAttrList")
    public List<PmsProductSaleAttr> spuSaleAttrList(String spuId){
        List<PmsProductSaleAttr> pmsProductSaleAttrs =  spuService.spuSaleAttrList(spuId);
        return pmsProductSaleAttrs;
    }

    /**
     * 在点击添加
     *显示图片
     * @param spuId
     * @return
     */
    @ResponseBody
    @RequestMapping("spuImageList")
    public List<PmsProductImage> spuImageList(String spuId){
        List<PmsProductImage> pmsProductImages= spuService.spuImageList(spuId);
        return pmsProductImages;

    }

    /***
     * 显示销售属性
     * @return
     */
    @ResponseBody
    @RequestMapping("baseSaleAttrList")
    public List<PmsBaseSaleAttr> baseSaleAttrList(){
        List<PmsBaseSaleAttr> pmsBaseSaleAttrs =spuService.baseSaleAttrList();
        return  pmsBaseSaleAttrs;
    }


    /**
     * 显示所有的spu
     * @param catalog3Id
     * @return
     */
    @ResponseBody
    @RequestMapping("spuList")
    public List<PmsProductInfo> spuList(String catalog3Id){
        List<PmsProductInfo> pmsProductInfos= spuService.supList(catalog3Id);
        return pmsProductInfos;
    }


    /**
     * 保存spu 销售属性
     * @param pmsProductInfo
     * @return
     */
    @ResponseBody
    @RequestMapping("saveSpuInfo")
    public String saveSpuInfo(@RequestBody PmsProductInfo pmsProductInfo ){
        spuService.saveSpuInfo(pmsProductInfo);
        return "success";
    }

    /**
     * 上传图片
     * @param multipartFile
     * @return
     */
    @ResponseBody
    @RequestMapping("fileUpload")
    public String fileUpload(@RequestParam("file") MultipartFile multipartFile ){


//        spuService.fileUpload(multipartFile);
        String imgUrl ="";
        imgUrl =  MyUploadUtil.upload_image(multipartFile);
//        返回上传的路径
        return imgUrl;
    }



}
