package top.chenat.commondao;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import top.chenat.commondao.bean.Entity;
import top.chenat.commondao.bean.Style;
import top.chenat.commondao.utils.StringUtil;

import javax.persistence.Id;
import javax.persistence.Transient;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
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
        entity = new Entity();
        entity.setColumns(getColumns(entityClass));
        entity.setPrimaryKey(entity.getPrimaryKey());
        entity.setTableName(StringUtil.convertByStyle(entityClass.getSimpleName(), Style.camelhump));
        entityClassMap.put(key, entity);
        return entity;
    }

    protected Set<Entity.Column> getColumns(Class<?> entityClass) {
        Set<Entity.Column> columns = new TreeSet<>();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(entityClass);
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            Field[] fields=entityClass.getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(Transient.class)) {
                    continue;
                }
                for (PropertyDescriptor desc : propertyDescriptors) {
                    if (!desc.getName().equals("class")&&desc.getName().equals(field.getName())) {
                        Entity.Column column = new Entity.Column();
                        column.setName(desc.getName());
                        column.setJavaType(desc.getPropertyType());
                        column.setReadMethod(desc.getReadMethod());
                        column.setWriteMethod(desc.getWriteMethod());
                        column.setField(field);
                        if (field.isAnnotationPresent(Id.class)) {
                            column.setIdentity(true);
                        }
                        columns.add(column);
                    }
                }
            }
        } catch (IntrospectionException e) {
            throw new RuntimeException(e);
        }
        return columns;
    }

    //获取非空的字段
    protected Set<Entity.Column> getNotNullColumn(Object record, Entity entity) throws Exception {
        Set<Entity.Column> columns = new TreeSet<>();
        for (Entity.Column column : entity.getColumns()) {
            if (column.getReadMethod().invoke(record) != null) {
                columns.add(column);
            }
        }
        return columns;
    }

    /**
     * 获取 record 里字段的值
     */
    protected MapSqlParameterSource getParameterMap(Object record, Set<Entity.Column> columns, boolean ignorePrimaryKey) throws Exception {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        for (Entity.Column column : columns) {
            if (ignorePrimaryKey && column.isIdentity()) {
                continue;
            }
            parameters.addValue(column.getName(), column.getReadMethod().invoke(record));
        }
        return parameters;
    }

    public static Entity getEntityTable(Class<?> entityClass) {
        String key = entityClass.getSimpleName();
        return entityClassMap.get(key);
    }


}
