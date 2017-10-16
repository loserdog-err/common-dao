import top.chenat.commondao.bean.Example;
import top.chenat.commondao.support.DaoSupport;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;

/**
 * Created by ChenAt 2017/10/14.
 * desc:
 */

public class CrudTest {

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
        Student student=studentDao.selectByPrimaryKey(1);
        System.out.println(student);

        student = new Student();
        student.setName("哈哈");
        System.out.println(studentDao.selectOne(student));
    }

    @Test
    public void testDelete() {
        System.out.println(studentDao.deleteByPrimaryKey(5));
    }

    @Test
    public void testUpdate() {
        Student student = new Student();
        student.setId(9);
        student.setName("wqnmlgb");
        student.setSex(99);
        studentDao.updateByPrimaryKeySelective(student);
    }

    @Test
    public void testSelectByPage() {
        Student student = new Student();
        student.setSex(0);
        System.out.println(studentDao.selectByPage(1, 30, student));
    }

    @Test
    public void testHaha() throws IntrospectionException {
        BeanInfo beanInfo = Introspector.getBeanInfo(Student.class);
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor descriptor : propertyDescriptors) {
        }

    }

    @Test
    public void testExample() throws Exception{
        studentDao.selectByPrimaryKey(10);
        Example example = new Example(Student.class);
        example.createCriteria().andEqualTo("sex", 0).andLike("name", "%蛤%");
        example.or(example.createCriteria().andEqualTo("name", "哈哈"));
        System.out.println(studentDao.selectByExample(example));


    }
}
