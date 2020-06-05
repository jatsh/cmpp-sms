package com.netease.jv.cmpp.utils;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class GsResult {

    public static final String STATUS_SUCCESS = "success";
    public static final String STATUS_FAIL = "fail";
    public static final String STATUS_ERROR = "error";

    private String status;
    private String msg;
    private Object data;

    public static GsResultBuilder newSuccess() {
        return new GsResultBuilder().status(STATUS_SUCCESS).msg(STATUS_SUCCESS);
    }

    public static GsResultBuilder newSuccess(String msg) {
        return new GsResultBuilder().status(STATUS_SUCCESS).msg(msg);
    }

    public static GsResultBuilder newFail() {
        return new GsResultBuilder().status(STATUS_FAIL).msg(STATUS_FAIL);
    }

    public static GsResultBuilder newFail(String msg) {
        return new GsResultBuilder().status(STATUS_FAIL).msg(msg);
    }

    public static GsResultBuilder newError() {
        return new GsResultBuilder().status(STATUS_ERROR).msg(STATUS_ERROR);
    }

    public static GsResultBuilder newError(String msg) {
        return new GsResultBuilder().status(STATUS_ERROR).msg(msg);
    }

}
