package com.feng.crawler.base.model;

import com.feng.crawler.base.common.enums.ResponseEnum;
import lombok.Data;

/**
 * @author fengyadong
 * @date 2023/6/16 10:16
 * @Description
 */
@Data
public class BaseResponse {

    private Integer code;

    private String msg;

    private Object data;

    private String requestId;

    public BaseResponse() {
    }

    public BaseResponse(ResponseEnum responseEnum) {
        this.code = responseEnum.getCode();
        this.msg = responseEnum.getName();
    }

    public static BaseResponse success(Object data) {
        BaseResponse baseResponse = BaseResponse.success();
        baseResponse.setData(data);
        return baseResponse;
    }

    public static BaseResponse success() {
        return new BaseResponse(ResponseEnum.SUCCESS);
    }

    public static BaseResponse fail() {
        return new BaseResponse(ResponseEnum.FAIL);
    }

}
