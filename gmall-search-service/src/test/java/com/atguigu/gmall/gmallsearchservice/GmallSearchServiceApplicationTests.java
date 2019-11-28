package com.atguigu.gmall.gmallsearchservice;



import com.atguigu.gmall.bean.PmsSearchSkuInfo;
import com.atguigu.gmall.bean.PmsSkuInfo;
import com.atguigu.gmall.gmallsearchservice.testBean.Movie;
import com.atguigu.gmall.service.SkuService;
import io.searchbox.client.JestClient;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import jdk.nashorn.internal.ir.annotations.Reference;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class GmallSearchServiceApplicationTests {

    @Autowired
    JestClient jestClient;

   @Reference
    SkuService skuService;



    @Test
    public void contextLoads() throws IOException {

//         获取所有的PmsSkuInfo
        List<PmsSkuInfo> pmsSkuInfoList =  skuService.getSkuForSearch();
        //PUT增
        List<PmsSearchSkuInfo> pmsSearchSkuInfoList = new  ArrayList<>();

        for (PmsSkuInfo pmsSkuInfo : pmsSkuInfoList) {
            PmsSearchSkuInfo pmsSearchSkuInfo = new PmsSearchSkuInfo();
            BeanUtils.copyProperties(pmsSkuInfo,pmsSearchSkuInfo);
            pmsSearchSkuInfo.setId(Long.parseLong(pmsSkuInfo.getId()));
            pmsSearchSkuInfoList.add(pmsSearchSkuInfo);
            for (PmsSearchSkuInfo searchSkuInfo : pmsSearchSkuInfoList) {
                Index build = new Index.Builder(searchSkuInfo).index("gmall0722").type("PmsSearchSkuInfo").id(searchSkuInfo.getId()+"").build();
                jestClient.execute(build);
            }


        }


    }

    @Test
    public void query() throws IOException {
        //        GET
        String query = myQuery();
        System.out.println(query);
        Search search = new Search.Builder(query).addIndex("movie_index").
                addType("movie").build();
        SearchResult execute = jestClient.execute(search);

        List<SearchResult.Hit<Movie, Void>> hits = execute.getHits(Movie.class);
        for (SearchResult.Hit<Movie, Void> hit : hits) {
            Movie source = hit.source;
            System.out.println(source.getName());
        }
    }

    public String myQuery()  {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();

        String actorids= "1";
        TermQueryBuilder termQueryBuilder = new TermQueryBuilder("actorList.id",actorids);
        boolQueryBuilder.filter(termQueryBuilder);

        MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder("name", "行动");
        boolQueryBuilder.must(matchQueryBuilder);

       searchSourceBuilder.query(boolQueryBuilder);

        searchSourceBuilder.from(0);
        searchSourceBuilder.size(20);

        return searchSourceBuilder.toString();

    }



}
