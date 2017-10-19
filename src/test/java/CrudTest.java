import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import top.chenat.bean.Student;
import top.chenat.commondao.bean.Example;
import top.chenat.commondao.paging.PageHelper;
import top.chenat.commondao.paging.PageInfo;
import top.chenat.commondao.support.DaoSupport;

import javax.persistence.Entity;
import java.util.List;

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
        daoSupport = new DaoSupport(jdbcTemplate,"top.chenat.commondao");
        daoSupport.setSqlWarningTime(100);
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
        PageHelper.startPage(11, 2);
        Example example = new Example(Student.class);
        example.createCriteria();

        List<Student> studentList = studentDao.selectByExample(example);
        System.out.println(new PageInfo<>(studentList));

//        student = new top.chenat.bean.Student();
//        student.setName("哈哈");
//        System.out.println(studentDao.selectOne(student));
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
    public void testExample() throws Exception {
        studentDao.selectByPrimaryKey(10);
        Example example = new Example(Student.class);
        example.createCriteria().andEqualTo("sex", 0).andLike("name", "%蛤%");
        example.or(example.createCriteria().andEqualTo("name", "哈哈"));
        System.out.println(studentDao.selectByExample(example));
    }

    @Test
    public void testHaha() {

        new CrudTest().findAnnotatedClasses("*");
    }

    public void findAnnotatedClasses(String scanPackage) {
        ClassPathScanningCandidateComponentProvider provider = createComponentScanner();
        for (BeanDefinition beanDef : provider.findCandidateComponents(scanPackage)) {
            printMetadata(beanDef);
        }
    }

    private ClassPathScanningCandidateComponentProvider createComponentScanner() {
        // Don't pull default filters (@Component, etc.):
        ClassPathScanningCandidateComponentProvider provider
                = new ClassPathScanningCandidateComponentProvider(false);
        provider.addIncludeFilter(new AnnotationTypeFilter(Entity.class));
        return provider;
    }

    private void printMetadata(BeanDefinition beanDef) {
        try {
            Class<?> cl = Class.forName(beanDef.getBeanClassName());
            Entity findable = cl.getAnnotation(Entity.class);
            System.out.printf("Found class: %s, with meta name: %s%n",
                    cl.getSimpleName(), findable.name());
        } catch (Exception e) {
            System.err.println("Got exception: " + e.getMessage());
        }
    }
}
