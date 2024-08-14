package hello.core;

import hello.core.member.Grade;
import hello.core.member.Member;
import hello.core.member.MemberService;

public class MemberApp {

    public static void main(String[] args) {
        /** DIP 문제전 실행 코드
         MemberService memberService = new MemberServiceImpl();

         Member member = new Member(1L, "memberA", Grade.VIP);

         memberService.join(member);

         Member findMember = memberService.findMember(1L);
         System.out.println("new member : " + member.getName());
         System.out.println("find member : " + findMember.getName());
         */

        // DIP 준수했을 때 실행 코드
        AppConfig appConfig = new AppConfig();

        MemberService memberService = appConfig.memberService();

        Member member = new Member(1L, "memberA", Grade.VIP);

        memberService.join(member);

        Member findMember = memberService.findMember(1L);
        System.out.println("new member : " + member.getName());
        System.out.println("find member : " + findMember.getName());

    }

}
