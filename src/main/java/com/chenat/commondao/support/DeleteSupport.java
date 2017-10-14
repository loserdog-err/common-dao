package com.chenat.commondao.support;

import com.chenat.commondao.BaseDaoSupport;
import com.chenat.commondao.bean.Entity;
import com.chenat.commondao.utils.SqlHelper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.Collections;

/**
 * Created by ChenAt 2017/10/14.
 * desc:
 */
public class DeleteSupport  extends BaseDaoSupport {

    private NamedParameterJdbcTemplate jdbcTemplate;

    public DeleteSupport(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public <T> int deleteByPrimaryKey(Object primaryKey, Class<T> entityClass) {
        Entity entity = getEntity(entityClass);
        Entity.Column primaryKeyColumn=entity.getPrimaryKey();
        final StringBuilder sql = new StringBuilder();
        sql.append(SqlHelper.deleteFromTable(entity.getTableName()));
        sql.append(SqlHelper.whereClause(Collections.singleton(primaryKeyColumn)));
        System.out.println(sql.toString());
        return jdbcTemplate.update(sql.toString(), new MapSqlParameterSource(primaryKeyColumn.getName(), primaryKey));
    }


}
