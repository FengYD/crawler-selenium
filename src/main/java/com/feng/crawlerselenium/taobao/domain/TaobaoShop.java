package com.feng.crawlerselenium.taobao.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.feng.crawlerselenium.base.domain.BaseDomain;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author fengyadong
 * @date 2023/6/21 13:51
 * @Description
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("taobao_shop")
public class TaobaoShop extends BaseDomain {

    private String shopUrl;

    private Integer shopState;

    private String shopName;

    private Long shopId;

    private String startDate;

    private Long externId;

}
