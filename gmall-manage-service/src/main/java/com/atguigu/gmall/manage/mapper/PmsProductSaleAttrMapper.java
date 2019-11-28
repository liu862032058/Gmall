package com.atguigu.gmall.manage.mapper;

import com.atguigu.gmall.bean.PmsProductSaleAttr;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface PmsProductSaleAttrMapper extends Mapper<PmsProductSaleAttr> {
    List<PmsProductSaleAttr> spuSaleAttrListCheckBySku(@Param("spuId") String spuId, @Param("skuId")String skuId);

}
