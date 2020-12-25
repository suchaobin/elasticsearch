package com.scb.elasticsearch.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author suchaobin
 * @description 物品
 * @date 2020/12/25 16:36
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Good {
    private String name;
    // 这边的价格用String是因为爬下来的价格带￥符号，不想做特殊处理
    private String price;
    private String src;
}
