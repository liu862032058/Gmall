package com.atguigu.gmall.manage.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.atguigu.gmall.bean.PmsSkuAttrValue;
import com.atguigu.gmall.bean.PmsSkuImage;
import com.atguigu.gmall.bean.PmsSkuInfo;
import com.atguigu.gmall.bean.PmsSkuSaleAttrValue;
import com.atguigu.gmall.manage.mapper.PmsSkuAttrValueMapper;
import com.atguigu.gmall.manage.mapper.PmsSkuImageMapper;
import com.atguigu.gmall.manage.mapper.PmsSkuInfoMapper;
import com.atguigu.gmall.manage.mapper.PmsSkuSaleAttrValueMapper;
import com.atguigu.gmall.service.SkuService;
import com.atguigu.gmall.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
public class SkuServiceImpl implements SkuService {

    @Autowired
    PmsSkuInfoMapper pmsSkuInfoMapper;

    @Autowired
    PmsSkuImageMapper pmsSkuImageMapper;

    @Autowired
    PmsSkuSaleAttrValueMapper pmsSkuSaleAttrValueMapper;

    @Autowired
    PmsSkuAttrValueMapper pmsSkuAttrValueMapper;

    @Autowired
    RedisUtil redisUtil;

    /**
     * 保存商品信息
     * @param pmsSkuInfo
     */
    @Override
    public void saveSkuInfo(PmsSkuInfo pmsSkuInfo) {

//        保存sku 生成主键  （已经插入数据库 ，就可以获取）
//        sku 的id  然后 就可以 通过当前的id 过去各个属性（平台属性，和销售属性 进行获取）
//先把整体的信息保存起来，然后就有了 pmsSkuInfo的id  通过这个id 获取各个属性值，
//                                保存都各个表中 （图片的，保存在图片的那个表）
//       平台 和 销售（值或者属性）都保存对应各个表里
        pmsSkuInfoMapper.insertSelective(pmsSkuInfo);
        String skuId =pmsSkuInfo.getId();


//      保存skuimage
        List<PmsSkuImage> skuImageList = pmsSkuInfo.getSkuImageList();
        for (PmsSkuImage pmsSkuImage : skuImageList) {
            pmsSkuImage.setSkuId(skuId);
            pmsSkuImageMapper.insertSelective(pmsSkuImage);
        }

//      保存sku销售属性
        List<PmsSkuSaleAttrValue> skuSaleAttrValueList = pmsSkuInfo.getSkuSaleAttrValueList();
        for (PmsSkuSaleAttrValue pmsSkuSaleAttrValue : skuSaleAttrValueList) {
            pmsSkuSaleAttrValue.setId(skuId);
            pmsSkuSaleAttrValueMapper.insertSelective(pmsSkuSaleAttrValue);
        }
//       保存  sku 平台属性关联信息
        List<PmsSkuAttrValue> skuAttrValueList = pmsSkuInfo.getSkuAttrValueList();
        for (PmsSkuAttrValue pmsSkuAttrValue : skuAttrValueList) {
            pmsSkuAttrValue.setSkuId(skuId);
            pmsSkuAttrValueMapper.insertSelective(pmsSkuAttrValue);
        }
    }

    /**
     * 获取skp 详情信息
     * @param skuId
     * @return
     */
    @Override
    public PmsSkuInfo item(String skuId) {
        System.out.println("进入商品详情");
        PmsSkuInfo pmsSkuInfo =null;
//        创建jedis
        Jedis jedis =null;

        try {
            // 先查询缓存
            jedis= redisUtil.getJedis();
            // key=object:id:field ,sku:id:info
            String key = jedis.get("sku:"+skuId+":info");
            //判断key存不存在
            if (StringUtils.isNotBlank(key)){

                pmsSkuInfo= JSON.parseObject(key,PmsSkuInfo.class);
                System.out.println("获得缓存数据");

            }else {
                System.out.println("没有获得缓存数据，领取分布式锁");
                String lock = "sku:"+skuId+":lock";
                String uuid = java.util.UUID.randomUUID().toString();
                String OK = jedis.set(lock, uuid, "nx", "px", 10000);

                if(StringUtils.isNotBlank(OK)&& OK.equals("OK")){
                    pmsSkuInfo=itemFromDb(skuId);

                    if (pmsSkuInfo != null){
                        System.out.println("没有获得缓存数据，但是获得分布式锁，" +
                                "访问db，将返回数据存入缓存,删除分布式锁");
                        jedis.set("sku:"+skuId+":info",JSON.toJSONString(pmsSkuInfo));
//                        // 防止误删其他线程的锁
                        String script ="if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
                        jedis.eval(script, Collections.singletonList(lock),Collections.singletonList(uuid));
                    }else {
                        System.out.println("数据库中不存在该值");
                    }
                }else {
//                    自旋
                    System.out.println("没有获得缓存数据，也没有获得分布式锁，开始自旋。。。。。。。。。。。");
                    return item(skuId);
                }
            }
        }catch (Exception e){

        }finally {
            jedis.close();
        }
        return  pmsSkuInfo;
    }

    public PmsSkuInfo itemFromDb(String skuId) {
//        获取一个sku 信息
        PmsSkuInfo pmsSkuInfo =new PmsSkuInfo();
        pmsSkuInfo.setId(skuId);
        PmsSkuInfo pmsSkuInfo1 = pmsSkuInfoMapper.selectOne(pmsSkuInfo);

        PmsSkuImage pmsSkuImage =new PmsSkuImage();
        pmsSkuImage.setSkuId(skuId);
//        获取所有的详情图片
        List<PmsSkuImage> pmsSkuImages = pmsSkuImageMapper.select(pmsSkuImage);
        pmsSkuInfo1.setSkuImageList(pmsSkuImages);


        return pmsSkuInfo1;
    }

    @Override
    public List<PmsSkuInfo> skuSaleAttrValueListBySpu(String spuId) {
//取出sku 但 sku里没有属性的值
        PmsSkuInfo pmsSkuInfo = new PmsSkuInfo();
        pmsSkuInfo.setProductId(spuId);
        List<PmsSkuInfo> pmsSkuInfoList = pmsSkuInfoMapper.select(pmsSkuInfo);
//        设值
        for (PmsSkuInfo skuInfo : pmsSkuInfoList) {

            PmsSkuSaleAttrValue pmsSkuSaleAttrValue = new PmsSkuSaleAttrValue();
            pmsSkuSaleAttrValue.setSkuId(skuInfo.getId());

            List<PmsSkuSaleAttrValue> pmsSkuSaleAttrValues = pmsSkuSaleAttrValueMapper.select(pmsSkuSaleAttrValue);
            skuInfo.setSkuSaleAttrValueList(pmsSkuSaleAttrValues);
        }



        return pmsSkuInfoList;
    }
}
