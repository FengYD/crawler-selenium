package com.feng.crawlerselenium.test.service;

import cn.hutool.core.thread.ThreadUtil;
import com.feng.crawlerselenium.base.common.utils.WebDriverUtil;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Service;

/**
 * @author fengyadong
 * @date 2023/6/25 9:22
 * @Description
 */
@Slf4j
@Service
public class TestTitleService {

    public void entrance() {
        WebDriver driver = WebDriverUtil.initChromeWebDriver();
        driver.get(" http://shop492973372.taobao.com/");
        while (true) {
            System.out.println(driver.getTitle());
            ThreadUtil.sleep(500);
        }
    }

}
