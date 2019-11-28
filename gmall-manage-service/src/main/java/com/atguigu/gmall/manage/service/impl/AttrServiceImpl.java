package com.atguigu.gmall.manage.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.bean.PmsBaseAttrInfo;
import com.atguigu.gmall.bean.PmsBaseAttrValue;
import com.atguigu.gmall.bean.PmsProductInfo;
import com.atguigu.gmall.manage.mapper.PmsBaseAttrInfoMapper;
import com.atguigu.gmall.manage.mapper.PmsBaseAttrValueMapper;
import com.atguigu.gmall.service.AttrService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Set;

@Service
public class AttrServiceImpl implements AttrService {

    @Autowired
    PmsBaseAttrInfoMapper pmsBaseAttrInfoMapper;

    @Autowired
    PmsBaseAttrValueMapper pmsBaseAttrValueMapper;




    //    显示信息
    @Override
    public List<PmsBaseAttrInfo> attrInfoList(String catalog3Id) {
//       创建基本属性 对象
        PmsBaseAttrInfo pmsBaseAttrInfo =new PmsBaseAttrInfo();
//        给基本属性赋值
        pmsBaseAttrInfo.setCatalog3Id(catalog3Id);
//        根据基本属性赋值的来查询 对象属性信息（list集合）
        List<PmsBaseAttrInfo> pmsBaseAttrInfoList = pmsBaseAttrInfoMapper.select(pmsBaseAttrInfo);
        //        循环list集合
        for (PmsBaseAttrInfo baseAttrInfo :pmsBaseAttrInfoList){
//            获取属性的id
            String id = baseAttrInfo.getId();
//            创建对应的属性 的值 的对象（ 基本属性：内存  对应的值：4G ，8G）
            PmsBaseAttrValue pmsBaseAttrValue =new PmsBaseAttrValue();
//           为对应的值 添加 id
            pmsBaseAttrValue.setAttrId(id);
//            查询出对应的值   一个属性（颜色）  可以对应多种颜色（红黄蓝）
            List<PmsBaseAttrValue> pmsBaseAttrValues = pmsBaseAttrValueMapper.select(pmsBaseAttrValue);
//            添加属性对应的值
            baseAttrInfo.setAttrValueList(pmsBaseAttrValues);
        }
        return pmsBaseAttrInfoList;
    }

    @Override
    public PmsBaseAttrInfo getAttrValueList(String attrId) {
//
        PmsBaseAttrInfo pmsBaseAttrInfo =new PmsBaseAttrInfo();
//
        pmsBaseAttrInfo.setId(attrId);
//
        PmsBaseAttrInfo pmsBaseAttrInfo1 = pmsBaseAttrInfoMapper.selectOne(pmsBaseAttrInfo);
//
        PmsBaseAttrValue pmsBaseAttrValue =new PmsBaseAttrValue();
//
        pmsBaseAttrValue.setId(attrId);
//
        List<PmsBaseAttrValue> pmsBaseAttrValues = pmsBaseAttrValueMapper.select(pmsBaseAttrValue);
//
        pmsBaseAttrInfo1.setAttrValueList(pmsBaseAttrValues);
//
        return pmsBaseAttrInfo1;
    }



    public void saveAttrInfo( PmsBaseAttrInfo pmsBaseAttrInfo){
//        保存和修改写在同一个功能里
//       有主键则为修改，没有主键为保存
        String id =pmsBaseAttrInfo.getId();

        if(StringUtils.isNotBlank(id)){
            pmsBaseAttrInfoMapper.insertSelective(pmsBaseAttrInfo);
        }else {
            pmsBaseAttrInfoMapper.updateByExampleSelective(null,null);
            PmsBaseAttrValue pmsBaseAttrValue =new PmsBaseAttrValue();
            pmsBaseAttrValue.setId(id);
            pmsBaseAttrValueMapper.delete(pmsBaseAttrValue);
        }

        String attrId = pmsBaseAttrInfo.getId();
        List<PmsBaseAttrValue> attrValueList = pmsBaseAttrInfo.getAttrValueList();
        for (PmsBaseAttrValue pmsBaseAttrValue : attrValueList) {
            pmsBaseAttrValue.setAttrId(attrId);
            pmsBaseAttrValueMapper.insertSelective(pmsBaseAttrValue);
        }


    }


    @Override
    public List<PmsBaseAttrInfo> search(Set<String> valueIdSet) {






        return null;
    }
}
