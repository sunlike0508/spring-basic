package hello.core.member;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class MemoryMemberRepository implements MemberRepository {

    private static final Map<Long, Member> store = new HashMap<>();
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

