package com.xzz.fruit.dao;

import com.xzz.fruit.pojo.Fruit;
import com.xzz.ssm.BaseDAO;

import java.util.List;

public class FruitDAOImpl extends BaseDAO<Fruit> implements FruitDAO {
    @Override
    public List<Fruit> getFruitList(String keyword, Integer pageNo) {
        return super.executeQuery("select * from t_fruit where fname like ? or remark like ? limit ?,5", "%" + keyword + "%", "%" + keyword + "%", ((pageNo - 1) * 5));
    }

    @Override
    public Fruit getFruitByFid(Integer fid) {
        return super.load("select * from t_fruit where fid = ?", fid);
    }

    @Override
    public void updateFruitByid(Fruit fruit) {
        String sql = "update t_fruit set fname = ?, price = ?, fcount = ?, remark = ? where fid = ?";
        super.executeUpdate(sql, fruit.getFname(), fruit.getPrice(), fruit.getFcount(), fruit.getRemark(), fruit.getFid());
    }

    @Override
    public void delFruitByid(Integer fid) {
        String sql = "delete from t_fruit where fid = ?";
        super.executeUpdate(sql, fid);
    }

    @Override
    public void insertFruit(Fruit fruit) {
        String sql = "insert into t_fruit values(0,?,?,?,?)";
        super.executeUpdate(sql, fruit.getFname(), fruit.getPrice(), fruit.getFcount(), fruit.getRemark());
    }

    @Override
    public int getFruitCount(String keyword) {
        return ((Long) super.executeComplexQuery("select count(0) from t_fruit where fname like ? or remark like ?", "%" + keyword + "%", "%" + keyword + "%")[0]).intValue();
    }
}