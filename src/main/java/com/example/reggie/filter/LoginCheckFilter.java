package com.example.reggie.filter;

import com.alibaba.fastjson.JSON;
import com.example.reggie.commen.BaseContext;
import com.example.reggie.commen.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.zip.Inflater;

/**
 * @author Cavan
 * @date 2023-03-30
 * @qq 2069543852
 */
/*
 *@Description: 检查用户是否已经登良
 *@Author: Cavan
 *@Date: 2023/3/30
 */
@Slf4j
@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*")
public class LoginCheckFilter implements Filter {

    //路径匹配器，支持通配符
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        //1.获取本次请求的URI
        String requestURI = request.getRequestURI();
        log.info("拦截到请求：{}", requestURI);
        //定义不需要处理的请求路径
        String[] urls = {
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/common/**",
                "swagger-ui.html"
        };
        //2.判断本次请求是否需要处理
        boolean check = check(urls, requestURI);
        //3.如果不需要处理，则直接放行
        if (check) {
            log.info("本次{}请求不需要处理", requestURI);
            chain.doFilter(request, response);
            return;
        }
        //4.判断登录状态，如果已登录，则放行
        if (request.getSession().getAttribute("employee") != null) {
            log.info("用户已登录，用户id为：{}", request.getSession().getAttribute("employee"));
            Long empId = (Long) request.getSession().getAttribute("employee");
            BaseContext.setCurrentId(empId);
            chain.doFilter(request, response);
            return;
        }
        log.info("用户未登录");
        //5.如果未登录的返回未登录结果,通过输出流方式向客户端页面响应数据
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;
    }

    /*
     *@Description: 进行路径匹配，检查本次请求是否需要放行
     *@Author: Cavan
     *@Date: 2023/3/30
     */
    public boolean check(String[] urls, String requestURI) {
        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url, requestURI);
            if (match) {
                return true;
            }
        }
        return false;
    }
}
