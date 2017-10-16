package top.chenat.commondao.support;

import top.chenat.commondao.BaseDaoSupport;
import top.chenat.commondao.bean.Entity;
import top.chenat.commondao.bean.Example;
import top.chenat.commondao.paging.CountSqlParser;
import top.chenat.commondao.paging.PageInfo;
import top.chenat.commondao.utils.SqlHelper;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Created by ChenAt 2017/10/14.
 * desc:
 */
public class SelectSupport extends BaseDaoSupport {

    private NamedParameterJdbcTemplate jdbcTemplate;

    private CountSqlParser countSqlParser= new CountSqlParser();

    public SelectSupport(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public <T> T selectByPrimaryKey(Object primaryKey, Class<T> entityClass) {
        Entity entity=getEntity(entityClass);
        Entity.Column primaryKeyColumn=entity.getPrimaryKey();
        if (primaryKey == null) {
            throw new RuntimeException("没有指定主键");
        }
        final StringBuilder sql = new StringBuilder();
        sql.append(SqlHelper.selectFromTable(entity.getTableName()));
        sql.append(SqlHelper.whereClause(Collections.singleton(primaryKeyColumn)));
        System.out.println(sql.toString());
        List<T> resultList=jdbcTemplate.query(sql.toString(), new MapSqlParameterSource(primaryKeyColumn.getName(), primaryKey), new BeanPropertyRowMapper<>(entityClass));
        if (!CollectionUtils.isEmpty(resultList)) {
            return resultList.get(0);
        }
        return null;
    }

    private <T> List<T> select(T record,boolean selectOne) {
        Class<T> clazz = (Class<T>) record.getClass();
        Entity entity=getEntity(record.getClass());
        try {
            Object primaryKeyValue = entity.getPrimaryKey().getReadMethod().invoke(record);
            if (primaryKeyValue!=null) {
                return Collections.singletonList(selectByPrimaryKey(primaryKeyValue, clazz));
            }
            Set<Entity.Column> notNullColumn = getNotNullColumn(record, entity);
            StringBuffer sql = new StringBuffer();
            sql.append(SqlHelper.selectFromTable(entity.getTableName()));
            sql.append(SqlHelper.whereClause(notNullColumn));
            if (selectOne) {
                sql.append(SqlHelper.limitClause(1, -1));
            }
            System.out.println(sql.toString());
            return jdbcTemplate.query(sql.toString(), getParameterMap(record, notNullColumn, false), new BeanPropertyRowMapper<>(clazz));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public <T> List<T> select(T record) {
        return select(record, false);
    }

    public <T> T selectOne(T record) {
        List<T> result = select(record, true);
        if (!CollectionUtils.isEmpty(result)) {
            return result.get(0);
        }
        return null;
    }

    public <T> PageInfo<T> selectByPage(int pageNum, int pageSize, T record) {

        Class<T> clazz = (Class<T>) record.getClass();
        Entity entity=getEntity(record.getClass());
        try {
            StringBuffer sql = new StringBuffer(SqlHelper.selectFromTable(entity.getTableName()));
            Set<Entity.Column> notNullColumn = getNotNullColumn(record, entity);
            Object primaryKeyValue = entity.getPrimaryKey().getField().get(record);
            if (primaryKeyValue!=null) {
                notNullColumn = Collections.singleton(entity.getPrimaryKey());
            }
            MapSqlParameterSource parameterMap = getParameterMap(record, notNullColumn, false);
            sql.append(SqlHelper.whereClause(notNullColumn));
            sql.append(SqlHelper.limitClause((pageNum-1) * pageSize, pageSize));
            String countSql=countSqlParser.getSimpleCountSql(sql.toString());
            //查询 count
            Integer count = jdbcTemplate.queryForObject(countSql, parameterMap, Integer.class);
            //分页查询
            List<T> result=jdbcTemplate.query(sql.toString(), getParameterMap(record, notNullColumn, false), new BeanPropertyRowMapper<>(clazz));

            System.out.println(sql.toString());
            System.out.println("count sql ===> "+countSql);

            PageInfo<T> pageInfo = new PageInfo<>(result, count == null ? 0 : count, pageNum,pageSize);

            return pageInfo;
//            return jdbcTemplate.query(sql.toString(), getParameterMap(record, notNullColumn, false), new BeanPropertyRowMapper<>(clazz));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public  List<?> selectByExample(Example example) {
        StringBuilder sql = new StringBuilder();
        sql.append(SqlHelper.selectFromTable(example.getTableName()));
        sql.append(SqlHelper.exampleWhereClause(example));
        System.out.println(sql.toString());
        return jdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<>(example.getEntityClass()));
    }
}
