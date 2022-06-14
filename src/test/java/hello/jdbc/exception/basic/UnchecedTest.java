package hello.jdbc.exception.basic;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

@Slf4j
public class UnchecedTest {
    /**
     * RuntimeExcetpion을 상속받은 예외는 언체크가 된다.
     */
    static class MyUncheckedException extends RuntimeException{
        public MyUncheckedException(String message) {
            super(message);
        }
    }

    @Test
    void unchecked_catch() {
        Service service = new Service();
        service.callCatch();
    }

    @Test
    void unchecked_throw() {
        Service service = new Service();
        Assertions.assertThatThrownBy(() -> service.callThrow()).isInstanceOf(MyUncheckedException.class);
    }


    static class Service {
        Repository repository = new Repository();

        void callCatch() {
            try {
                repository.call();
            } catch (MyUncheckedException e) {
                log.info("예외를 잡았음 ~ {}" , e.getMessage() , e);
            }
        }

        void callThrow() {
            repository.call();
        }
    }

    static class Repository {
        void call() {
            throw new MyUncheckedException("ex");
        }

    }
}
