package com.feng.crawler.base.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author fengyadong
 * @date 2023/6/19 10:09
 * @Description
 */
@TableName("sys_task")
@Data
@EqualsAndHashCode(callSuper = true)
public class SysTask extends BaseDomain {

    private String platform;

    private String taskName;

    private String entranceUrl;

    private String email;

    private Integer parallelNum;

}
