package com.scb.elasticsearch.controller;

import com.scb.elasticsearch.entity.Good;
import com.scb.elasticsearch.entity.User;
import com.scb.elasticsearch.service.ElasticSearchService;
import com.scb.elasticsearch.util.HtmlParseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

/**
 * @author suchaobin
 * @description controller
 * @date 2020/12/25 10:42
 **/
@RestController
public class ElasticSearchController {
    @Autowired
    private ElasticSearchService elasticSearchService;
    @Autowired
    private HtmlParseUtil htmlParseUtil;

    @GetMapping("/creat/index/{name}")
    public Boolean creatIndex(@PathVariable("name") String name) throws IOException {
        return elasticSearchService.creatIndex(name);
    }

    @GetMapping("/delete/index/{name}")
    public Boolean delIndex(@PathVariable("name") String name) throws IOException {
        return elasticSearchService.delIndex(name);
    }

    @GetMapping("/exists/index/{name}")
    public Boolean existsIndex(@PathVariable("name") String name) throws IOException {
        return elasticSearchService.existsIndex(name);
    }

    @GetMapping("/addUser/{index}/{name}/{age}")
    public String addUser(@PathVariable("index") String index,
                          @PathVariable("name") String name,
                          @PathVariable("age") Integer age) throws IOException {
        return elasticSearchService.addUser(index, new User(name, age));
    }

    @GetMapping("/delUser/{index}/{id}")
    public String addUser(@PathVariable("index") String index,
                          @PathVariable("id") String id) throws IOException {
        return elasticSearchService.delUser(index, id);
    }

    @GetMapping("/updateUser/{index}/{id}")
    public String updateUser(@PathVariable("index") String index,
                             @PathVariable("id") String id, User user) throws IOException {
        return elasticSearchService.updateUser(index, id, user);
    }

    @GetMapping("/getUser/{index}/{id}")
    public User getUser(@PathVariable("index") String index,
                        @PathVariable("id") String id) throws IOException {
        return elasticSearchService.getUser(index, id);
    }

    @GetMapping("/getUserList/{index}/{keyword}")
    public List<User> getUserList(@PathVariable("index") String index,
                                  @PathVariable("keyword") String keyword) throws IOException {
        return elasticSearchService.getUserList(index, keyword);
    }

    @GetMapping("/insertGoodsFromJD/{index}/{keyword}")
    public List<Good> insertGoodsFromJD(@PathVariable("index") String index,
                                        @PathVariable("keyword") String keyword) throws IOException {
        List<Good> goods = htmlParseUtil.parseJD(keyword);
        elasticSearchService.insertGoods(index, goods);
        return goods;
    }

    @GetMapping("/getGoods/{index}/{keyword}")
    public List<Good> getGoods(@PathVariable("index") String index,
                               @PathVariable("keyword") String keyword) throws IOException {
        return elasticSearchService.getGoods(index, keyword);
    }
}
