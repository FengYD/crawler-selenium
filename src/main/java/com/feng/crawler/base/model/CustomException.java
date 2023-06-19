package com.feng.crawler.base.model;

import com.feng.crawler.base.common.enums.CustomExceptionEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author fengyadong
 * @date 2023/6/19 10:19
 * @Description
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomException extends RuntimeException {

    private Integer code;

    private String message;

    private CustomExceptionEnum customExceptionEnum;

    public CustomException(CustomExceptionEnum customExceptionEnum) {
        this.customExceptionEnum = customExceptionEnum;
        this.code = customExceptionEnum.getCode();
        this.message = customExceptionEnum.getMessage();
    }

}
