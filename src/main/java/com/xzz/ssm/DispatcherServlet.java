package com.xzz.ssm;

import com.xzz.utils.StrUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 徐正洲
 * @create 2023-02-09 12:31
 *
 * DispatcherServlet作用：利用反射转发请求至不同的servlet以及到具体的方法。
 */

@WebServlet("*.do")
public class DispatcherServlet extends ViewBaseServlet {
    private Map<String, Object> beanMap = new HashMap<>();

    @Override
    public void init(ServletConfig config) throws ServletException {
        System.out.println("正在初始化~~~~~");
        InputStream resourceAsStream = DispatcherServlet.class.getClassLoader().getResourceAsStream("applicationContext.xml");

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();

        try {
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

            Document document = documentBuilder.parse(resourceAsStream);

            NodeList bean = document.getElementsByTagName("bean");

            for (int i = 0; i < bean.getLength(); i++) {
                Node item = bean.item(i);
                if (item.getNodeType() == Node.ELEMENT_NODE) {

                    Element element = (Element) item;

                    //获取xml中属性  fruit ---> FruitController
                    String id = element.getAttribute("id");
                    String aClass = element.getAttribute("class");

                    //获取到具体servlet类
                    Class controllerBeanClass = Class.forName(aClass);
                    
                    Object beanObj = controllerBeanClass.newInstance();

                    Method setServletContext = controllerBeanClass.getDeclaredMethod("setServletContext", ServletContext.class);
                    setServletContext.setAccessible(true);
                    setServletContext.invoke(beanObj, config.getServletContext());


                    beanMap.put(id, beanObj);
                }
            }


        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        String servletPath = req.getServletPath();
        String control = servletPath.substring(1);
        int i = control.lastIndexOf(".do");
        servletPath = control.substring(0, i);

        Object controllerBeanObj = beanMap.get(servletPath);

        String operate = req.getParameter("operate");

        if (StrUtil.isEmpty(operate)) {
            operate = "index";
        }

        //根据operate获取对应的方法
        try {
            Method method = controllerBeanObj.getClass().getDeclaredMethod(operate, HttpServletRequest.class, HttpServletResponse.class);
            if (method != null) {
                method.setAccessible(true);
                method.invoke(controllerBeanObj, req, resp);
            } else {
                System.out.println("operate非法！！！");
            }
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
