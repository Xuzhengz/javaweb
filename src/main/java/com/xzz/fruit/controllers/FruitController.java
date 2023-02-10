package com.xzz.fruit.controllers;

import com.xzz.fruit.service.FruitService;
import com.xzz.fruit.pojo.Fruit;
import com.xzz.ssm.ViewBaseServlet;
import com.xzz.utils.StrUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author 徐正洲
 * @create 2023-02-09 10:07
 */
public class FruitController extends ViewBaseServlet {

    private FruitService fruitService = null;

    private String add(String fname, Integer price, Integer fcount, String remark) {
        fruitService.insertFruit(new Fruit(0, fname, price, fcount, remark));
        return "redirect:fruit.do";
    }


    private String index(String oper, String keyword, Integer pageNo, HttpServletRequest req) {
        HttpSession session = req.getSession();


        if (pageNo == null) {
            pageNo = 1;
        }

        if (StrUtil.isNotEmpty(oper) && "search".equals(oper)) {
            pageNo = 1;
            if (StrUtil.isEmpty(keyword)) {
                keyword = "";
            }
            //将keyword保存到session
            session.setAttribute("keyword", keyword);
        } else {
            //如果不是点击查询按钮，那么查询是基于session中保存的现有keyword进行查询
            Object keywordValue = session.getAttribute("keyword");
            if (keywordValue != null) {
                keyword = (String) keywordValue;
            } else {
                keyword = "";
            }

        }
        session.setAttribute("pageNo", pageNo);

        List<Fruit> fruitList = fruitService.getFruitList(keyword, pageNo);

        session.setAttribute("fruitList", fruitList);

        //总页数
        int pageCount = fruitService.getPageCount(keyword);

        session.setAttribute("pageCount", pageCount);

        return "fruit/index";

    }

    private String del(Integer fid) {
        if (fid != null) {
            fruitService.delFruitByid(fid);
            return "redirect:fruit.do";
        }
        return "error";
    }

    private String edit(Integer fid, HttpServletRequest req) {
        if (fid != null) {
            Fruit fruit = fruitService.getFruitByFid(fid);
            req.setAttribute("fruit", fruit);
            return "fruit/edit";
        }
        return "error";
    }

    private String update(Integer fid, String fname, Integer price, Integer fcount, String remark) {
        fruitService.updateFruitByid(new Fruit(fid, fname, price, fcount, remark));
        return "redirect:fruit.do";
    }
}
