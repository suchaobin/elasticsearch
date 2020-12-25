package com.scb.elasticsearch.service.impl;

import com.alibaba.fastjson.JSON;
import com.scb.elasticsearch.entity.Good;
import com.scb.elasticsearch.entity.User;
import com.scb.elasticsearch.service.ElasticSearchService;
import org.elasticsearch.action.admin.indices.alias.get.GetAliasesRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.snapshots.UpdateIndexShardSnapshotStatusRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.naming.directory.SearchResult;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author suchaobin
 * @description service
 * @date 2020/12/25 10:32
 **/
@Service
public class ElasticSearchServiceImpl implements ElasticSearchService {
    @Autowired
    @Qualifier("restHighLevelClient")
    private RestHighLevelClient client;


    /**
     * 创建索引
     *
     * @param index 索引名
     * @return 是否成功
     * @throws IOException io异常
     */
    @Override
    public Boolean creatIndex(String index) throws IOException {
        CreateIndexRequest request = new CreateIndexRequest(index);
        CreateIndexResponse response = client.indices().create(request, RequestOptions.DEFAULT);
        return response.isAcknowledged();
    }

    /**
     * 删除索引
     *
     * @param index 索引名
     * @return 是否成功
     * @throws IOException io异常
     */
    @Override
    public Boolean delIndex(String index) throws IOException {
        DeleteIndexRequest request = new DeleteIndexRequest(index);
        AcknowledgedResponse response = client.indices().delete(request, RequestOptions.DEFAULT);
        return response.isAcknowledged();
    }

    /**
     * 是否存在索引
     *
     * @param index 索引名
     * @return 是否存在
     * @throws IOException io异常
     */
    @Override
    public Boolean existsIndex(String index) throws IOException {
        GetIndexRequest request = new GetIndexRequest(index);
        return client.indices().exists(request, RequestOptions.DEFAULT);
    }

    /**
     * 添加用户数据到elasticsearch中
     *
     * @param index 索引名
     * @param user  用户对象
     * @return 添加是否成功
     * @throws IOException io异常
     */
    @Override
    public String addUser(String index, User user) throws IOException {
        IndexRequest request = new IndexRequest(index);
        String json = JSON.toJSONString(user);
        request.source(json, XContentType.JSON);
        IndexResponse response = client.index(request, RequestOptions.DEFAULT);
        return response.toString();
    }

    /**
     * 删除用户数据
     *
     * @param index 索引名
     * @param id  用户id
     * @return 删除是否成功
     * @throws IOException io异常
     */
    @Override
    public String delUser(String index, String id) throws IOException {
        DeleteRequest request = new DeleteRequest(index, id);
        DeleteResponse response = client.delete(request, RequestOptions.DEFAULT);
        return response.toString();
    }

    /**
     * 修改用户数据
     *
     * @param index 索引名
     * @param id    用户id
     * @param user  用户对象
     * @return 修改返回
     * @throws IOException io异常
     */
    @Override
    public String updateUser(String index, String id, User user) throws IOException {
        UpdateRequest request = new UpdateRequest(index, id);
        String json = JSON.toJSONString(user);
        request.doc(json, XContentType.JSON);
        UpdateResponse response = client.update(request, RequestOptions.DEFAULT);
        return response.toString();
    }

    /**
     * 查找用户数据
     *
     * @param index 索引名
     * @param id    用户id
     * @return 用户数据
     * @throws IOException io异常
     */
    @Override
    public User getUser(String index, String id) throws IOException {
        GetRequest request = new GetRequest(index, id);
        GetResponse response = client.get(request, RequestOptions.DEFAULT);
        String json = response.getSourceAsString();
        return JSON.parseObject(json, User.class);
    }

    /**
     * 根据关键字获取用户信息
     *
     * @param index   索引名
     * @param keyword 关键字
     * @return 用户信息
     * @throws IOException io异常
     */
    @Override
    public List<User> getUserList(String index, String keyword) throws IOException {
        List<User> userList = new ArrayList<>();
        SearchRequest request = new SearchRequest(index);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(QueryBuilders.matchQuery("keyword", keyword));
        sourceBuilder.from(0);
        sourceBuilder.size(10);
        request.source(sourceBuilder);
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        SearchHit[] hits = response.getHits().getHits();
        for (SearchHit hit : hits) {
            String json = hit.getSourceAsString();
            User user = JSON.parseObject(json, User.class);
            userList.add(user);
        }
        return userList;
    }

    /**
     * 将物品信息插入到elasticsearch中
     *
     * @param index 索引名
     * @param goods 物品对象
     * @throws IOException io异常
     */
    @Override
    public void insertGoods(String index, List<Good> goods) throws IOException {
        IndexRequest request = new IndexRequest(index);
        for (Good good : goods) {
            String json = JSON.toJSONString(good);
            request.source(json, XContentType.JSON);
            client.index(request, RequestOptions.DEFAULT);
        }
    }

    /**
     * 获取物品信息
     *
     * @param index   索引名
     * @param keyword 关键字
     * @return 物品信息
     * @throws IOException io异常
     */
    @Override
    public List<Good> getGoods(String index, String keyword) throws IOException {
        List<Good> goods = new ArrayList<>();
        SearchRequest request = new SearchRequest();
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(QueryBuilders.matchQuery("name", keyword));
        // 设置高亮
        HighlightBuilder highlighter = new HighlightBuilder();
        highlighter.field("name");
        highlighter.preTags("<span style='color:red'>");
        highlighter.postTags("</span>");
        highlighter.requireFieldMatch(false);
        sourceBuilder.highlighter(highlighter);

        request.source(sourceBuilder);
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        SearchHit[] hits = response.getHits().getHits();
        for (SearchHit hit : hits) {
            // 先转对象
            String json = hit.getSourceAsString();
            Good good = JSON.parseObject(json, Good.class);
            // 高亮设置
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            HighlightField field = highlightFields.get("name");
            if (null != field) {
                Text[] fragments = field.getFragments();
                StringBuilder name = new StringBuilder();
                for (Text text : fragments) {
                    name.append(text.toString());
                }
                good.setName(name.toString());
            }
            goods.add(good);
        }
        return goods;
    }


}
