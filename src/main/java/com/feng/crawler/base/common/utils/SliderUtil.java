package com.feng.crawler.base.common.utils;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.RandomUtil;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * @author fengyadong
 * @date 2023/6/16 10:10
 * @Description 滑块工具类
 */
public class SliderUtil {

    /**
     * 滑块验证
     *
     * @param driver    driver
     * @param button    选中的滑块
     * @param xDistance 横向滑动的距离
     */
    public static void moveButton(RemoteWebDriver driver, WebElement button, Integer xDistance) {
        List<Integer> sliderDistanceList = simulateTrace(xDistance);

        Actions action = new Actions(driver)
                .clickAndHold(button);
        for (Integer sliderDistance : sliderDistanceList) {
            action.tick(action.getActivePointer().createPointerMove(Duration.ofMillis(20L), PointerInput.Origin.pointer(), sliderDistance, 0));
        }
        action.perform();
        ThreadUtil.sleep(2000);
        action.release();
    }

    /**
     * 模拟滑块轨迹
     *
     * @param distance 总距离
     * @return 每次移动的长度
     */
    public static List<Integer> simulateTrace(Integer distance) {
        List<Integer> resultList = new ArrayList<>();
        List<Double> doubleList = new ArrayList<>();
        double total = 100d;
        double slowDownDistance = RandomUtil.randomDouble(60, 65);
        double current = 0;
        double a = 0;
        double v = 0;
        while (current < total) {
            doubleList.add(current);
            if (current < slowDownDistance) {
                a += RandomUtil.randomDouble(0.4, 0.6);
            } else {
                a = 0 - RandomUtil.randomDouble(0.4, 0.6);
            }
            if (v > 10) {
                v = 10;
            }
            current = current + (v + 0.5 * a);
            v = v + a;
        }
        doubleList.add(current);

        for (int i = 0; i < doubleList.size() - 1; i++) {
            long d = Math.round((doubleList.get(i + 1) - doubleList.get(i)) / 100 * distance);
            resultList.add((int) d);
        }

        int extraTotal = resultList.stream().mapToInt(Integer::intValue).sum();
        resultList.set(resultList.size() - 1, extraTotal - distance);
        return resultList;
    }

}
