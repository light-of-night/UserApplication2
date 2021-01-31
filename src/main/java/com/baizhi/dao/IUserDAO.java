package com.baizhi.dao;

import com.baizhi.entities.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface IUserDAO {
    public void registUser(User user);

    public void deleteById(@Param("id") Integer id);

    public void updateUser(User user);

    public User queryByName(@Param("name") String name);

    public User queryById(@Param("id") Integer id);

    //    分页查询
    public List<User> queryByPage(
            @Param(value = "pageNow") Integer pageNow,
            @Param(value = "pageSize") Integer pageSize,
            @Param(value = "column") String column,
            @Param(value = "value") Object value);

    int queryCount(
            @Param(value = "column") String column,
            @Param(value = "value") Object value);

}
