package com.feng.crawler.douban.controller;

import com.feng.crawler.base.common.enums.CustomExceptionEnum;
import com.feng.crawler.base.common.utils.WebDriverUtil;
import com.feng.crawler.base.domain.SysTask;
import com.feng.crawler.base.mapper.SysTaskMapper;
import com.feng.crawler.base.model.BaseResponse;
import com.feng.crawler.base.model.CustomException;
import com.feng.crawler.base.service.BaseLoginService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author fengyadong
 * @date 2023/6/19 10:57
 * @Description
 */
@Slf4j
@RestController
@RequestMapping("/douban")
public class DoubanController {

    @Resource
    private SysTaskMapper sysTaskMapper;
    @Autowired
    private BaseLoginService doubanLoginService;

    @RequestMapping("/login")
    public BaseResponse doubanLogin(String taskName) {
        SysTask sysTask = sysTaskMapper.selectOneByTaskName(taskName);
        if (ObjectUtils.isEmpty(sysTask)) {
            throw new CustomException(CustomExceptionEnum.NO_TASK);
        }
        List<WebDriver> webDriverList = new ArrayList<>(sysTask.getParallelNum());
        for (int i = 0; i < sysTask.getParallelNum(); i++) {
            webDriverList.add(WebDriverUtil.initChromeWebDriver());
        }
        doubanLoginService.login(webDriverList, sysTask);
        for (int i = 0; i < sysTask.getParallelNum(); i++) {
            new Thread(() -> System.out.println(Thread.currentThread().getName() + "启动")).start();
        }
        String result = String.format("任务：%s已启动，并行数：%d", taskName, sysTask.getParallelNum());
        return BaseResponse.success(result);
    }

}
