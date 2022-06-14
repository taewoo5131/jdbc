package hello.jdbc.exception.basic;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

@Slf4j
public class CheckedTest {

    @Test
    void checked_catch() {
        Service service = new Service();
        service.callCatch();
    }

    @Test
    void checked_throw(){
        Service service = new Service();
        Assertions.assertThatThrownBy(() -> service.callThrow()).isInstanceOf(MyCheckedException.class);
    }


    /**
     * Exception을 상속받은 예외는 checked Exception이 된다.
     */
    static class MyCheckedException extends Exception{
        public MyCheckedException(String message) {
            super(message);
        }
    }

    static class Service {
        Repository repository = new Repository();

        /**
         * 예외를 잡아서 처리하는 코드
         */
        public void callCatch() {
            try {
                repository.call();
            } catch (MyCheckedException e) {
                // 예외 처리 로직
                log.info("예외처리  >> {} " , e.getMessage());
            }
        }

        /**
         * 예외를 잡지않고 밖으로 던지는 코드
         */
        public void callThrow() throws MyCheckedException{
            repository.call();
        }


    }

    static class Repository {
        public void call() throws MyCheckedException{
            throw new MyCheckedException("ex");
        }
    }
}
