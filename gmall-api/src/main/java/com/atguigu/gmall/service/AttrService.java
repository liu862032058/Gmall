package com.atguigu.gmall.service;

import com.atguigu.gmall.bean.PmsBaseAttrInfo;
import com.atguigu.gmall.bean.PmsProductInfo;

import java.util.List;
import java.util.Set;

public interface AttrService {
    void saveAttrInfo(PmsBaseAttrInfo pmsBaseAttrInfo);

    List<PmsBaseAttrInfo> attrInfoList(String catalog3Id);

    PmsBaseAttrInfo getAttrValueList(String attrId);

    List<PmsBaseAttrInfo> search(Set<String> valueIdSet);

}

