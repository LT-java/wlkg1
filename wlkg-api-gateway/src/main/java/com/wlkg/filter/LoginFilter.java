package com.wlkg.filter;


import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.wlkg.config.FilterProperties;
import com.wlkg.config.JwtProperties;
import com.wlkg.utils.CookieUtils;
import com.wlkg.utils.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.soap.Addressing;

@Component
@EnableConfigurationProperties({JwtProperties.class,FilterProperties.class})
public class LoginFilter extends ZuulFilter {

    @Autowired
    private JwtProperties prop;

    @Autowired
    private FilterProperties filterProperties;

    private static final Logger logger = LoggerFactory.getLogger(JwtProperties.class);

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 5;
    }

    @Override
    public boolean shouldFilter() {

        //获取上下文
        RequestContext ctx = RequestContext.getCurrentContext();
        //获取request
        HttpServletRequest request = ctx.getRequest();
        //获取路径
        String requestURI = request.getRequestURI();
        //判断白名单
        return !isAllowPath(requestURI);

    }

    private boolean isAllowPath(String requestURI) {
        //定义一个标记
        boolean flag = false;
        //遍历允许访问的路径
        for(String path : filterProperties.getAllowPaths()){
            //然后判断是否符合
            if(requestURI.startsWith(path)){
                flag = true;
                break;
            }
        }
        return flag;
    }

    @Override
    public Object run() throws ZuulException {

        //获取上下文
        RequestContext ctx = RequestContext.getCurrentContext();

        //获取request
        HttpServletRequest request = ctx.getRequest();

        //获取token
        String token = CookieUtils.getCookieValue(request,prop.getCookieName());

        //校验
        try {
            //校验通过什么都不做，即放行
            JwtUtils.getInfoFromToken(token,prop.getPublicKey());

        }catch(Exception e){
            //校验出现异常，返回403
            ctx.setSendZuulResponse(false);
            ctx.setResponseStatusCode(403);
            logger.error("非法访问，未登录，地址：{}", request.getRemoteHost(), e );
        }

        return null;
    }
}
