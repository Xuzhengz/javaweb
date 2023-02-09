package com.xzz.demo;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/demo1")
public class demo1 extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        //request保存作用域
/*        req.setAttribute("name","xzz");
        req.getRequestDispatcher("demo2").forward(req,resp);*/

        //session保存作用域
/*        HttpSession session = req.getSession();
        session.setAttribute("name","xzz");*/

        //application保存作用域
        ServletContext app = req.getServletContext();
        app.setAttribute("name", "com/xzz");
        resp.sendRedirect("demo2");


    }
}
