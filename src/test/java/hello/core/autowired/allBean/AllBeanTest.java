package hello.core.autowired.allBean;

import java.util.List;
import java.util.Map;
import hello.core.AutoAppConfig;
import hello.core.discount.DiscountPolicy;
import hello.core.member.Grade;
import hello.core.member.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

class AllBeanTest {

    @Test
    @DisplayName("map으로 모든 빈 조회")
    void test1() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(AutoAppConfig.class, DiscountService.class);

        DiscountService discountService = ac.getBean(DiscountService.class);

        Member member = new Member(1L, "memberA", Grade.VIP);

        int discountPrice = discountService.discount(member, 10000, "fixDiscountPolicy");

        Assertions.assertThat(discountPrice).isEqualTo(1000);

        int discountPrice2 = discountService.discount(member, 20000, "rateDiscountPolicy");

        Assertions.assertThat(discountPrice2).isEqualTo(2000);
    }


    static class DiscountService {

        private final Map<String, DiscountPolicy> policyMap;
        private final List<DiscountPolicy> policyList;


        public DiscountService(Map<String, DiscountPolicy> policyMap, List<DiscountPolicy> policyList) {
            this.policyMap = policyMap;
            this.policyList = policyList;

            System.out.println("policyMap = " + policyMap);
            System.out.println("policyList = " + policyList);
        }


        public int discount(Member member, int price, String discountCode) {

            DiscountPolicy discountPolicy = policyMap.get(discountCode);

            return discountPolicy.discount(member, price);
        }
    }
}
