package com.atguigu.gmall.service;

import com.atguigu.gmall.bean.*;

import java.util.List;

public interface SpuService {
    void saveSpuInfo(PmsProductInfo pmsProductInfo);

    List<PmsBaseSaleAttr> baseSaleAttrList();


    List<PmsProductInfo> supList(String catalog3Id);


    List<PmsProductImage> spuImageList(String spuId);

    List<PmsProductSaleAttr> spuSaleAttrList(String spuId);


//    List<PmsProductSaleAttr> spuSaleAttrListChecked(String spuId, String skuId);

    List<PmsProductSaleAttr> spuSaleAttrListCheckBySku(String spuId, String skuId);


//    void fileUpload(MultipartFile multipartFile);
}
