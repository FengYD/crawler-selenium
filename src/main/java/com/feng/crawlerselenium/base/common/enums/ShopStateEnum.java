package com.feng.crawlerselenium.base.common.enums;

import lombok.Getter;

/**
 * @author fengyadong
 * @date 2023/6/21 15:19
 * @Description
 */
@Getter
public enum ShopStateEnum {

    Normal(1, "正常"),
    NotPlat(2, "不是这个平台"),
    NotFound(4, "未找到");

    private final Integer code;

    private final String name;

    ShopStateEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public ShopStateEnum getByCode(Integer code) {
        for (ShopStateEnum value : values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }

}
