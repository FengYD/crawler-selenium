package com.feng.crawler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.net.URL;

/**
 * @author fengyadong
 * @date 2023/6/19 15:10
 * @Description
 */
@Slf4j
@Component
public class ApplicationListenerImpl implements ApplicationListener<ApplicationStartedEvent> {

    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        log.info("开始加载OpenCV库");
        URL url = ClassLoader.getSystemResource("lib/opencv/opencv_java430.dll");
        System.load(url.getPath());
        log.info("OpenCV库加载完成");
    }

}