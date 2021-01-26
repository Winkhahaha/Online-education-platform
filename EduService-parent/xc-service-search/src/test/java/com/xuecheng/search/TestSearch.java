package com.xuecheng.search;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author GaoMing
 * @version 1.0
 **/
@SpringBootTest(classes = SearchApplication.class)
@RunWith(SpringRunner.class)
public class TestSearch {

    @Autowired
    RestHighLevelClient client;

    @Autowired
    RestClient restClient;

    // 搜索全部记录
    @Test
    public void testSearchAll() throws IOException, ParseException {
        // 搜索源构建对象
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        // 设置搜索方式:matchAll
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        queryResultSearchSourceBuilder(searchSourceBuilder);
    }

    // 分页查询
    @Test
    public void testSearchPage() throws IOException, ParseException {
        // 搜索源构建对象
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        // 设置分页参数
        int page = 1;
        int size = 1;
        searchSourceBuilder.from((page - 1) * size); // 起始记录下标0开始
        searchSourceBuilder.size(size); // 每页显示的记录数
        // 设置搜索方式:matchAll
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        queryResultSearchSourceBuilder(searchSourceBuilder);
    }

    // 精确匹配
    @Test
    public void testSearchTerm() throws IOException, ParseException {
        // 搜索源构建对象
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        // 设置搜索方式:精确匹配字段name的value为Spring
        searchSourceBuilder.query(QueryBuilders.termQuery("name", "Spring"));
        queryResultSearchSourceBuilder(searchSourceBuilder);
    }

    // 根据id精确查询
    @Test
    public void testSearchByIds() throws IOException, ParseException {
        // 搜索源构建对象
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        // 设置要查询的主键id
        String[] ids = new String[]{"1", "2"};
        // 设置搜索方式根据id查询(因为id是非source字段,因此需要_id)
        // searchSourceBuilder.query(QueryBuilders.termQuery("_id", "1")); // 查一个
        searchSourceBuilder.query(QueryBuilders.termsQuery("_id", ids));    // 查多个
        queryResultSearchSourceBuilder(searchSourceBuilder);
    }

    // match
    @Test
    public void testSearchMatchQuery() throws IOException, ParseException {
        // 搜索源构建对象
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        // 设置搜索方式:至少3*0.8个词匹配到
        searchSourceBuilder.query(QueryBuilders.matchQuery("description", "spring开发框架").minimumShouldMatch("80%"));    // 查多个
        queryResultSearchSourceBuilder(searchSourceBuilder);
    }

    // Multi匹配多个字段并设置某字段权重
    @Test
    public void testSearchMulti() throws IOException, ParseException {
        // 搜索源构建对象
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        // 设置搜索方式:多字段匹配
        searchSourceBuilder.query(QueryBuilders.multiMatchQuery("spring css", "name", "description")
                .minimumShouldMatch("50%")
                // 增加该字段的查询比重
                .field("name", 10));
        queryResultSearchSourceBuilder(searchSourceBuilder);
    }

    // bool查询
    @Test
    public void testSearchBool() throws IOException, ParseException {
        // 搜索源构建对象
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        // 定义multiMatchQuery
        MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery("spring css", "name", "description")
                .minimumShouldMatch("50%")
                // 增加该字段的查询比重
                .field("name", 10);
        // 定义termQuery
        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("studymodel", "201001");
        // 定义boolQuery
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        // 装配
        boolQueryBuilder.must(multiMatchQueryBuilder);
        boolQueryBuilder.must(termQueryBuilder);
        // 设置搜索方式
        searchSourceBuilder.query(boolQueryBuilder);
        queryResultSearchSourceBuilder(searchSourceBuilder);
    }

