package com.atguigu.gmall.service;

import com.atguigu.gmall.bean.PmsBaseAttrInfo;
import com.atguigu.gmall.bean.PmsProductInfo;

import java.util.List;

public interface AttrService {
    void saveAttrInfo(PmsBaseAttrInfo pmsBaseAttrInfo);

    List<PmsBaseAttrInfo> attrInfoList(String catalog3Id);

    PmsBaseAttrInfo getAttrValueList(String attrId);
}

