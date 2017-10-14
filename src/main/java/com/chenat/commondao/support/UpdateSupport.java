package com.chenat.commondao.support;

import com.chenat.commondao.BaseDaoSupport;
import com.chenat.commondao.bean.Entity;
import com.chenat.commondao.utils.SqlHelper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.Collections;
import java.util.Set;

/**
 * Created by ChenAt 2017/10/14.
 * desc:
 */
public class UpdateSupport extends BaseDaoSupport {

    private NamedParameterJdbcTemplate jdbcTemplate;

    public UpdateSupport(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public <T> int updateByPrimaryKeySelective(T record) {

        try {
            Entity entity = getEntity(record.getClass());
            Entity.Column primaryKeyColumn=entity.getPrimaryKey();
            Set<Entity.Column> updateColumns = getNotNullColumn(record, entity);
            final StringBuilder sql = new StringBuilder();
            sql.append(SqlHelper.updateFromTable(entity.getTableName()));
            sql.append(SqlHelper.updateColumns(updateColumns));
            sql.append(SqlHelper.whereClause(Collections.singleton(primaryKeyColumn)));
            System.out.println(sql.toString());
            return jdbcTemplate.update(sql.toString(), getParameterMap(record, updateColumns, false));
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }

    }

}
