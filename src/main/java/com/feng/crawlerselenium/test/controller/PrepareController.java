package com.feng.crawlerselenium.test.controller;

import com.feng.crawlerselenium.base.model.BaseResponse;
import com.feng.crawlerselenium.taobao.domain.TaobaoShop;
import com.feng.crawlerselenium.taobao.mapper.TaobaoShopMapper;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author fengyadong
 * @date 2023/6/21 13:55
 * @Description
 */
@Slf4j
@RestController
@RequestMapping("/prepare")
public class PrepareController {

    @Resource
    private TaobaoShopMapper taobaoShopMapper;

    @RequestMapping("/taobao/site")
    public BaseResponse loadTaobaoUrl() throws IOException {
        log.info("开始加载url文件");
        List<String> urlList = Files.readAllLines(Paths.get("D:\\taobao.txt"));
        log.info("开始转换url");
        List<TaobaoShop> shopList = urlList.stream()
                .filter(s1 -> s1.startsWith("|"))
                .skip(1)
                .map(s2 -> {
                    String[] arrs = s2.split("\\|");
                    TaobaoShop taobaoShop = new TaobaoShop();
                    taobaoShop.setShopUrl(arrs[2]);
                    taobaoShop.setExternId(Long.parseLong(arrs[1].trim()));
                    return taobaoShop;
                })
                .collect(Collectors.toList());
        log.info("url写入数据库");
        List<List<TaobaoShop>> sliceList = Lists.partition(shopList, 5000);
        for (List<TaobaoShop> slice : sliceList) {
            taobaoShopMapper.insertBatch(slice);
        }
        return BaseResponse.success();
    }

}
