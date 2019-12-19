package com.example.transactiondynamic.myexception;

/**
 * @Author: XueWeiDong
 * @Description:
 * @Date: 14:38 2019/12/18
 */
public class ServiceException extends RuntimeException {
    public ServiceException(){
        super();
    }

    public ServiceException(String msg){
        super(msg);
    }

    public ServiceException(Throwable throwable){
        super(throwable);
    }

    public ServiceException(String msg, Throwable throwable){
        super(msg, throwable);
    }
}
