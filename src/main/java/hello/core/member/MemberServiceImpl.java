package hello.core.member;

public class MemberServiceImpl implements MemberService {

    /**
     * private final MemberRepository memberRepository = new MemoryMemberRepository();
     * // 추상화, 구현 모두 의존하고 있다. DIP를 어기고 있다.
     */

    private final MemberRepository memberRepository;


    public MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }


    @Override
    public void join(Member member) {
        memberRepository.save(member);
    }


    @Override
    public Member findMember(Long memberId) {
        return memberRepository.findById(memberId);
    }
}
