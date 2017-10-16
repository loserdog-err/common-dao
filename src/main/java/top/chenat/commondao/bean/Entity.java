package top.chenat.commondao.bean;

import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Entity {

    private String tableName;

    private Column primaryKey;

    private Set<Column> columns;


    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Set<Column> getColumns() {
        return columns;
    }

    public void setColumns(Set<Column> columns) {
        this.columns = columns;
    }

    public Column getPrimaryKey() {
        if (primaryKey != null) {
            return primaryKey;
        }
        if (CollectionUtils.isEmpty(columns)) {
            return null;
        }
        for (Column column : columns) {
            if (column.isIdentity()) {
                return column;
            }
        }
        return null;
    }

    public void setPrimaryKey(Column primaryKey) {
        this.primaryKey = primaryKey;
    }

    public Map<String,Column> getPropertyMap() {
        Map<String, Column> map = new HashMap<>();
        for (Entity.Column column : columns) {
            map.put(column.getName(), column);
        }
        return map;
    }
    public static class Column implements Comparable<Column>{
        private String name;

        private boolean identity;

        private Class<?> javaType;

        private Field field;

        private Method readMethod;

        private Method writeMethod;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Class<?> getJavaType() {
            return javaType;
        }

        public void setJavaType(Class<?> javaType) {
            this.javaType = javaType;
        }

        public boolean isIdentity() {
            return identity;
        }

        public void setIdentity(boolean identity) {
            this.identity = identity;
        }

        public Field getField() {
            return field;
        }

        public void setField(Field field) {
            this.field = field;
        }

        public Method getReadMethod() {
            return readMethod;
        }

        public void setReadMethod(Method readMethod) {
            this.readMethod = readMethod;
        }

        public Method getWriteMethod() {
            return writeMethod;
        }

        public void setWriteMethod(Method writeMethod) {
            this.writeMethod = writeMethod;
        }

        public int compareTo(Column o) {
            return o.name.compareTo(this.name);
        }
    }
}
