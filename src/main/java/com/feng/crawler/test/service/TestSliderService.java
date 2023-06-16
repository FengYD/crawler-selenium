package com.feng.crawler.test.service;

import com.feng.crawler.base.common.utils.WebDriverUtil;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Service;

/**
 * @author fengyadong
 * @date 2023/6/16 10:41
 * @Description
 */
@Slf4j
@Service
public class TestSliderService {

    public void entrance() {
        WebDriver driver = WebDriverUtil.initChromeWebDriver();
        driver.get("https://shop108094280.taobao.com/");
        driver.quit();
    }

}
