package com.feng.crawlerselenium.taobao.controller;

import cn.hutool.core.thread.ThreadUtil;
import com.feng.crawlerselenium.base.common.enums.CustomExceptionEnum;
import com.feng.crawlerselenium.base.common.utils.WebDriverUtil;
import com.feng.crawlerselenium.base.domain.SysTask;
import com.feng.crawlerselenium.base.mapper.SysTaskMapper;
import com.feng.crawlerselenium.base.model.BaseResponse;
import com.feng.crawlerselenium.base.model.CustomException;
import com.feng.crawlerselenium.base.service.BaseLoginService;
import com.feng.crawlerselenium.taobao.service.TaobaoShopService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * @author fengyadong
 * @date 2023/6/21 11:08
 * @Description
 */
@Slf4j
@RestController
@RequestMapping("/taobao")
public class TaobaoController {

    @Resource
    private SysTaskMapper sysTaskMapper;
    @Autowired
    private BaseLoginService taobaoLoginService;
    @Autowired
    private TaobaoShopService taobaoShopService;

    @RequestMapping("/login")
    public BaseResponse taobaoLogin(String taskName) {
        SysTask sysTask = sysTaskMapper.selectOneByTaskName(taskName);
        if (ObjectUtils.isEmpty(sysTask)) {
            throw new CustomException(CustomExceptionEnum.NO_TASK);
        }
        List<WebDriver> webDriverList = new ArrayList<>(sysTask.getParallelNum());
        for (int i = 0; i < sysTask.getParallelNum(); i++) {
            webDriverList.add(WebDriverUtil.initChromeWebDriver());
        }
        taobaoLoginService.login(webDriverList, sysTask);
        for (int i = 0; i < sysTask.getParallelNum(); i++) {
            new Thread(() -> System.out.println(Thread.currentThread().getName() + "启动")).start();
        }
        String result = String.format("任务：%s已启动，并行数：%d", taskName, sysTask.getParallelNum());
        return BaseResponse.success(result);
    }


    @RequestMapping("/shop")
    public BaseResponse taobaoShop(String taskName) {
        SysTask sysTask = sysTaskMapper.selectOneByTaskName(taskName);
        if (ObjectUtils.isEmpty(sysTask)) {
            throw new CustomException(CustomExceptionEnum.NO_TASK);
        }
        List<WebDriver> webDriverList = new ArrayList<>(sysTask.getParallelNum());
        log.info("初始化浏览器");
        for (int i = 0; i < sysTask.getParallelNum(); i++) {
            webDriverList.add(WebDriverUtil.initChromeWebDriver());
        }
        // 隐式等待时间
        for (WebDriver webDriver : webDriverList) {
            webDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        }
        log.info("开始登录");
        taobaoLoginService.login(webDriverList, sysTask);
        for (int i = 0; i < sysTask.getParallelNum(); i++) {
            int index = i;
            new Thread(
                    () -> taobaoShopService.crawlerDispatch(webDriverList.get(index), webDriverList.size(), index),
                    "taobaoShop-" + index
            ).start();
            ThreadUtil.sleep(2000);
        }
        String result = String.format("任务：%s已启动，并行数：%d", taskName, sysTask.getParallelNum());
        return BaseResponse.success(result);
    }

}
