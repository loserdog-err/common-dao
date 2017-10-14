package com.chenat.commondao.support;

import com.chenat.commondao.BaseDaoSupport;
import com.chenat.commondao.bean.Entity;
import com.chenat.commondao.utils.SqlHelper;
import com.sun.org.apache.regexp.internal.RE;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Created by ChenAt 2017/10/14.
 * desc:
 */
public class SelectSupport extends BaseDaoSupport {

    private NamedParameterJdbcTemplate jdbcTemplate;

    public SelectSupport(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public <T> T selectByPrimaryKey(Object primaryKey, Class<T> entityClass) {
        Entity entity=getEntity(entityClass);
        Entity.Column primaryKeyColumn=entity.getPrimaryKey();
        final StringBuilder sql = new StringBuilder();
        sql.append(SqlHelper.selectFromTable(entity.getTableName()));
        sql.append(SqlHelper.whereClause(Collections.singleton(primaryKeyColumn)));
        System.out.println(sql.toString());
        List<T> resultList=jdbcTemplate.query(sql.toString(), new MapSqlParameterSource(primaryKeyColumn.getName(), primaryKey), new BeanPropertyRowMapper<>(entityClass));
        if (!CollectionUtils.isEmpty(resultList)) {
            return resultList.get(0);
        }
        return null;
    }

    private <T> List<T> select(T record,boolean selectOne) {
        Class<T> clazz = (Class<T>) record.getClass();
        Entity entity=getEntity(record.getClass());
        try {
            Object primaryKeyValue = entity.getPrimaryKey().getField().get(record);
            if (primaryKeyValue!=null) {
                return Collections.singletonList(selectByPrimaryKey(primaryKeyValue, clazz));
            }
            Set<Entity.Column> notNullColumn = getNotNullColumn(record, entity);
            StringBuffer sql = new StringBuffer();
            sql.append(SqlHelper.selectFromTable(entity.getTableName()));
            sql.append(SqlHelper.whereClause(notNullColumn));
            if (selectOne) {
                sql.append(SqlHelper.limitClause(1, -1));
            }
            System.out.println(sql.toString());
            return jdbcTemplate.query(sql.toString(), getParameterMap(record, notNullColumn, false), new BeanPropertyRowMapper<>(clazz));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public <T> List<T> select(T record) {
        return select(record, false);
    }

    public <T> T selectOne(T record) {
        List<T> result = select(record, true);
        if (!CollectionUtils.isEmpty(result)) {
            return result.get(0);
        }
        return null;
    }
}
