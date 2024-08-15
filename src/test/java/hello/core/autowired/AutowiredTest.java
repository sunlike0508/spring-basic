package hello.core.autowired;

import java.util.Optional;
import hello.core.member.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.lang.Nullable;

class AutowiredTest {

    @Test
    @DisplayName("Autowired 옵션 처리")
    void test1() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(TestBean.class);

        //TestBean testBean = ac.getBean("testBean", TestBean.class);
    }


    static class TestBean {

        // Member가 존재 하지 않으면 실행자체를 안함.
        @Autowired(required = false)
        public void setBean(Member member) {

            System.out.println("member1 = " + member);
        }


        // 호출은 되는데 null이 들어감
        @Autowired
        public void setBean2(@Nullable Member member) {

            System.out.println("member2 = " + member);
        }


        // 호출은 되는데 Optional.empty이 들어감
        @Autowired
        public void setBean3(Optional<Member> member) {

            System.out.println("member3 = " + member);
        }
    }
}
