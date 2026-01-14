package com.raizumi.component.common.result;

import java.io.Serializable;

public class Result <T> implements Serializable {
    private Integer status = Code.SUCCESS.status;
    private String msg = Code.SUCCESS.msg;
    private T data;

    public Result() {
        this.status = Code.SUCCESS.status;
        this.msg = Code.SUCCESS.msg;
    }

    public Result(Code code) {
        this.status = code.status;
        this.msg = code.msg;
    }

    public Result(T data) {
        this.status = Code.SUCCESS.status;
        this.msg = Code.SUCCESS.msg;
        this.data = data;
    }

    public Result(Code code, T data) {
        this.status = code.status;
        this.msg = code.msg;
        this.data = data;
    }

    public Result(Integer status, String msg, T data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public static Result<?> success(){
        return new Result<>();
    }

    public static Result<?> success(Object data){
        return new Result<>(data);
    }

    public static Result<?> failure(){
        return new Result<>(Code.INTERNAL_ERROR);
    }

    public static Result<?> failure(Object data){
        return new Result<>(Code.INTERNAL_ERROR, data);
    }




    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
