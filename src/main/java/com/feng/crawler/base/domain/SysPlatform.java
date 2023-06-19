package com.feng.crawler.base.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author fengyadong
 * @date 2023/6/19 10:03
 * @Description
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_platform")
public class SysPlatform extends BaseDomain {

    private String  platform;

    private String loginUrl;

}