    // filter:对搜索结果进行过滤
    @Test
    public void testSearchFilter() throws IOException, ParseException {
        // 搜索源构建对象
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        // 定义multiMatchQuery
        MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery("spring css", "name", "description")
                .minimumShouldMatch("50%")
                // 增加该字段的查询比重
                .field("name", 10);
        // 定义boolQuery
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        // 定义过滤器
        boolQueryBuilder.filter(QueryBuilders.termQuery("studymodel", "201001"));
        boolQueryBuilder.filter(QueryBuilders.rangeQuery("price").gte(60).lte(100));
        // 装配
        boolQueryBuilder.must(multiMatchQueryBuilder);
        // 设置搜索方式
        searchSourceBuilder.query(boolQueryBuilder);
        queryResultSearchSourceBuilder(searchSourceBuilder);
    }

    // sort:排序
    @Test
    public void testSearchSort() throws IOException, ParseException {
        // 搜索源构建对象
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        // 定义multiMatchQuery
        MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery("开发框架", "name", "description")
                .minimumShouldMatch("50%")
                // 增加该字段的查询比重
                .field("name", 10);
        // 定义boolQuery
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        // 定义过滤器
        boolQueryBuilder.filter(QueryBuilders.rangeQuery("price").gte(60).lte(100));
        // 设置搜索方式
        searchSourceBuilder.query(boolQueryBuilder);
        // 添加排序
        searchSourceBuilder.sort("studymodel", SortOrder.DESC);
        searchSourceBuilder.sort("price", SortOrder.ASC);
        queryResultSearchSourceBuilder(searchSourceBuilder);
    }

    // 高亮显示:排序
    @Test
    public void testSearchHighlight() throws IOException, ParseException {
        // 搜索源构建对象
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        // 定义boolQuery
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        // 定义过滤器
        boolQueryBuilder.filter(QueryBuilders.rangeQuery("price").gte(60).lte(100));
        // 设置搜索方式
        searchSourceBuilder.query(boolQueryBuilder);
        // 添加排序
        searchSourceBuilder.sort("studymodel", SortOrder.DESC);
        searchSourceBuilder.sort("price", SortOrder.ASC);
        queryResultSearchSourceBuilder(searchSourceBuilder);
    }

    public void queryResultSearchSourceBuilder(SearchSourceBuilder searchSourceBuilder) throws IOException, ParseException {
        // 创建搜索请求对象
        SearchRequest searchRequest = new SearchRequest("xc_course");
        // 指定类型
        searchRequest.types("doc");
        // 设置源字段过滤,第一个参数结果集包括哪些字段,第二个参数表示结果集不包括哪些字段
        searchSourceBuilder.fetchSource(new String[]{"name", "studymodel", "price", "timestamp"}, new String[]{"description"});
        /**
         * 设置搜索关键字高亮
         */
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.preTags("<tag>");
        highlightBuilder.postTags("</tag>");
        highlightBuilder.fields().add(new HighlightBuilder.Field("name"));
        // 向搜索中请求对象设置搜索源
        searchRequest.source(searchSourceBuilder);
        // 执行搜索
        SearchResponse searchResponse = client.search(searchRequest);
        // 搜索结果
        SearchHits hits = searchResponse.getHits();
        // 匹配到的总记录数
        long totalHits = hits.getTotalHits();
        // 得到匹配度高的文档
        SearchHit[] searchHits = hits.getHits();
        // 日期格式化对象
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (SearchHit hit : searchHits) {
            // 文档的主键
            String id = hit.getId();
            // 源文档内容
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            String name = (String) sourceAsMap.get("name");
            // 取出name高亮字段
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            HighlightField highlightField = Optional.ofNullable(highlightFields.get("name")).orElse(null);
            // 使用高亮name替换上面的name
            name = Arrays.stream(highlightField.getFragments()).map(Text::toString).collect(Collectors.joining());
            String studymodel = (String) sourceAsMap.get("studymodel");
            Double price = (Double) sourceAsMap.get("price");
            Date timestamp = dateFormat.parse((String) sourceAsMap.get("timestamp"));
            System.out.println(name + " " + studymodel + " " + price + " " + timestamp);
        }
    }
}
