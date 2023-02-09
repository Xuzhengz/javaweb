package com.xzz.fruit.dao;

import com.xzz.fruit.pojo.Fruit;

import java.util.List;

public interface FruitDAO {
    //查询库存列表
    List<Fruit> getFruitList(String keyword, Integer pageNo);

    Fruit getFruitByFid(Integer fid);

    void updateFruitByid(Fruit fruit);

    void delFruitByid(Integer fid);

    void insertFruit(Fruit fruit);

    int getFruitCount(String keyword);
}
