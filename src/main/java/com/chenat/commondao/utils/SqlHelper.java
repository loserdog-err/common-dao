package com.chenat.commondao.utils;

import com.chenat.commondao.bean.Entity;

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

    public static String selectFromTable(String tableName) {
        return "SELECT * FROM " + tableName + " ";
    }

    public static String whereClause(Set<Entity.Column> columns) {
        StringBuilder whereClause = new StringBuilder("WHERE ");
        for (Entity.Column column : columns) {
            whereClause.append(column.getName()).append("=").append(":").append(column.getName()).append(" AND ");
        }
        whereClause.delete(whereClause.lastIndexOf("AND"), whereClause.length());
        return whereClause.toString();
    }

    public static String deleteFromTable(String tableName) {
        return "DELETE FROM " + tableName + " ";
    }

    public static String updateFromTable(String tableName) {
        return "UPDATE " + tableName + " ";
    }

    public static String updateColumns(Set<Entity.Column> updateColumns) {
        StringBuilder sql = new StringBuilder("SET ");
        for (Entity.Column column : updateColumns) {
            if (column.isIdentity()) {
                continue;
            }
            sql.append(column.getName()).append("=").append(":").append(column.getName()).append(",");
        }
        sql.deleteCharAt(sql.length() - 1).append(" ");
        return sql.toString();
    }

    public static String limitClause(int start, int end) {
        return "LIMIT " + start + (end > 0 ? end : " ");
    }
}
