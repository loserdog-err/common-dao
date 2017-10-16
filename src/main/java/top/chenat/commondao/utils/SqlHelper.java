package top.chenat.commondao.utils;

import top.chenat.commondao.bean.Entity;
import org.springframework.util.CollectionUtils;
import top.chenat.commondao.bean.Example;

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
        if (CollectionUtils.isEmpty(columns)) {
            return " ";
        }
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
        return "LIMIT " + start + (end > 0 ? ","+end : " ");
    }

    public static String exampleWhereClause(Example example) {
        if (CollectionUtils.isEmpty(example.getOredCriteria())) {
            return "";
        }
        StringBuilder sql = new StringBuilder("");

        for (Example.Criteria oredCriteria : example.getOredCriteria()) {
            if (!oredCriteria.isValid()) {
                continue;
            }
            for (int i=0;i<oredCriteria.getCriteria().size();i++ ) {

                Example.Criterion criterion = oredCriteria.getCriteria().get(i);
                if (i != 0) {
                    sql.append(" AND ");
                }
                if (criterion.isNoValue()) {
                    sql.append(criterion.getCondition());
                } else if (criterion.isSingleValue()) {
                    sql.append(criterion.getCondition()).append(" ").append(criterion.getValue());
                } else if (criterion.isBetweenValue()) {
                    sql.append(criterion.getCondition()).append(" ").append(criterion.getValue()).append(" ").append(criterion.getSecondValue());
                }
            }
            sql.append(" or ");
        }
        sql.delete(sql.lastIndexOf("or"), sql.length());
        sql.insert(0, " WHERE ");
        return sql.toString();

//        return "<if test=\"_parameter != null\">" +
//                "<where>\n" +
//                "  <foreach collection=\"oredCriteria\" item=\"criteria\" separator=\"or\">\n" +
//                "    <if test=\"criteria.valid\">\n" +
//                "      <trim prefix=\"(\" prefixOverrides=\"and\" suffix=\")\">\n" +
//                "        <foreach collection=\"criteria.criteria\" item=\"criterion\">\n" +
//                "          <choose>\n" +
//                "            <when test=\"criterion.noValue\">\n" +
//                "              and ${criterion.condition}\n" +
//                "            </when>\n" +
//                "            <when test=\"criterion.singleValue\">\n" +
//                "              and ${criterion.condition} #{criterion.value}\n" +
//                "            </when>\n" +
//                "            <when test=\"criterion.betweenValue\">\n" +
//                "              and ${criterion.condition} #{criterion.value} and #{criterion.secondValue}\n" +
//                "            </when>\n" +
//                "            <when test=\"criterion.listValue\">\n" +
//                "              and ${criterion.condition}\n" +
//                "              <foreach close=\")\" collection=\"criterion.value\" item=\"listItem\" open=\"(\" separator=\",\">\n" +
//                "                #{listItem}\n" +
//                "              </foreach>\n" +
//                "            </when>\n" +
//                "          </choose>\n" +
//                "        </foreach>\n" +
//                "      </trim>\n" +
//                "    </if>\n" +
//                "  </foreach>\n" +
//                "</where>" +
//                "</if>";
    }
}
