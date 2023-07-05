package com.feng.crawlerselenium.base.common.utils;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.RandomUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import java.time.Duration;

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

    /**
     * 鼠标悬浮在元素上一段时间
     */
    public static void mouseHover(WebDriver webDriver, WebElement webElement, Duration time) {
        Actions action = new Actions(webDriver).moveToElement(webElement).pause(time);
        action.perform();
        action.release();
    }

}
