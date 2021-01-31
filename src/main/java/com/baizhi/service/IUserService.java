package com.baizhi.service;

import com.baizhi.entities.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface IUserService {
    public void registUser(User user);

    public User queryByName(@Param("name") String name);

    public User queryById(@Param("id") Integer id);

    public void deleteByIds(Integer[] ids);

    public void updateUser(User user);

    public List<User> queryByPage(Integer pageNow, Integer pageSize,
                                  String column, Object value);

    int queryUserCount(String column, Object value);

    //    登录
    public User login(User user);
}
