package com.feng.crawler.taobao.service;

import cn.hutool.core.util.StrUtil;
import com.feng.crawler.base.common.enums.CustomExceptionEnum;
import com.feng.crawler.base.common.utils.CrawlerUtil;
import com.feng.crawler.base.common.utils.SliderUtil;
import com.feng.crawler.base.domain.SysAccount;
import com.feng.crawler.base.domain.SysPlatform;
import com.feng.crawler.base.domain.SysTask;
import com.feng.crawler.base.mapper.SysPlatformMapper;
import com.feng.crawler.base.model.CustomException;
import com.feng.crawler.base.service.BaseLoginService;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.LinkedList;
import java.util.List;

/**
 * @author fengyadong
 * @date 2023/6/21 11:11
 * @Description
 */
@Slf4j
@Service
public class TaobaoLoginService extends BaseLoginService {

    @Resource
    private SysPlatformMapper sysPlatformMapper;

    /**
     * 登录
     *
     * @param webDriverList webDriverList
     * @param sysTask       任务
     */
    @Override
    public void login(List<WebDriver> webDriverList, SysTask sysTask) {
        SysPlatform platform = sysPlatformMapper.selectOneByPlatform(sysTask.getPlatform());
        List<SysAccount> accountList = getAccountsByPlatForm(sysTask.getPlatform());
        if (CollectionUtils.isEmpty(accountList)) {
            throw new CustomException(CustomExceptionEnum.NO_ACCOUNT);
        }
        while (accountList.size() < sysTask.getParallelNum()) {
            SysAccount copyAccount = new SysAccount();
            BeanUtils.copyProperties(accountList.get(0), copyAccount);
            accountList.add(copyAccount);
        }
        LinkedList<SysAccount> stack = new LinkedList<>(accountList);
        for (WebDriver webDriver : webDriverList) {
            SysAccount account = stack.pop();
            webDriver.get(platform.getLoginUrl());
            login0(webDriver, account);
        }
    }


    @Override
    protected void login0(WebDriver webDriver, SysAccount account) {
        WebElement loginForm = new WebDriverWait(webDriver, Duration.ofSeconds(10))
                .until(driver -> driver.findElement(By.className("login-form")));
        String[] username = StrUtil.split(account.getUsername(), 3);
        loginForm.findElement(By.id("fm-login-id")).sendKeys(username);
        CrawlerUtil.sleep(1000, 2000);
        String[] password = StrUtil.split(account.getPassword(), 3);
        loginForm.findElement(By.id("fm-login-password")).sendKeys(password);
        CrawlerUtil.sleep(1000, 2000);
        loginForm.findElement(By.className("fm-btn")).click();
        while (!loginSuccess(webDriver)) {
            while (CrawlerUtil.existWebElement(webDriver, By.id("baxia-dialog-content"))) {
                webDriver.switchTo().frame("baxia-dialog-content");
                handleSlider(webDriver);
                webDriver.switchTo().defaultContent();
                CrawlerUtil.sleep(1000, 2000);
                if (CrawlerUtil.existWebElement(webDriver, By.className("fm-btn")) &&
                        webDriver.findElement(By.className("fm-btn")).isEnabled()) {
                    webDriver.findElement(By.className("fm-btn")).click();
                }
                CrawlerUtil.sleep(1000, 2000);
            }
            CrawlerUtil.sleep(1000, 2000);
        }
        log.info("登录成功");
    }

    /**
     * 处理登录的滑块验证码
     */
    private void handleSlider(WebDriver webDriver) {
        log.info("处理登录滑块");
        SliderUtil.moveButton(webDriver, webDriver.findElement(By.id("nc_1_n1z")), 258);
        log.info("登录滑块处理完成");
    }

    private boolean loginSuccess(WebDriver webDriver) {
        return webDriver.getCurrentUrl().contains("i.taobao.com");
    }


}
