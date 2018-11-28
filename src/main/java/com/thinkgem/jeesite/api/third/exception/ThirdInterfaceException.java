package com.thinkgem.jeesite.api.third.exception;

/**
 * 第三方接口异常
 *
 * @author kakasun
 * @create 2018-04-20 下午3:53
 */
public class ThirdInterfaceException extends Exception {

    public ThirdInterfaceException() {
    }

    public ThirdInterfaceException(String message) {
        super(message);
    }

    public ThirdInterfaceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ThirdInterfaceException(Throwable cause) {
        super(cause);
    }
}
