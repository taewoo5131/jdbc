package hello.jdbc.repository;

import hello.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
//@Transactional
//@Rollback(true)
//@SpringBootTest
class MemberRepositoryV0Test {

    MemberRepositoryV0 repositoryV0 = new MemberRepositoryV0();

    @BeforeEach
    public void beforeEach() {

    }

    @AfterEach
    public void afterEach() {

    }

    @Test
    void crud() throws SQLException {
        // save
        Member member = new Member("leetaewoo6", 10000);
//        repositoryV0.save(member);

        // findById
        Member findMember = repositoryV0.findById(member.getMemberId());
        log.info("findMember= {} " , member);
//        Assertions.assertThat(member).isEqualTo(findMember);

        // update
//        repositoryV0.update(member.getMemberId(), 40000);

        //delete
        repositoryV0.delete(member.getMemberId());
    }
}