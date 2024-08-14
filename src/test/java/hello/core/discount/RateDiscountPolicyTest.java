package hello.core.discount;

import hello.core.member.Grade;
import hello.core.member.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RateDiscountPolicyTest {

    RateDiscountPolicy rateDiscountPolicy = new RateDiscountPolicy();


    @Test
    @DisplayName("VIP는 할인 금액이 1000원이어야 한다.")
    void discount() {
        Member member = new Member(1L, "memberVIP", Grade.VIP);

        int discountPrice = rateDiscountPolicy.discount(member, 10000);

        Assertions.assertThat(discountPrice).isEqualTo(1000);
    }


    @Test
    @DisplayName("VIP가 아니면 할인 금액은 없다.")
    void discount_zero() {
        Member member = new Member(1L, "memberVIP", Grade.BASIC);

        int discountPrice = rateDiscountPolicy.discount(member, 10000);

        Assertions.assertThat(discountPrice).isEqualTo(0);
    }
}