package com.chenat.commondao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.persistence.Id;
import javax.persistence.Transient;
import java.lang.reflect.Field;
import java.util.*;

/**
 * Created by ChenAt 2017/10/14.
 * desc:
 */
public class CommonDao {

    private static Map<String, Entity> entityClassMap = new HashMap<>();

    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public CommonDao(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void insertSelective(Object record){
        try {
            Entity entity=getEntity(record.getClass());
            final Set<Entity.Column> insertColumn = getInsertColumn(record, entity);
            final StringBuilder sb = new StringBuilder();
            sb.append(SqlHelper.insertIntoTable(entity.getTableName()));
            sb.append(SqlHelper.insertColumns(record, insertColumn));
            System.out.println(sb.toString());
            KeyHolder holder=new GeneratedKeyHolder();
            jdbcTemplate.update(sb.toString(), getParameterMap(record, insertColumn), holder, new String[]{entity.getPrimaryKey().getName()});
            int primaryKeyValue = holder.getKey().intValue();
            entity.getPrimaryKey().set(record, primaryKeyValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Set<Entity.Column> getInsertColumn(Object record, Entity entity) throws Exception {
        Set<Entity.Column> columns = new TreeSet<>();
        for (Entity.Column column : entity.getColumns()) {
            if (column.isIdentity()) {
                continue;
            }
            if (column.getField().get(record) != null) {
                columns.add(column);
            }
        }
        return columns;
    }

    public MapSqlParameterSource getParameterMap(Object record, Set<Entity.Column> columns) throws Exception {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        for (Entity.Column column : columns) {
            parameters.addValue(column.getName(), column.getField().get(record));
        }
        return parameters;
    }

    private Entity getEntity(Class<?> entityClass) {
        String key = entityClass.getSimpleName();
        Entity entity=entityClassMap.get(key);
        if (entity != null) {
            return entity;
        }
        Set<Entity.Column> columns = new HashSet<Entity.Column>();
        entity = new Entity();
        Field[] fields=entityClass.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Transient.class)) {
                continue;
            }
            field.setAccessible(true);
            Entity.Column column = new Entity.Column();
            column.setName(field.getName());
            column.setJavaType(field.getType());
            column.setField(field);
            if (field.isAnnotationPresent(Id.class)) {
                entity.setPrimaryKey(field);
                column.setIdentity(true);
            }
            columns.add(column);
        }
        entity.setTableName(StringUtil.convertByStyle(entityClass.getSimpleName(), Style.camelhump));
        entity.setColumns(columns);
        entityClassMap.put(key, entity);
        return entity;
    }

}
