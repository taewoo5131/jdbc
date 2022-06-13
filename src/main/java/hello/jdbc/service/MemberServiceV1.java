package hello.jdbc.service;

import hello.jdbc.domain.Member;
import hello.jdbc.repository.MemberRepositoryV1;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@RequiredArgsConstructor
public class MemberServiceV1 {
    private final MemberRepositoryV1 memberRepository;

    public void accountTransfer(String fromId, String toId, int money) throws SQLException {
        Member fromMember = memberRepository.findById(fromId);
        Member toMember = memberRepository.findById(toId);
        memberRepository.update(fromMember.getMemberId() , fromMember.getMoney() - money);
        if (toMember.getMemberId().equals("ex")) {
            throw new IllegalStateException("이체중 예외 발생 !!!!");
        }
        memberRepository.update(toMember.getMemberId(), toMember.getMoney() + money);

    }
}
