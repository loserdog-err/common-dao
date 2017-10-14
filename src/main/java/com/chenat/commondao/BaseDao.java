package com.chenat.commondao;

import com.chenat.commondao.daosupport.DaoSupport;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.ParameterizedType;

public class BaseDao<T> {



    Class<T> entityClass;

    DaoSupport daoSupport;

    public BaseDao() {
        ParameterizedType pt = (ParameterizedType) this.getClass().getGenericSuperclass();
        this.entityClass = (Class<T>) pt.getActualTypeArguments()[0];
    }

    @Autowired
    public void setDaoSupport(DaoSupport daoSupport) {
        this.daoSupport = daoSupport;
    }

    /**
     * 插入不为空的字段
     */
    public void insertSelective(T record) {
        daoSupport.insertSelective(record);
    }

    /**
     * 根据主键获取 entity
     */
    public T selectByPrimaryKey(Object primaryKey) {
        return daoSupport.selectByPrimaryKey(primaryKey,entityClass);
    }

    public int deleteByPrimaryKey(Object primaryKey) {
        return daoSupport.deleteByPrimaryKey(primaryKey, entityClass);
    }

    public int updateByPrimaryKeySelective(T record) {
        return daoSupport.updateByPrimaryKeySelective(record);
    }


}
