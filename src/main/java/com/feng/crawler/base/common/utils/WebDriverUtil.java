package com.feng.crawler.base.common.utils;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author fengyadong
 * @date 2023/6/16 11:01
 * @Description
 */
public class WebDriverUtil {

    public static WebDriver initChromeWebDriver() {
        ChromeOptions options = new ChromeOptions();
        configCommonChromeOptions(options);
        ChromeDriver driver = new ChromeDriver(options);
        //修改window.navigator.webdirver=undefined，防机器人识别机制
        Map<String, Object> command = new HashMap<>();
        command.put("source", "Object.defineProperty(navigator, 'webdriver', {get: () => undefined})");
        driver.executeCdpCommand("Page.addScriptToEvaluateOnNewDocument", command);
        return driver;
    }

    private static void configCommonChromeOptions(ChromeOptions options) {
        // 关闭界面上的---Chrome正在受到自动软件的控制
        options.addArguments("disable-infobars");
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--disable-blink-features=AutomationControlled");
        // 允许重定向
        options.addArguments("--disable-web-security");
        //options.addArguments("--start-maximized");
        options.addArguments("--no-sandbox");
        //options.addArguments("--whitelisted-ips=''");
        //设置ExperimentalOption
        List<String> excludeSwitches = Lists.newArrayList("enable-automation");
        options.setExperimentalOption("excludeSwitches", excludeSwitches);
        options.setExperimentalOption("useAutomationExtension", false);
    }


}
