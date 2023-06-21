package com.feng.crawler.base.common.utils;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.PointerInput;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.Duration;
import java.util.*;

/**
 * @author fengyadong
 * @date 2023/6/16 10:10
 * @Description 滑块工具类
 */
@Slf4j
public class SliderUtil {

    public static final String SIMULATE1 = "simulateTrace1";  // 先加速后减速，直接移动到指定位置
    public static final String SIMULATE2 = "simulateTrace2";  // 先加速后减速，先超过指定位置再移动回来，不完全拼合，误差在距离的2%以内

    /**
     * 滑块验证
     *
     * @param driver    driver
     * @param button    选中的滑块
     * @param xDistance 横向滑动的距离
     */
    public static void moveButton(WebDriver driver, WebElement button, Integer xDistance) {
        moveButton(driver, button, xDistance, SIMULATE1);
    }

    /**
     * 滑块验证
     *
     * @param driver         driver
     * @param button         选中的滑块
     * @param xDistance      横向滑动的距离
     * @param simulateMethod 生成滑块轨迹方法
     */
    public static void moveButton(WebDriver driver, WebElement button, Integer xDistance, String simulateMethod) {
        List<Integer> sliderDistanceList = simulateTrace(simulateMethod, xDistance);

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
     */
    public static List<Integer> simulateTrace(String simulateMethod, Integer distance) {
        try {
            Class<SliderUtil> sliderUtilClass = SliderUtil.class;
            Method[] methods = sliderUtilClass.getDeclaredMethods();
            Optional<Method> method = Arrays.stream(methods).filter(e -> e.getName().equals(simulateMethod)).findFirst();
            if (method.isPresent()) {
                Object o = method.get().invoke(sliderUtilClass, distance);
                return (List<Integer>) o;
            } else {
                throw new NoSuchMethodException();
            }
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            log.error("调用异常", e);
        }
        return Collections.emptyList();
    }


    /**
     * 模拟滑块轨迹（先加速后减速，直接移动到指定位置）
     *
     * @param distance 总距离
     * @return 每次移动的长度
     */
    private static List<Integer> simulateTrace1(Integer distance) {
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

    /**
     * 模拟滑块轨迹（先加速后减速，先超过指定位置再移动回来，不完全拼合，误差在距离的2%以内）
     */
    private static List<Integer> simulateTrace2(Integer distance) {
        distance = (int) Math.floor(distance * RandomUtil.randomDouble(0.98, 1.02));
        List<Integer> resultList = new ArrayList<>();
        List<Double> doubleList = new ArrayList<>();
        double total = 110d;
        double slowDownDistance = RandomUtil.randomDouble(85, 90);
        double current = 0;
        double a = 0;
        double v = 0;
        while (current < total) {
            doubleList.add(current);
            if (current < slowDownDistance) {
                a += RandomUtil.randomDouble(0.2, 0.4);
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
        // 往回移动
        total = 100;
        a = -0.2;
        v = -2.0;
        while (current > total) {
            doubleList.add(current);
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
