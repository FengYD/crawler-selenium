package com.feng.crawlerselenium.base.common.enums;

import lombok.Getter;

/**
 * @author fengyadong
 * @date 2023/6/16 10:18
 * @Description
 */
@Getter
public enum ResponseEnum {

    SUCCESS(0, "成功"),
    FAIL(1, "失败");

    private final Integer code;

    private final String name;

    ResponseEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public ResponseEnum getByCode(Integer code) {
        for (ResponseEnum value : values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}
