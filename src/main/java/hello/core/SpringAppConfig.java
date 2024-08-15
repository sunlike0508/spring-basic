package hello.core;

import hello.core.discount.DiscountPolicy;
import hello.core.discount.RateDiscountPolicy;
import hello.core.member.MemberService;
import hello.core.member.MemberServiceImpl;
import hello.core.member.MemoryMemberRepository;
import hello.core.order.OrderService;
import hello.core.order.OrderServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class SpringAppConfig {

    /**
     * 스프링이 알아서 bean으로 등록해줌
     */

    @Bean
    public MemberService memberService() {
        System.out.println("SpringAppConfig.memberService");
        return new MemberServiceImpl(memberRepository());
    }


    @Bean
    public OrderService orderService() {
        System.out.println("SpringAppConfig.orderService");
        return new OrderServiceImpl(memberRepository(), discountPolicy());
    }


    @Bean
    public MemoryMemberRepository memberRepository() {
        System.out.println("SpringAppConfig.memberRepository");
        return new MemoryMemberRepository();
    }


    @Bean
    public DiscountPolicy discountPolicy() {

        //return new FixDiscountPolicy();

        return new RateDiscountPolicy();
    }
}
