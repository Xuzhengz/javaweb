package com.xzz.servlet_api;

import javax.jws.soap.InitParam;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

/**
 * @author 徐正洲
 * @create 2023-02-10 11:09
 *
 * 生命周期：
 * 1）实例化
 * 2）初始化
 *      两个方法: init(),init(config)
 *      可在初始化方法中获取初始化已定义好的方法
 *      servletConfig可在注解@WebServlet获取初始化参数也可以在web.xml中通过<init-parmer></>获取
 *      servletContext可在web.xml中<context-param></>获取初始化参数
 * 3）服务
 *
 *
 * 4）销毁
 */
@WebServlet(urlPatterns = "/demo01",initParams = @WebInitParam(name = "name",value = "xzz"))
public class Demo01 extends HttpServlet {
    @Override
    public void init() throws ServletException {
        ServletConfig servletConfig = getServletConfig();
        String name = servletConfig.getInitParameter("name");
        System.out.println(name);

        ServletContext servletContext = getServletContext();
        String initParameter = servletContext.getInitParameter("view-prefix");

        System.out.println(initParameter.toString());
    }
}
