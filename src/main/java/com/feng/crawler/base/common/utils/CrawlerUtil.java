package com.feng.crawler.base.common.utils;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.RandomUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;

/**
 * @author fengyadong
 * @date 2023/6/19 10:51
 * @Description
 */
public class CrawlerUtil {

    public static boolean sleep(Integer rangeLow, Integer rangeHigh) {
        return ThreadUtil.sleep(RandomUtil.randomInt(rangeLow, rangeHigh));
    }

    /**
     * 检查元素是否存在
     */
    public static boolean existWebElement(WebDriver webDriver, By by) {
        try {
            webDriver.findElement(by);
        } catch (NoSuchElementException e) {
            return false;
        }
        return true;
    }

}
