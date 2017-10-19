package top.chenat.commondao.interceptor;

import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;

/**
 * Created by ChenAt on 10/18/17.
 * desc:jdbcTemplate query 时候的回调
 */
public interface QueryListener {

    void onQueryInvoke(PreparedStatementCreator psc, final PreparedStatementSetter pss, final ResultSetExtractor<?> rse);

}
