package com.xzz.fruit.service;

import com.xzz.fruit.pojo.Fruit;

import java.util.List;

/**
 * @author 徐正洲
 * @create 2023-02-10 12:31
 */
public interface FruitService {
    //查询库存列表
    List<Fruit> getFruitList(String keyword, Integer pageNo);


    //根据id查询实体对象
    Fruit getFruitByFid(Integer fid);

    //更新实体对象
    void updateFruitByid(Fruit fruit);

    //根据id删除实体对象
    void delFruitByid(Integer fid);

    //新增实体对象
    void insertFruit(Fruit fruit);

    //查询总页数
    int getPageCount(String keyword);
}
