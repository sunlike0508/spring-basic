package hello.core;

import hello.core.discount.DiscountPolicy;
import hello.core.discount.RateDiscountPolicy;
import hello.core.member.MemberService;
import hello.core.member.MemberServiceImpl;
import hello.core.member.MemoryMemberRepository;
import hello.core.order.OrderService;
import hello.core.order.OrderServiceImpl;


public class AppConfig {


    /**
     * 애플리케이션이 실제 동작에 필요한 구현 객체를 생성 한다.
     * 생성한 객체 인스턴스의 참조(래퍼런스)를 생성자를 통해서 주입(연결)한다.
     * <p>
     * SRP 단일 책임 원칙
     * 클라이언트 객체는 직접구현 객체를 생성하고 연결하고 실행하는 책임을 가지고 있었으나
     * 이제는 구현객체 생성 및 연결 -> Appconfig가 담당
     * 구현객체 실행 -> OrderServiceImple가 담당
     * --> 책임 분리
     */

    public MemberService memberService() {
        return new MemberServiceImpl(memberRepository());
    }


    public OrderService orderService() {
        /**
         * DIP
         * 클라이언트가 구체회 클래스에 의존하지 않음. 인터페이스만 바로보면 된다.
         * 여기서 객체 인스턴스를 클라이언트 대신 여기서 구체화
         */
        return new OrderServiceImpl(memberRepository(), discountPolicy());
    }


    /**
     * 중복 제거 : 다음에 DB가 변경되는 경우 여기만 바꾸면 된다. (ex: Mysql -> oracle)
     */
    private MemoryMemberRepository memberRepository() {
        return new MemoryMemberRepository();
    }


    private DiscountPolicy discountPolicy() {
        /**
         * 정책이 변경되면 클라이언트 코드(OrderServiceImpl)를 변경할 필요 X
         * 여기만 변경하면 된다. (OCP)
         */
        //return new FixDiscountPolicy();

        return new RateDiscountPolicy();
    }
}
