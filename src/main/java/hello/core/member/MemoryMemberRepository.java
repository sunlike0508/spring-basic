package hello.core.member;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

public class MemoryMemberRepository implements MemberRepository{

    private static Map<Long, Member> store = new HashMap<>();
    // 실무에서는 동시성 문제로 ConcurrentMap 이걸 씀

    @Override
    public void save(Member member) {
        store.put(member.getId(), member);
    }


    @Override
    public Member findById(Long memberId) {
        return store.get(memberId);
    }
}

