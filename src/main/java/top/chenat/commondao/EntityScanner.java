package top.chenat.commondao;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ChenAt on 10/17/17.
 * desc:
 */
public class EntityScanner {

    public List<Class<?>> scan(String basePackage) {
        try {
            return findAnnotatedClasses(basePackage);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<Class<?>> findAnnotatedClasses(String scanPackage) throws Exception{
        List<Class<?>> entityClassList = new ArrayList<>();
        ClassPathScanningCandidateComponentProvider provider = createComponentScanner();
        for (BeanDefinition beanDef : provider.findCandidateComponents(scanPackage)) {
            Class<?> clazz=Class.forName(beanDef.getBeanClassName());
            entityClassList.add(clazz);
        }
        return entityClassList;
    }

    private ClassPathScanningCandidateComponentProvider createComponentScanner() {
        // Don't pull default filters (@Component, etc.):
        ClassPathScanningCandidateComponentProvider provider
                = new ClassPathScanningCandidateComponentProvider(false);
        provider.addIncludeFilter(new AnnotationTypeFilter(Entity.class));
        return provider;
    }

//    private void printMetadata(BeanDefinition beanDef) {
//        try {
//            Class<?> cl = Class.forName(beanDef.getBeanClassName());
//            Entity findable = cl.getAnnotation(Entity.class);
//            System.out.printf("Found class: %s, with meta name: %s%n",
//                    cl.getSimpleName(), findable.name());
//        } catch (Exception e) {
//            System.err.println("Got exception: " + e.getMessage());
//        }
//    }
}
