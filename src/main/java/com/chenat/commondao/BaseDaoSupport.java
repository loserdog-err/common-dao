package com.chenat.commondao;

import com.chenat.commondao.bean.Entity;
import com.chenat.commondao.bean.Style;
import com.chenat.commondao.utils.StringUtil;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import javax.persistence.Id;
import javax.persistence.Transient;
import java.lang.reflect.Field;
import java.util.*;

/**
 * Created by ChenAt 2017/10/14.
 * desc:
 */
public class BaseDaoSupport {
    private static Map<String, Entity> entityClassMap = new HashMap<>();


    protected Entity getEntity(Class<?> entityClass) {
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
                entity.setPrimaryKey(column);
                column.setIdentity(true);
            }
            columns.add(column);
        }
        entity.setTableName(StringUtil.convertByStyle(entityClass.getSimpleName(), Style.camelhump));
        entity.setColumns(columns);
        entityClassMap.put(key, entity);
        return entity;
    }

    //获取非空的字段
    protected Set<Entity.Column> getNotNullColumn(Object record, Entity entity) throws Exception {
        Set<Entity.Column> columns = new TreeSet<>();
        for (Entity.Column column : entity.getColumns()) {
            if (column.getField().get(record) != null) {
                columns.add(column);
            }
        }
        return columns;
    }

    protected MapSqlParameterSource getParameterMap(Object record, Set<Entity.Column> columns, boolean ignorePrimaryKey) throws Exception {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        for (Entity.Column column : columns) {
            if (ignorePrimaryKey && column.isIdentity()) {
                continue;
            }
            parameters.addValue(column.getName(), column.getField().get(record));
        }
        return parameters;
    }
}
