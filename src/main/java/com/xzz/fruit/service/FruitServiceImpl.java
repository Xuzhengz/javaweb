package com.xzz.fruit.service;

import com.xzz.fruit.dao.FruitDAO;
import com.xzz.fruit.dao.FruitDAOImpl;
import com.xzz.fruit.pojo.Fruit;

import java.util.List;

/**
 * @author 徐正洲
 * @create 2023-02-10 12:32
 */
public class FruitServiceImpl implements FruitService {
    private FruitDAO fruitDAO = null;

    @Override
    public List<Fruit> getFruitList(String keyword, Integer pageNo) {
        return fruitDAO.getFruitList(keyword, pageNo);
    }

    @Override
    public Fruit getFruitByFid(Integer fid) {
        return fruitDAO.getFruitByFid(fid);
    }

    @Override
    public void updateFruitByid(Fruit fruit) {
        fruitDAO.updateFruitByid(fruit);
    }

    @Override
    public void delFruitByid(Integer fid) {
        fruitDAO.delFruitByid(fid);
    }

    @Override
    public void insertFruit(Fruit fruit) {
        fruitDAO.insertFruit(fruit);
    }

    @Override
    public int getPageCount(String keyword) {
        int fruitCount = fruitDAO.getFruitCount(keyword);
        int pageCount = (fruitCount + 5 - 1) / 5;
        return pageCount;
    }
}
