package hello.core.scope;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;

class PrototypeTest {

    @Test
    void test() {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(PrototypeBean.class);

        System.out.println("find PrototypeBean 1");
        PrototypeBean prototypeBean1 = ac.getBean(PrototypeBean.class);
        System.out.println(prototypeBean1);
        System.out.println("find PrototypeBean 2");
        PrototypeBean prototypeBean2 = ac.getBean(PrototypeBean.class);
        System.out.println(prototypeBean2);

        Assertions.assertThat(prototypeBean1).isNotSameAs(prototypeBean2);

        ac.close();
    }


    @Scope("prototype")
    static class PrototypeBean {

        @PostConstruct
        public void init() {
            System.out.println("PrototypeBean.init");
        }


        @PreDestroy
        public void close() {
            System.out.println("PrototypeBean.close");
        }
    }
}
