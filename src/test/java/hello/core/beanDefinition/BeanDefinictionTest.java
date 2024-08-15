package hello.core.beanDefinition;

import hello.core.SpringAppConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


class BeanDefinictionTest {

    AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(SpringAppConfig.class);


    @Test
    @DisplayName("빈 설정 메타정보 확인")
    void find1() {
        String[] beanNames = ac.getBeanDefinitionNames();

        for(String beanName : beanNames) {

            BeanDefinition beanDefinition = ac.getBeanDefinition(beanName);

            if(beanDefinition.getRole() == BeanDefinition.ROLE_APPLICATION) {
                System.out.println("name : " + beanName + " definition : " + beanDefinition);
            }
        }
    }

}
