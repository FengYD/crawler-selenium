package com.feng.crawlerselenium.base.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author fengyadong
 * @date 2023/6/19 9:54
 * @Description
 */
@TableName("sys_account")
@Data
@EqualsAndHashCode(callSuper = true)
public class SysAccount extends BaseDomain {

    private String platform;

    private String username;

    private String password;

    private Integer status;

}
