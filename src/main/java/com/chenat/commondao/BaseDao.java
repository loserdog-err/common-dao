package com.chenat.commondao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import javax.persistence.Id;
import javax.persistence.Transient;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.*;

public class BaseDao<T> {



    Class<T> entityClass;

    CommonDao commonDao;

    public BaseDao() {
        ParameterizedType pt = (ParameterizedType) this.getClass().getGenericSuperclass();
        this.entityClass = (Class<T>) pt.getActualTypeArguments()[0];
    }

    @Autowired
    public void setCommonDao(CommonDao commonDao) {
        this.commonDao = commonDao;
    }

    public void insertSelective(T record) {
        commonDao.insertSelective(record);

    }


}
