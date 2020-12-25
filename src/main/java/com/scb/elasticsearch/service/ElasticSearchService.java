package com.scb.elasticsearch.service;

import com.scb.elasticsearch.entity.Good;
import com.scb.elasticsearch.entity.User;

import java.io.IOException;
import java.util.List;

/**
 * @author suchaobin
 * @description service
 * @date 2020/12/25 10:31
 **/
public interface ElasticSearchService {
    /**
     * 创建索引
     *
     * @param index 索引名
     * @return 是否成功
     * @throws IOException io异常
     */
    Boolean creatIndex(String index) throws IOException;

    /**
     * 删除索引
     *
     * @param index 索引名
     * @return 是否成功
     * @throws IOException io异常
     */
    Boolean delIndex(String index) throws IOException;

    /**
     * 是否存在索引
     *
     * @param index 索引名
     * @return 是否存在
     * @throws IOException io异常
     */
    Boolean existsIndex(String index) throws IOException;

    /**
     * 添加用户数据到elasticsearch中
     *
     * @param index 索引名
     * @param user  用户对象
     * @return 添加返回
     * @throws IOException io异常
     */
    String addUser(String index, User user) throws IOException;

    /**
     * 删除用户数据
     *
     * @param index 索引名
     * @param id    用户id
     * @return 删除返回
     * @throws IOException io异常
     */
    String delUser(String index, String id) throws IOException;

    /**
     * 修改用户数据
     *
     * @param index 索引名
     * @param id    用户id
     * @param user  用户对象
     * @return 修改返回
     * @throws IOException io异常
     */
    String updateUser(String index, String id, User user) throws IOException;

    /**
     * 查找用户数据
     *
     * @param index 索引名
     * @param id    用户id
     * @return 用户数据
     * @throws IOException io异常
     */
    User getUser(String index, String id) throws IOException;

    /**
     * 根据关键字获取用户信息
     *
     * @param index   索引名
     * @param keyword 关键字
     * @return 用户信息
     * @throws IOException io异常
     */
    List<User> getUserList(String index, String keyword) throws IOException;

    /**
     * 将物品信息插入到elasticsearch中
     *
     * @param index 索引名
     * @param goods 物品对象
     * @throws IOException io异常
     */
    void insertGoods(String index, List<Good> goods) throws IOException;

    /**
     * 获取物品信息
     *
     * @param index   索引名
     * @param keyword 关键字
     * @return 物品信息
     * @throws IOException io异常
     */
    List<Good> getGoods(String index, String keyword) throws IOException;
}
