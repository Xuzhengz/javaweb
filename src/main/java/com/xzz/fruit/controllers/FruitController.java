package com.xzz.fruit.controllers;

import com.xzz.fruit.dao.FruitDAOImpl;
import com.xzz.fruit.pojo.Fruit;
import com.xzz.ssm.ViewBaseServlet;
import com.xzz.utils.StrUtil;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author 徐正洲
 * @create 2023-02-09 10:07
 */
public class FruitController extends ViewBaseServlet {
    private ServletContext servletContext;

    public void setServletContext(ServletContext servletContext) throws ServletException {
        this.servletContext = servletContext;
        super.init(servletContext);
    }

    private FruitDAOImpl fruitDAO = new FruitDAOImpl();

    private void add(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String fname = req.getParameter("fname");
        Integer price = Integer.valueOf(req.getParameter("price"));
        Integer fcount = Integer.valueOf(req.getParameter("fcount"));
        String remark = req.getParameter("remark");


        fruitDAO.insertFruit(new Fruit(0, fname, price, fcount, remark));

        resp.sendRedirect("fruit.do");
    }


    private void index(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();

        //设置当前页，默认1
        Integer pageNum = 1;

        //判断是否有值，如果有则是通过点击查询过来的请求，反之就是正常搜索操作
        String oper = req.getParameter("oper");

        String keyword = null;

        if (StrUtil.isNotEmpty(oper) && "search".equals(oper)) {
            pageNum = 1;
            keyword = req.getParameter("keyword");
            if (StrUtil.isEmpty(keyword)) {
                keyword = "";
            }
            //将keyword保存到session
            session.setAttribute("keyword", keyword);
        } else {
            String pageNo = req.getParameter("pageNo");
            if (pageNo != null && !"".equals(pageNo)) {
                pageNum = Integer.parseInt(pageNo);
            }

            //如果不是点击查询按钮，那么查询是基于session中保存的现有keyword进行查询
            Object keywordObj = session.getAttribute("keyword");
            if (keywordObj != null) {
                keyword = (String) keywordObj;
            } else {
                keyword = "";
            }
        }
        session.setAttribute("pageNo", pageNum);

        List<Fruit> fruitList = fruitDAO.getFruitList(keyword, pageNum);

        session.setAttribute("fruitList", fruitList);

        //总记录
        int fruitCount = fruitDAO.getFruitCount(keyword);

        //总页数
        int pageCount = (fruitCount + 5 - 1) / 5;

        session.setAttribute("pageCount", pageCount);


        super.processTemplate("fruit/index", req, resp);

    }

    private void del(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String fid = req.getParameter("fid");
        if (StrUtil.isNotEmpty(fid)) {
            int id = Integer.parseInt(fid);
            fruitDAO.delFruitByid(id);
            resp.sendRedirect("fruit.do");
        }
    }

    private void edit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String fid = req.getParameter("fid");

        if (StrUtil.isNotEmpty(fid)) {
            int id = Integer.parseInt(fid);
            Fruit fruit = fruitDAO.getFruitByFid(id);
            req.setAttribute("fruit", fruit);
            super.processTemplate("fruit/edit", req, resp);
        }
    }

    protected void update(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer fid = Integer.valueOf(req.getParameter("fid"));
        String fname = req.getParameter("fname");
        Integer price = Integer.valueOf(req.getParameter("price"));
        Integer fcount = Integer.valueOf(req.getParameter("fcount"));
        String remark = req.getParameter("remark");

        fruitDAO.updateFruitByid(new Fruit(fid, fname, price, fcount, remark));

        resp.sendRedirect("fruit.do");
    }
}
