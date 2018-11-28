package com.thinkgem.jeesite.common.filter;


import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 修改响应头允许跨域
 *
 * @author kakasun
 * @create 2018-03-12 下午2:37
 */
public class CorsFilter implements Filter {

    //@Value("${diy.config.cors}")
    //boolean cors;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain) throws IOException, ServletException {
     //   if(cors) {
            HttpServletResponse response = (HttpServletResponse) res;
            HttpServletRequest reqs = (HttpServletRequest) req;
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Allow-Credentials", "true");
            response.setHeader( "Access-Control-Allow-Methods", "POST,GET,OPTIONS,DELETE,PUT" );
            response.setHeader("Access-Control-Max-Age", "3600");
            response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept,token,tag,timestamp,jeesite.session.id,__sid,__cookie");
            if (reqs.getMethod().equals( "OPTIONS" )) {
                response.setStatus( 200 );
                return;
            }
      //  }
        filterChain.doFilter(req, res);
    }
    @Override
    public void destroy() {

    }
}
