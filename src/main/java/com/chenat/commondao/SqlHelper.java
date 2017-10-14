package com.chenat.commondao;

import java.util.Set;

/**
 * Created by ChenAt 2017/10/14.
 * desc:
 */
public class SqlHelper {

    public static String insertIntoTable(String tableName) {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO ");
        sql.append(tableName);
        sql.append(" ");
        return sql.toString();
    }

    public static String insertColumns(Object record, Set<Entity.Column> columns) {
        StringBuilder sql = new StringBuilder("(");
        //获取全部列
        //当某个列有主键策略时，不需要考虑他的属性是否为空，因为如果为空，一定会根据主键策略给他生成一个值
        for (Entity.Column column : columns) {
            sql.append(column.getName() + ",");
        }
        sql.deleteCharAt(sql.length() - 1);
        sql.append(") ");

        sql.append(" VALUES (");
        for (Entity.Column column : columns) {
            sql.append(":").append(column.getName()).append(",");
        }
        sql.deleteCharAt(sql.length() - 1);
        sql.append(") ");
        return sql.toString();
    }

    public static void setJdbcTypeByJavaType() {

    }
}
