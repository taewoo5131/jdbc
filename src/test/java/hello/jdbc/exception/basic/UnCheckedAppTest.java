package hello.jdbc.exception.basic;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.ConnectException;
import java.sql.SQLException;

@Slf4j
public class UnCheckedAppTest {

    @Test
    void checked() throws SQLException, ConnectException {
        Controller controller = new Controller();
        Assertions.assertThatThrownBy(() -> {
            controller.request();
        }).isInstanceOf(RuntimeException.class);
    }

    static class Controller {
        Service service = new Service();

        public void request() throws SQLException, ConnectException {
            service.logic();
        }
    }

    @Test
    void printEx() {
        Controller controller = new Controller();
        try {
            controller.request();
        } catch (Exception e) {
            log.info("ex > ", e);
        }
    }

    static class Service {
        Repository repository = new Repository();
        NetworkClient networkClient = new NetworkClient();

        public void logic() throws SQLException, ConnectException {
            repository.call();
            networkClient.call();
        }
    }

    /**
     * 그냥 언체크예외를 던짐 ㅋㅋ
     */
    static class NetworkClient {
        public void call(){
            throw new RuntimeConnectException("연결 실패");
        }
    }

    /**
     * 체크예외를 언체크예외로 전환
     */
    static class Repository {
        public void call(){
            try {
                runSQL();
            } catch (SQLException e) {
                log.info("message = {}", "message입니다~" , e);
                throw new RuntimeSQLException(e);
            }
        }

        public void runSQL() throws SQLException{
            throw new SQLException("ex");
        }
    }

    static class RuntimeConnectException extends RuntimeException {
        public RuntimeConnectException(String message) {
            super(message);
        }
    }

    static class RuntimeSQLException extends RuntimeException {
        public RuntimeSQLException(Throwable cause) {
            super(cause);
        }
    }

}
