package hello.core.singleton;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

class StatefulServiceTest {

    @Test
    void test1() {

        ApplicationContext ac = new AnnotationConfigApplicationContext(TestConfig.class);

        StatefulService statefulService1 = ac.getBean(StatefulService.class);

        statefulService1.order("userA", 10000);
        statefulService1.order("userB", 20000);

        int price = statefulService1.getPrice();

        System.out.println(price); // 내가 의도한건 A가 주문한 10000원이 나와야 한다. 그러나 20000만원 나옴. 맴버변수를 같이 쓰기 때문

        Assertions.assertThat(price).isEqualTo(20000);
    }


    @Test
    @DisplayName("클라이언트에서 멀티 객체사용 싱글톤 문제점. 스프링은 객체를 싱글톤으로 관리하기 때문에 사실상 멀티가 아님")
    void test2() {

        ApplicationContext ac = new AnnotationConfigApplicationContext(TestConfig.class);

        StatefulService statefulService1 = ac.getBean(StatefulService.class);
        StatefulService statefulService2 = ac.getBean(StatefulService.class);

        statefulService1.order("userA", 10000);
        statefulService2.order("userB", 20000);

        int price = statefulService1.getPrice();

        System.out.println(price); // 내가 의도한건 A가 주문한 10000원이 나와야 한다. 그러나 20000만원 나옴. 맴버변수를 같이 쓰기 때문

        Assertions.assertThat(price).isEqualTo(20000);
    }


    @Test
    @DisplayName("지역변수 사용안하게 해결")
    void test3() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(TestConfig.class);

        StatelessService statefulService1 = ac.getBean(StatelessService.class);

        int price = statefulService1.order("userA", 10000);
        int price2 = statefulService1.order("userB", 20000);

        System.out.println(price);

        Assertions.assertThat(price).isEqualTo(10000);
        Assertions.assertThat(price2).isEqualTo(20000);
    }


    static class TestConfig {

        @Bean
        public StatefulService statefulService() {
            return new StatefulService();
        }


        @Bean
        public StatelessService statelessService() {
            return new StatelessService();
        }
    }
}