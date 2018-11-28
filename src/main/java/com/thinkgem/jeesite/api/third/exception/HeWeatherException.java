package com.thinkgem.jeesite.api.third.exception;

/**
 * 和风天气接口异常
 *
 * @author kakasun
 * @create 2018-04-20 下午3:55
 */
public class HeWeatherException extends ThirdInterfaceException {

    public HeWeatherException() {
    }

    public HeWeatherException(String message) {
        super(message);
    }

    public HeWeatherException(String message, Throwable cause) {
        super(message, cause);
    }

    public HeWeatherException(Throwable cause) {
        super(cause);
    }
}
