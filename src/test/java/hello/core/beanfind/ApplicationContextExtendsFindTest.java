package hello.core.beanfind;

import java.util.Map;
import hello.core.discount.DiscountPolicy;
import hello.core.discount.FixDiscountPolicy;
import hello.core.discount.RateDiscountPolicy;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


class ApplicationContextExtendsFindTest {

    AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(TestConfig.class);


    @Test
    @DisplayName("부모 타입으로 조회시, 자식이 둘 이상 있으면, 중복 오류가 발생")
    void findBean() {
        Assertions.assertThrows(NoSuchBeanDefinitionException.class, () -> ac.getBean(DiscountPolicy.class));
    }


    @Test
    @DisplayName("부모 타입으로 조회시, 자식이 둘 이상 있으면, 빈 이름을 지정해라")
    void findBean2() {

        DiscountPolicy rateDiscountPolicy = ac.getBean("rateDiscountPolicy", DiscountPolicy.class);

        assertThat(rateDiscountPolicy).isInstanceOf(DiscountPolicy.class);
    }


    @Test
    @DisplayName("특정 타입으로 조회, 근데 좋지 않은 방법. 구현객체에 의존하니까")
    void findBean3() {

        RateDiscountPolicy rateDiscountPolicy = ac.getBean(RateDiscountPolicy.class);

        assertThat(rateDiscountPolicy).isInstanceOf(RateDiscountPolicy.class);
    }


    @Test
    @DisplayName("부모 타입으로 모두 조회")
    void find4() {
        Map<String, DiscountPolicy> beanOfType = ac.getBeansOfType(DiscountPolicy.class);

        assertThat(beanOfType.size()).isEqualTo(2);

        for(String key : beanOfType.keySet()) {
            System.out.println("key : " + key + " value : " + beanOfType.get(key));
        }
    }


    @Test
    @DisplayName("부모 객체로 모두 조회")
    void find5() {
        Map<String, Object> beanOfType = ac.getBeansOfType(Object.class);

        for(String key : beanOfType.keySet()) {
            System.out.println("key : " + key + " value : " + beanOfType.get(key));
        }
    }


    @Configuration
    static class TestConfig {

        @Bean
        public DiscountPolicy fixDiscountPolicy() {
            return new FixDiscountPolicy();
        }


        @Bean
        public DiscountPolicy rateDiscountPolicy() {
            return new RateDiscountPolicy();
        }
    }

}
