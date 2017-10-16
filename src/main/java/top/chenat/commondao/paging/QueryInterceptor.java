package top.chenat.commondao.paging;

import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by ChenAt 2017/10/16.
 * desc:
 */
public class QueryInterceptor {
    private static  CountSqlParser countSqlParser= new CountSqlParser();


    public static void intercept(final NamedParameterJdbcTemplate jdbcTemplate) throws Exception {
//        Field jdbcTemplateField=jdbcTemplate.getClass().getDeclaredField("classicJdbcTemplate");
//        jdbcTemplateField.setAccessible(true);
//        final JdbcTemplate actualJdbcTemplate= (JdbcTemplate) jdbcTemplateField.get(jdbcTemplate);
//        JdbcOperations proxy= (JdbcOperations) Proxy.newProxyInstance(QueryInterceptor.class.getClassLoader(), new Class[]{JdbcOperations.class}, new InvocationHandler() {
//            @Override
//            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
//                long start = System.currentTimeMillis();
//                Page page=PageHelper.getLocalPage();
//                if (page == null) {
//                    return method.invoke(actualJdbcTemplate, args);
//                }
//
//                if (method.getName().startsWith("query")) {
//                    if (args[0] instanceof String) {
//                        String originSql = (String) args[0];
//                        if (page.isCount()) {
//                            String countSql=countSqlParser.getSimpleCountSql(originSql);
//                            //查询 count
//                            Integer count = jdbcTemplate.queryForObject(countSql, parameterMap, Integer.class);
//                        }
//                    }
//                }
//                Object result=method.invoke(actualJdbcTemplate, args);
//                System.out.println("execute sql spend ===>" + (System.currentTimeMillis() - start));
//
//                return result;
//            }
//        });
//        jdbcTemplateField.set(jdbcTemplate, proxy);
    }
}
