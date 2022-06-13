package hello.jdbc.service;

import hello.jdbc.domain.Member;
import hello.jdbc.repository.MemberRepositoryV1;
import hello.jdbc.repository.MemberRepositoryV2;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * 트랜잭션 - 파라미터 연동 , 풀을 고려한 종료
 */
@RequiredArgsConstructor
public class MemberServiceV2 {
    private final DataSource dataSource;
    private final MemberRepositoryV2 memberRepository;

    public void accountTransfer(String fromId, String toId, int money) throws SQLException {

        Connection connection = dataSource.getConnection();

        try {
            connection.setAutoCommit(false);
            // 비즈니스 로직
            bizLogic(fromId, toId, money, connection);

            connection.commit();
        } catch (Exception e) {
            connection.rollback();
            throw new IllegalStateException(e);
        } finally {
            if (connection != null) {
                try {
                    connection.setAutoCommit(true); // pool 에 반납 전 초기화
                    connection.close();
                } catch (Exception e) {
                    throw e;
                }
            }
        }

    }

    private void bizLogic(String fromId, String toId, int money, Connection connection) throws SQLException {
        Member fromMember = memberRepository.findById(fromId, connection);
        Member toMember = memberRepository.findById(toId, connection);
        memberRepository.update(fromMember.getMemberId() , fromMember.getMoney() - money , connection);
        if (toMember.getMemberId().equals("ex")) {
            throw new IllegalStateException("이체중 예외 발생 !!!!");
        }
        memberRepository.update(toMember.getMemberId(), toMember.getMoney() + money , connection);
    }
}
