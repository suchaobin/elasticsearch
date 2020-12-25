package com.scb.elasticsearch.util;

import com.scb.elasticsearch.entity.Good;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author suchaobin
 * @description 网页解析工具
 * @date 2020/12/25 15:47
 **/
@Component
@Slf4j
public class HtmlParseUtil {

    public List<Good> parseJD(String keyword) throws IOException {
        List<Good> goods = new ArrayList<>();
        //获取url请求
        String url = "https://search.jd.com/Search?keyword=" + keyword;
        //解析网页,Jsoup返回的是Document对象（浏览器Document对象）
        Document document = Jsoup.parse(new URL(url), 10000);
        //所有在js中使用的方法，这里都能使用
        Element element = document.getElementById("J_goodsList");
        Elements elements = element.getElementsByTag("li");
        for (Element el : elements) {
            String price = el.getElementsByClass("p-price").get(0).text();
            String name = el.getElementsByClass("p-name").get(0).text();
            String src = el.getElementsByTag("img").get(0).attr("data-lazy-img");
            goods.add(new Good(name, price, src));
        }
        return goods;
    }
}
