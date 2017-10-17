package top.chenat.commondao.paging;

import org.springframework.cglib.proxy.*;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.util.ReflectionUtils;
import top.chenat.commondao.utils.SqlHelper;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;

/**
 * Created by ChenAt 2017/10/16.
 * desc:
 */
public class QueryInterceptor implements MethodInterceptor,CallbackFilter {
    private static  CountSqlParser countSqlParser= new CountSqlParser();
    private JdbcTemplate jdbcTemplate;

    public QueryInterceptor(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public static void intercept(final NamedParameterJdbcTemplate jdbcTemplate) throws Exception {
        JdbcTemplate innerJdbcTemplate=(JdbcTemplate) jdbcTemplate.getJdbcOperations();
        DataSource dataSource = (innerJdbcTemplate.getDataSource());
        QueryInterceptor queryInterceptor = new QueryInterceptor(innerJdbcTemplate);

        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(JdbcTemplate.class);
        Callback noOpCb= NoOp.INSTANCE;
        Callback[] callbacks = new Callback[]{noOpCb, queryInterceptor};
        enhancer.setCallbacks(callbacks);
        enhancer.setCallbackFilter(queryInterceptor);
        JdbcTemplate proxy= (JdbcTemplate) enhancer.create(new Class[]{DataSource.class},new Object[]{dataSource});

        Field jdbcTemplateField=jdbcTemplate.getClass().getDeclaredField("classicJdbcTemplate");
        jdbcTemplateField.setAccessible(true);
        jdbcTemplateField.set(jdbcTemplate, proxy);
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        long start = System.currentTimeMillis();
        PreparedStatementCreator psc= (PreparedStatementCreator) args[0];
        String originSql=getSql(psc);
        Object[] parameters = getParameters(psc);
        Page page=PageHelper.getLocalPage();
        if (page == null) {
            return methodProxy.invokeSuper(obj, args);
        }

        if (page.isCount()) {
            String countSql=countSqlParser.getSimpleCountSql(originSql);
            //查询 count
            Integer count = this.jdbcTemplate.queryForObject(countSql,parameters, Integer.class);
            page.setTotalCount(count);
            System.out.println("countSql===> " + countSql + "   <======= result :" + count);
        }
        String pageSql=SqlHelper.wrapPageSql(originSql, (page.getPageNum() - 1) * page.getPageSize(), page.getPageSize());
        replaceSql(pageSql, psc);
        Object result= methodProxy.invokeSuper(obj, args);
        if (result instanceof Collection) {
            result = PageHelper.afterPage(result);
        }
        PageHelper.clearPage();
        return result;
    }

    private void replaceSql(String pageSql, PreparedStatementCreator psc) {
        Field sqlField = null;
        sqlField=ReflectionUtils.findField(psc.getClass(), "actualSql");
        if (sqlField == null) {
            sqlField = ReflectionUtils.findField(String.class, "sql");
            if (sqlField == null) {
                return;
            }
        }
        sqlField.setAccessible(true);
        ReflectionUtils.setField(sqlField, psc, pageSql);
    }

    private Object[] getParameters(PreparedStatementCreator psc) {
        try {
            Field field=psc.getClass().getDeclaredField("parameters");
            if (field == null) {
                return null;
            }
            field.setAccessible(true);
            List<?> parameters= (List<?>) field.get(psc);
            if (parameters != null && parameters.size() > 0) {
                return parameters.toArray();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    private  String getSql(Object sqlProvider) {
        if (sqlProvider instanceof SqlProvider) {
            return ((SqlProvider) sqlProvider).getSql();
        }
        else {
            return null;
        }
    }


    @Override
    public int accept(Method method) {
        if ("query".equals(method.getName())) {
            Class<?>[] parameterTypes = method.getParameterTypes();
            if (parameterTypes.length == 3) {
                if (parameterTypes[0] == PreparedStatementCreator.class && parameterTypes[1] == PreparedStatementSetter.class && parameterTypes[2] == ResultSetExtractor.class) {
                    return 1;
                }
            }
        }
        return 0;
    }
}
