package top.chenat.commondao.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import top.chenat.commondao.bean.Example;
import top.chenat.commondao.paging.PageInfo;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

/**
 * Created by ChenAt 2017/10/14.
 * desc:
 */
public class DaoSupport {


    private SelectSupport selectSupport;

    private UpdateSupport updateSupport;

    private DeleteSupport deleteSupport;

    private InsertSupport insertSupport;

    @Autowired
    public DaoSupport(NamedParameterJdbcTemplate jdbcTemplate) {
        selectSupport = new SelectSupport(jdbcTemplate);
        updateSupport = new UpdateSupport(jdbcTemplate);
        deleteSupport = new DeleteSupport(jdbcTemplate);
        insertSupport = new InsertSupport(jdbcTemplate);

        try {
            hookJdbcTemplate(jdbcTemplate);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void hookJdbcTemplate(final NamedParameterJdbcTemplate jdbcTemplate) throws Exception{
        Field jdbcTemplateField=jdbcTemplate.getClass().getDeclaredField("classicJdbcTemplate");
        jdbcTemplateField.setAccessible(true);
        final JdbcTemplate actualJdbcTemplate= (JdbcTemplate) jdbcTemplateField.get(jdbcTemplate);
        JdbcOperations proxy= (JdbcOperations) Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[]{JdbcOperations.class}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                long start = System.currentTimeMillis();
                if ("query".equals(method.getName()) && args.length == 3 && args[0] instanceof PreparedStatementCreator) {
                    Object preparedStatementCreator = args[0];
                }
                Object result=method.invoke(actualJdbcTemplate, args);
                System.out.println("execute sql spend ===>" + (System.currentTimeMillis() - start));

                return result;
            }
        });
        jdbcTemplateField.set(jdbcTemplate, proxy);
    }

    public void insertSelective(Object record){
        insertSupport.insertSelective(record);
    }

    public <T> T selectByPrimaryKey(Object primaryKey, Class<T> entityClass) {
        return selectSupport.selectByPrimaryKey(primaryKey, entityClass);
    }



    public <T> int deleteByPrimaryKey(Object primaryKey, Class<T> entityClass) {
        return deleteSupport.deleteByPrimaryKey(primaryKey, entityClass);
    }

    public <T> int updateByPrimaryKeySelective(T record) {
        return updateSupport.updateByPrimaryKeySelective(record);
    }


    public <T> List<T> select(T record) {
        return selectSupport.select(record);
    }

    public <T> T selectOne(T record) {
        return selectSupport.selectOne(record);
    }

    public <T> PageInfo<T> selectByPage(int pageNum, int pageSize, T record) {
        return selectSupport.selectByPage(pageNum, pageSize, record);
    }

    public  List<?> selectByExample(Example example) {
        return selectSupport.selectByExample(example);
    }
}
