package hello.core.beanfind;

import hello.core.SpringAppConfig;
import hello.core.member.MemberService;
import hello.core.member.MemberServiceImpl;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


class ApplicationContextInfoBasicFindTest {

    AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(SpringAppConfig.class);


    @Test
    void findMemberService() {
        MemberService memberService = ac.getBean("memberService", MemberService.class);

        assertThat(memberService).isInstanceOf(MemberService.class);
    }


    @Test
    void findMemberService2() {
        MemberServiceImpl memberService = ac.getBean("memberService", MemberServiceImpl.class);

        assertThat(memberService).isInstanceOf(MemberServiceImpl.class);
    }


    @Test
    void findMemberService3() {

        Assertions.assertThrows(NoSuchBeanDefinitionException.class, () -> ac.getBean("xxxx", MemberService.class));
    }
}
