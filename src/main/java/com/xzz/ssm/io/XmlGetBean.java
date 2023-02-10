package com.xzz.ssm.io;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 徐正洲
 * @create 2023-02-10 13:55
 */
public class XmlGetBean implements BeanFactory {
    private Map<String, Object> beanMap = new HashMap<>();

    public XmlGetBean() {
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("applicationContext.xml");
            //1.创建DocumentBuilderFactory
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            //2.创建DocumentBuilder对象
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            //3.创建Document对象
            Document document = documentBuilder.parse(inputStream);

            //4.获取所有的bean节点
            NodeList beanNodeList = document.getElementsByTagName("bean");
            for (int i = 0; i < beanNodeList.getLength(); i++) {
                Node beanNode = beanNodeList.item(i);
                if (beanNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element beanElement = (Element) beanNode;
                    String beanId = beanElement.getAttribute("id");
                    String className = beanElement.getAttribute("class");
                    Class controllerBeanClass = Class.forName(className);
                    Object beanObj = controllerBeanClass.newInstance();
                    beanMap.put(beanId, beanObj);
                }
            }
            //bean与bean之间的依赖关系
            for (int i = 0; i < beanNodeList.getLength(); i++) {
                Node beanNode = beanNodeList.item(i);
                if (beanNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element beanElement = (Element) beanNode;
                    String beanId = beanElement.getAttribute("id");
                    NodeList childNodes = beanElement.getChildNodes();
                    for (int j = 0; j < childNodes.getLength(); j++) {
                        Node item = childNodes.item(j);
                        if (item.getNodeType() == Node.ELEMENT_NODE && item.getNodeName().equals("property")) {
                            Element propertElement = (Element) item;
                            String name = propertElement.getAttribute("name");
                            String ref = propertElement.getAttribute("ref");
                            //找到ref的实例
                            Object refObj = beanMap.get(ref);
                            //将ref的bean对象设置到实例的property的属性上
                            Object beanObj = beanMap.get(beanId);
                            Class beanObjClass = beanObj.getClass();
                            Field declaredField = beanObjClass.getDeclaredField(name);

                            declaredField.setAccessible(true);
                            declaredField.set(beanObj, refObj);


                        }

                    }


                }


            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Object getBeanById(String id) {
        return beanMap.get(id);
    }
}
