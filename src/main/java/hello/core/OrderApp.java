package hello.core;

import hello.core.member.Grade;
import hello.core.member.Member;
import hello.core.member.MemberService;
import hello.core.order.Order;
import hello.core.order.OrderService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class OrderApp {

    public static void main(String[] args) {

        /** DIP 준수했을 때 실행 코드
         AppConfig appConfig = new AppConfig();

         MemberService memberService = appConfig.memberService();
         OrderService orderService = appConfig.orderService();

         Member member = new Member(1L, "memberA", Grade.VIP);

         memberService.join(member);

         Order order = orderService.createOrder(1L, "itemA", 10000);

         System.out.println(order.toString());
         */

        /**
         * ApplicationContext를 스프링 컨테이너라고 한다.
         * SpringAppConfig를 개발자가 직접 객체 생성하고 DI를 했지만 이제 스프링 컨테이너가 해준다.
         * 스프링 컨테이너가 @Configuration이 붙은 클래스에 설정된 정보를 사용하여 @Bean이라 적힌 메서드를 모두 호출해서 반환된 객체를
         * 컨테이너에 등록한다.
         *
         * 이렇게 등록된것을 스프링 빈이라 한다.
         */
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(SpringAppConfig.class);

        // 여기서 "memberService"는 SpringAppConfig에 내가 등록한 메소드명을 말한다.
        MemberService memberService = applicationContext.getBean("memberService", MemberService.class);
        OrderService orderService = applicationContext.getBean("orderService", OrderService.class);

        Member member = new Member(1L, "memberA", Grade.VIP);

        memberService.join(member);

        Order order = orderService.createOrder(1L, "itemA", 10000);

        System.out.println(order.toString());

    }

}
