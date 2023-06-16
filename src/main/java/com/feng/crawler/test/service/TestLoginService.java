package com.feng.crawler.test.service;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.RandomUtil;
import com.feng.crawler.base.common.utils.WebDriverUtil;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Service;

/**
 * @author fengyadong
 * @date 2023/6/16 17:06
 * @Description
 */
@Slf4j
@Service
public class TestLoginService {

    public void entrance() {
        WebDriver driver = WebDriverUtil.initChromeWebDriver();
        login(driver);
        System.out.println(driver.getTitle());
    }


    public void login(WebDriver driver) {
        driver.get("https://login.taobao.com/");
        WebElement loginForm =  driver.findElement(By.id("login-form"));
        ThreadUtil.sleep(RandomUtil.randomInt(1000, 2000));
        loginForm.findElement(By.name("fm-login-id")).sendKeys("1432725886f");
        ThreadUtil.sleep(RandomUtil.randomInt(1000, 2000));
        loginForm.findElement(By.name("fm-login-password")).sendKeys("123456789f");
        ThreadUtil.sleep(RandomUtil.randomInt(300, 1000));
        loginForm.findElement(By.className("fm-button fm-submit password-login")).click();
        ThreadUtil.sleep(RandomUtil.randomInt(300, 1000));
    }

}
