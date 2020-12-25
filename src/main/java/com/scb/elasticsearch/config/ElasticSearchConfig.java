package com.scb.elasticsearch.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.Node;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

/**
 * @author suchaobin
 * @description elasticsearch配置类
 * @date 2020/12/25 09:58
 **/
@Configuration
public class ElasticSearchConfig {

    @Bean(name = "nodes")
    public List<Node> getNodeList() {
        HttpHost httpHost = new HttpHost("localhost", 9200, "http");
        return Arrays.asList(new Node(httpHost));
    }

    @Bean(name = "restHighLevelClient")
    public RestHighLevelClient getClient(@Qualifier("nodes") List<Node> nodes) {
        Node[] nodeArr = nodes.toArray(new Node[1]);
        return new RestHighLevelClient(RestClient.builder(nodeArr));
    }
}
