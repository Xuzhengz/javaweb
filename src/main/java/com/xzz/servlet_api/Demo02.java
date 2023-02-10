package com.xzz.servlet_api;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author 徐正洲
 * @create 2023-02-10 15:48
 */
@WebServlet("/demo02")
public class Demo02 extends HttpServlet {


    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("响应。。");

        req.getRequestDispatcher("succ.html").forward(req, resp);
    }
}
