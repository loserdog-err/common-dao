import com.chenat.commondao.daosupport.DaoSupport;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

/**
 * Created by ChenAt 2017/10/14.
 * desc:
 */

public class CrutTest {

    public NamedParameterJdbcTemplate jdbcTemplate;

    public DaoSupport daoSupport;

    public StudentDao studentDao;


    @Before
    public void setUp() throws Exception {
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        dataSource.setDriverClass("com.mysql.jdbc.Driver");
        dataSource.setJdbcUrl("jdbc:mysql://localhost:3306/test?useUnicode=true&zeroDateTimeBehavior=convertToNull&useSSL=false");
        dataSource.setUser("root");
        dataSource.setPassword("3233006");

        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        daoSupport = new DaoSupport(jdbcTemplate);
        studentDao = new StudentDao();
        studentDao.setDaoSupport(daoSupport);
    }

    @Test
    public void testInsert() throws Exception {
        Student student = new Student();
        student.setSex(10);
        student.setName("哈哈");
        studentDao.insertSelective(student);
        System.out.println(student);
    }

    @Test
    public void testSelect() {
        Student student=studentDao.selectByPrimaryKey(9);
        System.out.println(student);
    }

    @Test
    public void testDelete() {
        System.out.println(studentDao.deleteByPrimaryKey(9));
    }

    @Test
    public void testUpdate() {
        Student student = new Student();
        student.setId(9);
        student.setName("wqnmlgb");
        student.setSex(99);
        studentDao.updateByPrimaryKeySelective(student);
    }
}
