package hello.core.scan;

import hello.core.AutoAppConfig;
import hello.core.member.MemberService;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

class AutoAppConfigTest {

    @Test
    void test1() {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(AutoAppConfig.class);


        MemberService memberService = ac.getBean(MemberService.class);

        assertThat(memberService).isInstanceOf(MemberService.class);
    }
}
