package hello.core.singleton;

import hello.core.SpringAppConfig;
import hello.core.member.MemberService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

class SingletonTest {

    @Test
    @DisplayName("스프링 없는 순수한 컨테이너, 서비스를 호추라 할때마다 새로운 객체를 생출")
    void test1() {
        SpringAppConfig springAppConfig = new SpringAppConfig();

        MemberService memberService1 = springAppConfig.memberService();

        MemberService memberService2 = springAppConfig.memberService();

        System.out.println(memberService1);
        System.out.println(memberService2);

        Assertions.assertThat(memberService1).isNotSameAs(memberService2);
        Assertions.assertThat(memberService1).isNotEqualTo(memberService2);

    }


    @Test
    @DisplayName("싱글톤 객체 생성")
    void test2() {
        //SingletonService singletonService = new SingletonService(); // 싱글톤 외부에서 생성 불가

        SingletonService singletonService1 = SingletonService.getInstance();
        SingletonService singletonService2 = SingletonService.getInstance();

        singletonService1.logic();
        singletonService2.logic();

        Assertions.assertThat(singletonService1).isSameAs(singletonService2);
        Assertions.assertThat(singletonService1).isEqualTo(singletonService2);

        // same = 객체 주소값 비교
        // equal = 객체 값 비교
    }


    @Test
    @DisplayName("스피릉 컨테이너와 싱글톤, 스프링 컨테이너는 빈을 싱글톤으로 관리하는 것을 증명")
    void test3() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(SpringAppConfig.class);

        MemberService memberService1 = ac.getBean("memberService", MemberService.class);

        MemberService memberService2 = ac.getBean("memberService", MemberService.class);

        System.out.println(memberService1);
        System.out.println(memberService2);

        Assertions.assertThat(memberService1).isSameAs(memberService2);
    }


}
