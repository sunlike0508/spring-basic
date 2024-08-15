package hello.core.order;

import hello.core.annotation.MainDiscountPolicy;
import hello.core.discount.DiscountPolicy;
import hello.core.member.Member;
import hello.core.member.MemberRepository;
import org.springframework.stereotype.Component;


@Component
//@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final MemberRepository memberRepository;

    /**
     * private final DiscountPolicy discountPolicy = new FixDiscountPolicy();
     * private final DiscountPolicy discountPolicy = new RateDiscountPolicy();
     * 역할과 구현은 충실하게 분리 -> OK
     * 다형성 활용, 인터페이스와 구현 객체 분리 -> OK
     * OCP, DIP 준수했는가? -> NO
     * OrderServiceImpl이 DiscountPolicy 인터페이스만 의존하는줄 알았는데
     * 실제로는 FixDiscountPolicy 또는 RateDiscountPolicy에 의존하는 것이다.
     * 왜? Fix에서 Rate로 정책이 변경되면 결국 OrderServiceImpl은 DiscountPolicy를 초기화하는(인스턴스화) 수정해야한다. (DIP 위반)
     * 즉, 변경하지 말고 확장을 해야하는데 클라이언트(OrderServiceImpl) 코드를 수정해야 하므로 OCP 위반
     */

    /**
     * 누군가 이걸 대신 객체를 생성하고 구체화 해야한다.
     * 관심사의 분리.
     * OrderServiceImpl은 DiscountService가 뭔지 관심 없다. 딱 주문만!!!
     * DiscountService의 역할을 맞게 변경해주는 클래스 생성(구현 객체를 생성)
     * AppConfig 클래스의 탄생 !
     * 이를 통해 OrderServiceImpl은 MemoryMemberRepository, FixDiscountPolicy에 의존하지 않는다.
     * 단지 MemberRepository 인터페이스에만 의존한다.
     * OrderServiceImpl은 생성자를 통해 어떤 구현 객체가 들어오는지 알수가 없다.(알 필요도 없다)
     * 생성자를 통해 들어오는 구현 객체는 오직 외부에서 결정된다.
     * 의존관계에 대한 고민은 외부에 던지고 오로지 나의 실행(비즈니스 로직)에 집중한다!! DIP 준수
     */

    private final DiscountPolicy discountPolicy;


    //@Autowired // 자동 주입(컴포넌트 스캔)을 사용한다면 생략 가능
    public OrderServiceImpl(MemberRepository memberRepository, @MainDiscountPolicy DiscountPolicy discountPolicy) {
        this.memberRepository = memberRepository;
        this.discountPolicy = discountPolicy;
    }


    @Override
    public Order createOrder(Long memberId, String itemName, int itemPrice) {

        Member member = memberRepository.findById(memberId);

        int disCountPrice = discountPolicy.discount(member, itemPrice);

        return new Order(memberId, itemName, itemPrice, disCountPrice);
    }


    public MemberRepository getMemberRepository() {
        return memberRepository;
    }
}
