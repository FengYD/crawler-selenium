package com.feng.crawler.base.common.utils;

import com.google.common.collect.Lists;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
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

    /**
     * 初始化chromeWebDriver
     */
    public static WebDriver initChromeWebDriver() {
        // 配置参数
        ChromeOptions options = configCommonChromeOptions();
        // 设置日志输出到控制台
        ChromeDriverService service = new ChromeDriverService.Builder()
                .withLogOutput(System.out)
                .build();
        ChromeDriver driver = new ChromeDriver(service, options);
        //修改window.navigator.webdriver=undefined，防机器人识别机制
        Map<String, Object> command = new HashMap<>();
        command.put("source", "Object.defineProperty(navigator, 'webdriver', {get: () => undefined})");
        driver.executeCdpCommand("Page.addScriptToEvaluateOnNewDocument", command);
        return driver;
    }

    /**
     * 配置通用chrome参数
     */
    private static ChromeOptions configCommonChromeOptions() {
        ChromeOptions options = new ChromeOptions();
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
        return options;
    }


}
