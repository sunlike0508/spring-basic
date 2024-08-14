package hello.core;

import hello.core.member.Grade;
import hello.core.member.Member;
import hello.core.member.MemberService;
import hello.core.order.Order;
import hello.core.order.OrderService;

public class OrderApp {

    public static void main(String[] args) {

        // DIP 준수했을 때 실행 코드
        AppConfig appConfig = new AppConfig();

        MemberService memberService = appConfig.memberService();
        OrderService orderService = appConfig.orderService();

        Member member = new Member(1L, "memberA", Grade.VIP);

        memberService.join(member);

        Order order = orderService.createOrder(1L, "itemA", 10000);

        System.out.println(order.toString());

    }

}
