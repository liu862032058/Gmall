package com.atguigu.gmall.gmallsearchservice.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.bean.PmsSearchParam;
import com.atguigu.gmall.bean.PmsSearchSkuInfo;
import com.atguigu.gmall.bean.PmsSkuInfo;
import com.atguigu.gmall.service.SearchService;
import io.searchbox.client.JestClient;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import jdk.nashorn.internal.ir.annotations.Reference;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.index.query.TermsQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.highlight.HighlightBuilder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class SearchServiceImpl implements SearchService {


    @Autowired
    JestClient jestClient;




    /**
     * 三级分类和关键字搜索 二选一
     * @param pmsSearchParam
     * @return
     */

    @Override
    public List<PmsSearchSkuInfo> search(PmsSearchParam pmsSearchParam) {

        List<PmsSearchSkuInfo> pmsSearchSkuInfoList = new ArrayList<>();
        String query=myQuery(pmsSearchParam);
        System.out.println(query);

        Search search = new Search.Builder(query).addIndex("gmall0722").addType("PmsSearchSkuInfo").build();
        try {
            SearchResult execute = jestClient.execute(search);
            if (execute!=null){
                List<SearchResult.Hit<PmsSearchSkuInfo, Void>> hits = execute.getHits(PmsSearchSkuInfo.class);
                if (hits !=null){
                    for (SearchResult.Hit<PmsSearchSkuInfo, Void> hit : hits) {
                        PmsSearchSkuInfo pmsSearchSkuInfo = hit.source;
                        Map<String, List<String>> highlight = hit.highlight;
                        String highlightSkuName = highlight.get("skuName").get(0);
                        if (highlight !=null){
                            pmsSearchSkuInfo.setSkuName(highlightSkuName);
                        }
                        pmsSearchSkuInfoList.add(pmsSearchSkuInfo);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pmsSearchSkuInfoList;
    }


    public String myQuery(PmsSearchParam pmsSearchParam)  {

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        String catalog3Id = pmsSearchParam.getCatalog3Id();
        String keyword = pmsSearchParam.getKeyword();
        String[] valueIds = pmsSearchParam.getValueId();


        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
//        三级分类
        if (StringUtils.isNotBlank(catalog3Id)){
            TermsQueryBuilder termQueryBuilder = new TermsQueryBuilder("catalog3Id",catalog3Id);
            boolQueryBuilder.filter(termQueryBuilder);
        }
//        关键字
        if (StringUtils.isNotBlank(keyword)){
            MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder("skuName", keyword);
            boolQueryBuilder.must(matchQueryBuilder);
//            高亮
            HighlightBuilder highlightBuilder =new HighlightBuilder();
            highlightBuilder.field("skuName");
            highlightBuilder.preTags("<span style='color:red;font-weigh:bolder;'>");
            highlightBuilder.postTags("</span>");
            searchSourceBuilder.highlight(highlightBuilder);

        }

//属性值过滤
        if (valueIds !=null && valueIds.length>0){
            for (String vaueId : valueIds) {
                TermQueryBuilder termQueryBuilder=new TermQueryBuilder("skuAttrValueList.valueId",vaueId);
                boolQueryBuilder.filter(termQueryBuilder);
            }
        }
        searchSourceBuilder.query(boolQueryBuilder);
        searchSourceBuilder.from(0);
        searchSourceBuilder.size(20);

        return searchSourceBuilder.toString();
    }
}
