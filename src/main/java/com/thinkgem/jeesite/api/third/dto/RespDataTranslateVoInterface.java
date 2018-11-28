package com.thinkgem.jeesite.api.third.dto;

import com.thinkgem.jeesite.api.third.exception.ThirdInterfaceException;

/**
 * 将第三方的返回数据转Vo接口
 *
 * @author kakasun
 * @create 2018-04-20 下午2:35
 */
public interface RespDataTranslateVoInterface<T> {

    /**
     * 创建vo
     * @return
     */
    T createVo(String body) throws ThirdInterfaceException ;
}
