package hello.core.beanfind;

import java.util.Map;
import hello.core.member.MemberRepository;
import hello.core.member.MemoryMemberRepository;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


class ApplicationContextInfoSameBeanTest {

    AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(SameBeanConfig.class);


    @Test
    @DisplayName("타입으로 조회시 같은 타입이 둘 이상이면, 중복 오류 발생")
    void findDuplicatedBean() {

        assertThrows(NoUniqueBeanDefinitionException.class, () -> ac.getBean(MemberRepository.class));
    }


    @Test
    @DisplayName("타입으로 조회시 같은 타입이 둘 이상이면, 빈 이름으로 지정하면 된다")
    void findNotDuplicatedBean() {

        MemberRepository memberRepository = ac.getBean("memberRepository1", MemberRepository.class);

        assertThat(memberRepository).isInstanceOf(MemberRepository.class);
    }


    @Test
    @DisplayName("타입으로 조회시 같은 타입이 둘 이상이면, 빈 이름으로 지정하면 된다")
    void findAllBeanByType() {

        Map<String, MemberRepository> beanofType = ac.getBeansOfType(MemberRepository.class);

        for(String key : beanofType.keySet()) {
            System.out.println("key : " + key + " value : " + beanofType.get(key));
        }

        System.out.println("beanOfType : " + beanofType);

        assertThat(beanofType.size()).isEqualTo(2);
    }


    @Configuration
    static class SameBeanConfig {

        @Bean
        public MemberRepository memberRepository1() {
            return new MemoryMemberRepository();
        }


        @Bean
        public MemberRepository memberRepository2() {
            return new MemoryMemberRepository();
        }
    }

}
