package hello.core.beanfind;

import hello.core.SpringAppConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


class ApplicationContextInfoTest {

    AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(SpringAppConfig.class);


    @Test
    void findAllBean() {
        String[] beanNames = ac.getBeanDefinitionNames();

        for(String beanName : beanNames) {
            Object bean = ac.getBean(beanName);

            System.out.println("name = " + beanName + " object = " + bean);
        }
    }


    @Test
    void findApplicationBean() {
        String[] beanNames = ac.getBeanDefinitionNames();

        for(String beanName : beanNames) {
            BeanDefinition beanDefinition = ac.getBeanDefinition(beanName);

            if(beanDefinition.getRole() == BeanDefinition.ROLE_APPLICATION) {
                Object bean = ac.getBean(beanName);

                System.out.println("name = " + beanName + " object = " + bean);
            }
        }
    }

}
