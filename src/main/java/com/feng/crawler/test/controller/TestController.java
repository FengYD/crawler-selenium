package com.feng.crawler.test.controller;

import com.feng.crawler.base.model.BaseResponse;
import com.feng.crawler.test.service.TestSliderService;
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
@RequestMapping("/base/test")
public class TestController {

    @Autowired
    private TestSliderService testSliderService;

    @RequestMapping("/slider")
    public BaseResponse testSlider() {
        testSliderService.entrance();
        return BaseResponse.success();
    }

}