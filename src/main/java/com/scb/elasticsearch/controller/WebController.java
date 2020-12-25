package com.scb.elasticsearch.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author suchaobin
 * @description controller
 * @date 2020/12/25 15:29
 **/
@Controller
@RequestMapping("/view")
public class WebController {

    @RequestMapping("/index")
    public String index() {
        return "index";
    }

}
