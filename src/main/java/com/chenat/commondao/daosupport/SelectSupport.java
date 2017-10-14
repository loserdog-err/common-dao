package com.chenat.commondao.daosupport;

import com.chenat.commondao.BaseDaoSupport;
import com.chenat.commondao.bean.Entity;
import com.chenat.commondao.utils.SqlHelper;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

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
}
