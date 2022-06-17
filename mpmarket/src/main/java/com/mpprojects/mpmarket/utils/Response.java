package com.mpprojects.mpmarket.utils;

import lombok.Data;

/**
 * 这个类是信息返回的统一格式类。将原本代码中返回String 的内容统一化
 */
@Data
public class Response<T> {

    private String code;
    private String msg;
    private T data;

    public Response(String code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public Response(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
