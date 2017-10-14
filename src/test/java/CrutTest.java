import com.chenat.commondao.CommonDao;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.lang.reflect.Field;

/**
 * Created by ChenAt 2017/10/14.
 * desc:
 */

public class CrutTest {

    public NamedParameterJdbcTemplate jdbcTemplate;

    public CommonDao commonDao;


    @Before
    public void setUp() throws Exception {
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        dataSource.setDriverClass("com.mysql.jdbc.Driver");
        dataSource.setJdbcUrl("jdbc:mysql://localhost:3306/test?useUnicode=true&zeroDateTimeBehavior=convertToNull&useSSL=false");
        dataSource.setUser("root");
        dataSource.setPassword("3233006");

        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        commonDao = new CommonDao(jdbcTemplate);
    }

    @Test
    public void test() throws Exception {
        Student student = new Student();
        student.setSex(10);
        student.setName("哈哈");
        StudentDao studentDao = new StudentDao();
        studentDao.setCommonDao(commonDao);
        studentDao.insertSelective(student);

        System.out.println(student);

    }
}
