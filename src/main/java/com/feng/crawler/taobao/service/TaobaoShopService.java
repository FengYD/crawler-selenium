package com.feng.crawler.taobao.service;

import com.feng.crawler.base.common.enums.ShopStateEnum;
import com.feng.crawler.base.common.utils.CrawlerUtil;
import com.feng.crawler.base.common.utils.SliderUtil;
import com.feng.crawler.taobao.domain.TaobaoShop;
import com.feng.crawler.taobao.mapper.TaobaoShopMapper;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.List;

/**
 * @author fengyadong
 * @date 2023/6/21 11:51
 * @Description
 */
@Slf4j
@Service
public class TaobaoShopService {

    @Resource
    private TaobaoShopMapper taobaoShopMapper;

    public void crawlerDispatch(WebDriver webDriver, Integer modNum, Integer num) {
        log.info("开始获取数据 modNum = {} num = {}", modNum, num);
        long minId = 0;
        List<TaobaoShop> shopList = selectNextGroup(minId, modNum, num);
        while (!CollectionUtils.isEmpty(shopList)) {
            for (TaobaoShop shop : shopList) {
                if (ObjectUtils.isEmpty(shop.getShopState())) {
                    crawlerTaobaoShop(webDriver, shop);
                }
            }
            minId = shopList.get(shopList.size() - 1).getId();
            shopList = selectNextGroup(minId, modNum, num);
            log.info("执行到id = {}", minId);
        }
    }

    private List<TaobaoShop> selectNextGroup(long minId, int modNum, int num) {
        if (modNum == 1) {
            return taobaoShopMapper.selectScopeOverId(minId, 1000);
        } else {
            return taobaoShopMapper.selectScopeOverIdAndModId(minId, 1000, modNum, num);
        }
    }

    public void crawlerTaobaoShop(WebDriver webDriver, TaobaoShop taobaoShop) {
        if (!taobaoShop.getShopUrl().contains("taobao.com")) {
            taobaoShop.setShopState(ShopStateEnum.NotPlat.getCode());
            taobaoShopMapper.updateById(taobaoShop);
        }
        webDriver.get(taobaoShop.getShopUrl());
        CrawlerUtil.sleep(1000, 2000);
        // 滑块验证
        while (existNoCaptcha(webDriver)) {
            handleNoCaptcha(webDriver);
            CrawlerUtil.sleep(1000, 2000);
        }
        String title = webDriver.getTitle();
        if (title.equals("店铺浏览-淘宝网") || title.equals("爱逛街")) {
            taobaoShop.setShopState(ShopStateEnum.NotFound.getCode());
            taobaoShopMapper.updateById(taobaoShop);
        }
        taobaoShop.setShopState(ShopStateEnum.Normal.getCode());
        // 从网页title中提取店铺名称
        if (!ObjectUtils.isEmpty(title)) {
            taobaoShop.setShopName(title.substring(3, title.length() - 4));
        } else {
            log.error("未找到店铺名称, id = {} {}", taobaoShop.getId(), title);
        }
        // 执行脚本获取shopId
        try {
            String storeIdScript = "return shop_config.shopId ";
            Object storeId = ((JavascriptExecutor) webDriver).executeScript(storeIdScript);
            if (!ObjectUtils.isEmpty(storeId)) {
                taobaoShop.setShopId(Long.parseLong(storeId.toString()));
            }
        } catch (JavascriptException e) {
            log.error("未获取到shopId, id = {}", taobaoShop.getId());
        }

        // 从元素中获取开店时间
        if (CrawlerUtil.existWebElement(webDriver, By.className("shop-summary"))) {
            CrawlerUtil.mouseHover(webDriver, webDriver.findElement(By.id("J_TEnterShop")), Duration.ofSeconds(2));
            WebElement startDateElement = webDriver.findElement(By.className("J_id_time"));
            if (!ObjectUtils.isEmpty(startDateElement)) {
                taobaoShop.setStartDate(startDateElement.getText());
            }
        }
        // 使用js脚本获取开店时间
//        if (ObjectUtils.isEmpty(taobaoShop.getShopId())) {
//            String resJson = HttpUtil.get("https://shop.taobao.com/getShopInfo.htm?shopId=" + taobaoShop.getShopId()
//                    + "&_ksTS=" + System.currentTimeMillis() +"_22 " + "&callback=jsonp23");
//            JSONObject json = JSONObject.parseObject(resJson, JSONObject.class);
//            Optional<String> startDate = Optional.ofNullable(json).map(e -> e.getJSONObject("data")).map(e -> e.getString("starts"));
//            startDate.ifPresent(taobaoShop::setStartDate);
//        }
        taobaoShopMapper.updateById(taobaoShop);
    }


    private boolean existNoCaptcha(WebDriver webDriver) {
        return CrawlerUtil.existWebElement(webDriver, By.id("nocaptcha"));
    }

    private void handleNoCaptcha(WebDriver webDriver) {
        log.info("处理验证滑块");
        if (webDriver.getPageSource().contains("重试")) {
            webDriver.findElement(By.id("`nc_1_refresh1`")).click();
            CrawlerUtil.sleep(200, 500);
        }
        SliderUtil.moveButton(webDriver, webDriver.findElement(By.id("nc_1_n1z")), 258);
        log.info("验证滑块处理完成");
    }


}

