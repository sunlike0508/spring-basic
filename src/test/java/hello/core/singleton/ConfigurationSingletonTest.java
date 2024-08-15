package hello.core.singleton;

import hello.core.SpringAppConfig;
import hello.core.member.MemberRepository;
import hello.core.member.MemberServiceImpl;
import hello.core.order.OrderServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

class ConfigurationSingletonTest {

    @Test
    @DisplayName("스프링 컨테이너는 객체를 싱글콘으로 관리하는것을 증명")
    void test1() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(SpringAppConfig.class);

        MemberServiceImpl memberService = ac.getBean("memberService", MemberServiceImpl.class);
        OrderServiceImpl orderService = ac.getBean("orderService", OrderServiceImpl.class);
        MemberRepository memberRepository = ac.getBean(MemberRepository.class);

        MemberRepository memberRepository1 = memberService.getMemberRepository();
        MemberRepository memberRepository2 = orderService.getMemberRepository();

        System.out.println(memberRepository1);
        System.out.println(memberRepository2);
        System.out.println(memberRepository);

        Assertions.assertThat(memberRepository).isSameAs(memberRepository1).isSameAs(memberRepository2);
    }


    @Test
    void test2() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(SpringAppConfig.class);

        SpringAppConfig bean = ac.getBean(SpringAppConfig.class);

        System.out.println("bean.getClass() = " + bean.getClass());
    }


}
