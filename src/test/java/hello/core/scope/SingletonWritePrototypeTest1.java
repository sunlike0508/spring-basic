package hello.core.scope;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.inject.Provider;
import lombok.RequiredArgsConstructor;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;

class SingletonWritePrototypeTest1 {

    @Test
    void prototypeFind() {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(PrototypeBean.class);

        PrototypeBean prototypeBean1 = ac.getBean(PrototypeBean.class);
        prototypeBean1.addCount();

        assertThat(prototypeBean1.getCount()).isEqualTo(1);

        PrototypeBean prototypeBean2 = ac.getBean(PrototypeBean.class);
        prototypeBean2.addCount();

        assertThat(prototypeBean2.getCount()).isEqualTo(1);
    }


    @Test
    @DisplayName("싱글톤 클라이언트 사용 프로토타입")
    void test2() {
        AnnotationConfigApplicationContext ac =
                new AnnotationConfigApplicationContext(ClientBean.class, PrototypeBean.class);

        ClientBean clientBean1 = ac.getBean(ClientBean.class);

        int count1 = clientBean1.logic();

        assertThat(count1).isEqualTo(1);

        ClientBean clientBean2 = ac.getBean(ClientBean.class);

        int count2 = clientBean2.logic();

        assertThat(count2).isEqualTo(1);
    }


    @Scope("singleton")
    @RequiredArgsConstructor
    static class ClientBean {

        private final Provider<PrototypeBean> prototypeBeanProvider;


        public int logic() {

            PrototypeBean prototypeBean = prototypeBeanProvider.get();

            prototypeBean.addCount();

            return prototypeBean.getCount();
        }
    }


    @Scope("prototype")
    static class PrototypeBean {

        private int count = 0;


        public void addCount() {
            count++;
        }


        public int getCount() {
            return count;
        }


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
