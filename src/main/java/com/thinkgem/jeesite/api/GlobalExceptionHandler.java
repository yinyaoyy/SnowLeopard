package com.thinkgem.jeesite.api;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.support.spring.FastJsonJsonView;
import com.thinkgem.jeesite.common.utils.BusinessException;
import com.thinkgem.jeesite.common.utils.ResponseCode;
import com.thinkgem.jeesite.common.utils.StringUtils;
/**
 * 全局异常捕获
 * @author kakasun
 * @create 2018-04-17 上午11:41
 */
public class GlobalExceptionHandler implements HandlerExceptionResolver {

    private static Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @Override
    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {
        log.debug("捕获异常：", e);
        String tag = httpServletRequest.getHeader("tag");
        HttpStatus status = getStatus(httpServletRequest);
        int stat = status.value();
        if (StringUtils.isBlank(tag)) {
            if( e instanceof BusinessException) {//如果是自定义错误，就跳转到自定义错误页面
                return new ModelAndView("error/999");
            }
            httpServletResponse.setStatus(stat);
            switch (stat) {
                case 404:
                    return new ModelAndView("error/404");
                case 403:
                    return new ModelAndView("error/403");
                case 400:
                    return new ModelAndView("error/400");
                default:
                    return new ModelAndView("error/500");
            }
        }
        httpServletResponse.setContentType("application/json; charset=utf-8");
        ModelAndView mv = new ModelAndView();
        /*  使用FastJson提供的FastJsonJsonView视图返回，不需要捕获异常   */
        FastJsonJsonView view = new FastJsonJsonView();
        Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put("status", String.valueOf(stat));
        //
        if("500".equals(String.valueOf(stat))){
        	 attributes.put("msg", ResponseCode.ERROR.getDesc());
        }else{
        	attributes.put("msg", e.getLocalizedMessage());
        }
        view.setAttributesMap(attributes);
        mv.setView(view);
        return mv;
    }
    HttpStatus getStatus(HttpServletRequest request){
        Integer statusCode = (Integer)request.getAttribute("javax.servlet.error.status_code");
        if (statusCode == null) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        } else {
            try {
                return HttpStatus.valueOf(statusCode);
            } catch (Exception var4) {
                return HttpStatus.INTERNAL_SERVER_ERROR;
            }
        }
    }
}
