package com.atguigu.gmall.manage.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.bean.*;
import com.atguigu.gmall.manage.mapper.*;
import com.atguigu.gmall.service.SpuService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class SpuServiceImpl implements SpuService {

    @Autowired
    PmsProductInfoMapper pmsProductInfoMapper;

    @Autowired
    PmsBaseSaleAttrMapper pmsBaseSaleAttrMapper;

    @Autowired
    PmsProductSaleAttrMapper pmsProductSaleAttrMapper;

    @Autowired
    PmsProductImageMapper pmsProductImageMapper;

    @Autowired
    PmsProductSaleAttrValueMapper pmsProductSaleAttrValueMapper;





    /**
     * 保存spu信息
     * @param pmsProductInfo
     */
    @Override
    public void saveSpuInfo(PmsProductInfo pmsProductInfo) {
//        保存商品信息
        pmsProductInfoMapper.insertSelective(pmsProductInfo);
//      就有了ProductInfo  的id
        String supId =pmsProductInfo.getId();

        /**
         * 单表查询
         * 保存
         */

//        添加商品的销售属性
        List<PmsProductSaleAttr> spuSaleAttrList = pmsProductInfo.getSpuSaleAttrList();
//    遍历
        for (PmsProductSaleAttr pmsProductSaleAttr :spuSaleAttrList){
            pmsProductSaleAttr.setProductId(supId);
            pmsProductSaleAttrMapper.insertSelective(pmsProductSaleAttr);
//      销售属性 对应有多个值
            List<PmsProductSaleAttrValue> spuSaleAttrValueList = pmsProductSaleAttr.getSpuSaleAttrValueList();

            // 添加销售属性值集合
            for (PmsProductSaleAttrValue pmsProductSaleAttrValue : spuSaleAttrValueList){
//
                pmsProductSaleAttrValue.setProductId(supId);
//
                pmsProductSaleAttrValueMapper.insertSelective(pmsProductSaleAttrValue);
            }
        }
//        spuImage  添加图片
        List<PmsProductImage> spuImageList = pmsProductInfo.getSpuImageList();
        if(null != spuImageList&&spuImageList.size()>0){
            for (PmsProductImage pmsProductImage :spuImageList){
                pmsProductImage.setProductId(supId);
                pmsProductImageMapper.insertSelective(pmsProductImage);
            }
        }


    }

    @Override
    public List<PmsBaseSaleAttr> baseSaleAttrList() {
        return pmsBaseSaleAttrMapper.selectAll();
    }

    /**
     * 根据 第三级分类
     * @param catalog3Id
     * @return
     */
    @Override
    public List<PmsProductInfo> supList(String catalog3Id) {
        PmsProductInfo pmsProductInfo =new PmsProductInfo();
        pmsProductInfo.setCatalog3Id(catalog3Id);
        List<PmsProductInfo> pmsProductInfos = pmsProductInfoMapper.select(pmsProductInfo);

        return pmsProductInfos;
    }

    @Override
    public List<PmsProductImage> spuImageList(String spuId) {
        PmsProductImage pmsProductImage =new PmsProductImage();
        pmsProductImage.setProductId(spuId);
        List<PmsProductImage> pmsProductImages = pmsProductImageMapper.select(pmsProductImage);
        return pmsProductImages;
    }

    /**
     * 显示销售属性
     * @param spuId
     * @return
     */
    @Override
    public List<PmsProductSaleAttr> spuSaleAttrList(String spuId) {
//
        PmsProductSaleAttr pmsProductSaleAttr = new PmsProductSaleAttr();
//        把传过来的id  setProductId
        pmsProductSaleAttr.setProductId(spuId);
//        通过pmsProductSaleAttrMapper 查询多个  PmsProductSaleAttr（多个销售属性）
        List<PmsProductSaleAttr> pmsProductSaleAttrs = pmsProductSaleAttrMapper.select(pmsProductSaleAttr);

//        为了每一个销售属性查询到PmsProductSaleAttrValue （销售属性值）    一对多的关系
//       比如 得到了 尺寸和颜色   尺寸对应多个值 （同理颜色也是）
        for (PmsProductSaleAttr productSaleAttr : pmsProductSaleAttrs) {
//      （有一个装 销售属性值 容器   new PmsProductSaleAttrValue() ）
            PmsProductSaleAttrValue pmsProductSaleAttrValue =new PmsProductSaleAttrValue();
//            你想要获取某个 销售属性值  （setProductId ，setSaleAttrId）
//          setProductId --》 页面传过来的 spuId
            pmsProductSaleAttrValue.setProductId(spuId);
            pmsProductSaleAttrValue.setSaleAttrId(productSaleAttr.getSaleAttrId());

//    你才能获取到销售属性值（某台手机的颜色 红色，绿色  ）（多个） 所以用list
            List<PmsProductSaleAttrValue> pmsProductSaleAttrValues =
                    pmsProductSaleAttrValueMapper.select(pmsProductSaleAttrValue);
//            找个 productSaleAttr 对应的属性容器装起来
            productSaleAttr.setSpuSaleAttrValueList(pmsProductSaleAttrValues);
        }

        return pmsProductSaleAttrs;
    }



    @Override
    public List<PmsProductSaleAttr> spuSaleAttrListCheckBySku(String spuId, String skuId) {
        List<PmsProductSaleAttr> pmsProductSaleAttrs = pmsProductSaleAttrMapper.spuSaleAttrListCheckBySku(spuId, skuId);
        return pmsProductSaleAttrs;
    }


}
