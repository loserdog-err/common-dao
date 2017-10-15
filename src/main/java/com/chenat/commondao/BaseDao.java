package com.chenat.commondao;

import com.chenat.commondao.paging.PageInfo;
import com.chenat.commondao.support.DaoSupport;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.ParameterizedType;
import java.util.List;

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

    /**
     * 根据主键删除 entity
     */
    public int deleteByPrimaryKey(Object primaryKey) {
        return daoSupport.deleteByPrimaryKey(primaryKey, entityClass);
    }

    /**
     * 根据主键更新 entity
     */
    public int updateByPrimaryKeySelective(T record) {
        return daoSupport.updateByPrimaryKeySelective(record);
    }

    /**
     * 根据查询参数获取 entity
     */
    public List<T> select(T record) {
        return daoSupport.select(record);
    }

    /**
     * 根据查询参数获取单个 entity
     */
    public T selectOne(T record) {
        return daoSupport.selectOne(record);
    }

    /**
     * 分页查询
     */
    public PageInfo<T> selectByPage(int pageNum, int pageSize, T record) {
        return daoSupport.selectByPage(pageNum, pageSize, record);
    }

}
