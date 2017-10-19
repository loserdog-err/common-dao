package top.chenat.commondao.interceptor;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;

/**
 * Created by ChenAt on 10/18/17.
 * desc:
 */
public class PageListener implements QueryListener {

    private JdbcTemplate jdbcTemplate;
    public PageListener(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void onQueryInvoke(PreparedStatementCreator psc, PreparedStatementSetter pss, ResultSetExtractor<?> rse) {

    }
}
