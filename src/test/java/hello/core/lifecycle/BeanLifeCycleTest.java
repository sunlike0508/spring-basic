package hello.core.lifecycle;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

class BeanLifeCycleTest {

    @Test
    void lifeCycleTest() {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(LifeCycleConfig.class);

        NetworkClient client = ac.getBean(NetworkClient.class);

        ac.close();

    }


    @Configuration
    static class LifeCycleConfig {

        //@Bean(initMethod = "init", destroyMethod = "close")
        @Bean
        public NetworkClient networkClient() {
            NetworkClient networkClient = new NetworkClient();

            // 객체 생성 및 주입 이후에 일어남
            System.out.println("이건 언제?");
            networkClient.setUrl("http://test");
            System.out.println("이건 언제?22");

            return networkClient;
        }
    }
}