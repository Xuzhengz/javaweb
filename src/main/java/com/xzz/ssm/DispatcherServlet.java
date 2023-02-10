package com.xzz.ssm;

import com.xzz.ssm.io.BeanFactory;
import com.xzz.ssm.io.XmlGetBean;
import com.xzz.utils.StrUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * @author 徐正洲
 * @create 2023-02-09 12:31
 * <p>
 * DispatcherServlet作用：利用反射转发请求至不同的servlet以及到具体的方法。
 */

@WebServlet("*.do")
public class DispatcherServlet extends ViewBaseServlet {
    private BeanFactory beanFactory = null;

    public void init() throws ServletException {
        super.init();
        beanFactory = new XmlGetBean();
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");

        String servletPath = req.getServletPath();
        String control = servletPath.substring(1);
        int servletPathLastIndex = control.lastIndexOf(".do");
        servletPath = control.substring(0, servletPathLastIndex);

        Object controllerBeanObj = beanFactory.getBeanById(servletPath);

        String operate = req.getParameter("operate");

        if (StrUtil.isEmpty(operate)) {
            operate = "index";
        }

        try {
            Method[] methods = controllerBeanObj.getClass().getDeclaredMethods();
            for (Method method : methods) {
                if (operate.equals(method.getName())) {
                    //1、统一获取参数
                    Parameter[] parameters = method.getParameters();

                    //用于存放参数的指
                    Object[] parameterValues = new Object[parameters.length];

                    for (int i = 0; i < parameters.length; i++) {
                        Parameter parameter = parameters[i];
                        String parameterName = parameter.getName();
                        if ("req".equals(parameterName)) {
                            parameterValues[i] = req;
                        } else if ("resp".equals(parameterName)) {
                            parameterValues[i] = resp;
                        } else if ("session".equals(parameterName)) {
                            parameterValues[i] = req.getSession();
                        } else {
                            //从请求中获取参数值
                            String parameterValue = req.getParameter(parameterName);
                            String typeName = parameter.getType().getName();

                            //定义一个所有类型都能接住的对象
                            Object parameterObj = parameterValue;

                            if (parameterObj != null) {
                                if ("java.lang.Integer".equals(typeName)) {
                                    parameterObj = Integer.parseInt(parameterValue);
                                }
                            }

                            parameterValues[i] = parameterObj;
                        }
                    }
                    method.setAccessible(true);
                    Object returnObj = method.invoke(controllerBeanObj, parameterValues);

                    //视图处理
                    if (returnObj != null) {
                        String methodValue = (String) returnObj;
                        if (methodValue.startsWith("redirect:")) {
                            String methodValueSub = methodValue.substring("redirect:".length());
                            resp.sendRedirect(methodValueSub);
                        } else {
                            super.processTemplate(methodValue, req, resp);
                        }

                    }


                }
            }
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }


    }
}
