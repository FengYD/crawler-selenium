package com.feng.crawlerselenium.test.controller;

import com.feng.crawlerselenium.base.model.BaseResponse;
import com.feng.crawlerselenium.test.service.TestLoginService;
import com.feng.crawlerselenium.test.service.TestSliderService;
import com.feng.crawlerselenium.test.service.TestTitleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author fengyadong
 * @date 2023/6/16 10:14
 * @Description
 */
@Slf4j
@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private TestSliderService testSliderService;
    @Autowired
    private TestLoginService testLoginService;
    @Autowired
    private TestTitleService testTitleService;

    @RequestMapping("/slider")
    public BaseResponse testSlider() {
        new Thread(() -> testSliderService.entrance()).start();
        return BaseResponse.success();
    }

    @RequestMapping("/login")
    public BaseResponse testLogin() {
        new Thread(() -> testLoginService.entrance()).start();
        return BaseResponse.success();
    }

    @RequestMapping("/title")
    public BaseResponse testTitle() {
        new Thread(() -> testTitleService.entrance()).start();
        return BaseResponse.success();
    }
}