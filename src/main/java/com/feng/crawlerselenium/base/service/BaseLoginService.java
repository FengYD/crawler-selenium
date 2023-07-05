package com.feng.crawlerselenium.base.service;

import com.feng.crawlerselenium.base.common.enums.CustomExceptionEnum;
import com.feng.crawlerselenium.base.domain.SysAccount;
import com.feng.crawlerselenium.base.domain.SysPlatform;
import com.feng.crawlerselenium.base.domain.SysTask;
import com.feng.crawlerselenium.base.mapper.SysAccountMapper;
import com.feng.crawlerselenium.base.mapper.SysPlatformMapper;
import com.feng.crawlerselenium.base.model.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.LinkedList;
import java.util.List;

/**
 * @author fengyadong
 * @date 2023/6/19 9:53
 * @Description
 */
@Slf4j
@Service
public abstract class BaseLoginService {

    @Resource
    private SysPlatformMapper sysPlatformMapper;
    @Resource
    private SysAccountMapper sysAccountMapper;


    /**
     * 登录
     * @param webDriverList webDriverList
     * @param sysTask 任务
     */
    public void login(List<WebDriver> webDriverList, SysTask sysTask) {
        SysPlatform platform = sysPlatformMapper.selectOneByPlatform(sysTask.getPlatform());
        List<SysAccount> accountList = getAccountsByPlatForm(sysTask.getPlatform());
        if (CollectionUtils.isEmpty(accountList)) {
            throw new CustomException(CustomExceptionEnum.NO_ACCOUNT);
        } else if (accountList.size() < sysTask.getParallelNum()) {
            throw new CustomException(CustomExceptionEnum.LACK_ACCOUNT);
        }
        LinkedList<SysAccount> stack = new LinkedList<>(accountList);
        for (WebDriver webDriver : webDriverList) {
            SysAccount account = stack.pop();
            webDriver.get(platform.getLoginUrl());
            login0(webDriver, account);
        }
    }

    /**
     * 根据平台名称获取账户
     * @param platform 平台名称
     */
    protected List<SysAccount> getAccountsByPlatForm(String platform) {
        return sysAccountMapper.selectListByPlatform(platform);
    }

    protected abstract void login0(WebDriver webDriver, SysAccount account);

}
