package com.xzz.servlet_api.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

/**
 * @author 徐正洲
 * @create 2023-02-10 15:52
 */
@WebFilter("/demo02")
public class Demo02Filter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("hello");
        filterChain.doFilter(servletRequest,servletResponse);
        System.out.println("hello2");

    }

    @Override
    public void destroy() {
    }
}
