package com.mpprojects.mpmarket.utils;

import lombok.Data;

/**
 * 这个类是信息返回的统一格式类。将原本代码中返回String 的内容统一化
 * 暂不知code 的编号规则，故暂列如下：
 * 200：成功
 * 1002：操作失败
 * 1003：对象已存在
 * 1004：对象不存在
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
