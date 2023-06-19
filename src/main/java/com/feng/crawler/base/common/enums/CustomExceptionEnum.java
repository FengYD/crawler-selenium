package com.feng.crawler.base.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * @author fengyadong
 * @date 2023/6/19 10:19
 * @Description
 */
@Getter
@AllArgsConstructor
public enum CustomExceptionEnum {

    ERROR(1, "ERROR"),
    NO_ACCOUNT(10, "未配置账户"),
    LACK_ACCOUNT(11, "账户数量不足"),
    NO_TASK(21, "未查询到任务"),
    ERROR_CONTENT_TYPE(31, "返回数据类型不对"),
    ;

    private final Integer code;

    private final String message;

    public CustomExceptionEnum parseByCode(Integer code) {
        return Arrays.stream(CustomExceptionEnum.values())
                .filter(e -> e.getCode().equals(code))
                .findAny()
                .orElse(null);
    }
}
