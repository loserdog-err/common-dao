package com.chenat.commondao.support;

import com.chenat.commondao.BaseDaoSupport;
import com.chenat.commondao.bean.Entity;
import com.chenat.commondao.utils.SqlHelper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.util.Set;

/**
 * Created by ChenAt 2017/10/14.
 * desc:
 */
public class InsertSupport extends BaseDaoSupport {

    private NamedParameterJdbcTemplate jdbcTemplate;

    public InsertSupport(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    public void insertSelective(Object record){
        try {
            Entity entity=getEntity(record.getClass());
            final Set<Entity.Column> insertColumn = getNotNullColumn(record, entity);
            final StringBuilder sql = new StringBuilder();
            sql.append(SqlHelper.insertIntoTable(entity.getTableName()));
            sql.append(SqlHelper.insertColumns(record, insertColumn));
            System.out.println(sql.toString());
            KeyHolder holder=new GeneratedKeyHolder();
            jdbcTemplate.update(sql.toString(), getParameterMap(record, insertColumn,false), holder, new String[]{entity.getPrimaryKey().getName()});
            int primaryKeyValue = holder.getKey().intValue();
            entity.getPrimaryKey().getField().set(record, primaryKeyValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
