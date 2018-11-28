package com.thinkgem.jeesite.common.web;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.thinkgem.jeesite.common.utils.ResponseCode;

import java.io.Serializable;

/**
 * api响应接口
 * 不序列化空字段
 * @author kakasun
 * @create 2018-03-11 上午8:26
 */
@JsonSerialize
@JsonInclude(Include.ALWAYS)
public class ServerResponse<T> implements Serializable {

    private static final long serialVersionUID = 3520341813581979101L;
    private int status;
    private String msg;
    private T body;

    private ServerResponse(int status){
        this.status = status;
    }
    private ServerResponse(int status,T body){
        this.status = status;
        this.body = body;
    }

    private ServerResponse(int status,String msg,T body){
        this.status = status;
        this.msg = msg;
        this.body = body;
    }

    private ServerResponse(int status,String msg){
        this.status = status;
        this.msg = msg;
    }

    //使之不在json序列化结果当中
    @JsonIgnore
    public boolean isSuccess(){
        return this.status == ResponseCode.SUCCESS.getCode();
    }

    public int getStatus(){
        return status;
    }
    public T getBody(){
        return body;
    }
    public String getMsg(){
        return msg;
    }


    public static <T> ServerResponse<T> createBySuccess(){
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode());
    }

    public static <T> ServerResponse<T> createBySuccessMessage(String msg){
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(),msg);
    }

    public static <T> ServerResponse<T> createBySuccess(T body){
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(),body);
    }

    public static <T> ServerResponse<T> createBySuccess(String msg,T body){
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(),msg,body);
    }


    public static <T> ServerResponse<T> createByError(){
        return new ServerResponse<T>(ResponseCode.ERROR.getCode(),ResponseCode.ERROR.getDesc());
    }


    public static <T> ServerResponse<T> createByErrorMessage(String errorMessage){
        return new ServerResponse<T>(ResponseCode.ERROR.getCode(),errorMessage);
    }

    public static <T> ServerResponse<T> createByErrorCodeMessage(int errorCode,String errorMessage){
        return new ServerResponse<T>(errorCode,errorMessage);
    }
    
    public static <T> ServerResponse<T> createByErrorCodeMessage(ResponseCode responseCode){
        return new ServerResponse<T>(responseCode.getCode(),responseCode.getDesc());
    }

    public ServerResponse msg(String msg){
        this.msg = msg;
        return this;
    }

    @Override
    public String toString() {
        return "ServerResponse{" +
                "status=" + status +
                ", msg='" + msg + '\'' +
                ", body=" + body +
                '}';
    }
}
