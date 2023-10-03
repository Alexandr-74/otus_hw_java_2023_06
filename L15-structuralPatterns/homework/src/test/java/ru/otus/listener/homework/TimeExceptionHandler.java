package ru.otus.listener.homework;

import org.junit.jupiter.api.Test;
import ru.otus.model.Message;
import ru.otus.processor.homework.ExceptionProcessor;

import java.time.OffsetTime;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;

public class TimeExceptionHandler {
    @Test
    void handlerTest() {

        // given
        ExceptionProcessor exceptionProcessor = new ExceptionProcessor();
        var time = OffsetTime.now();
        if (time.getSecond()%2==0 && time.getNano()<999999000) {
            assertThatCode(()->{
                exceptionProcessor.process(new Message.Builder(1).field1("f1").build());
            }).isInstanceOf(IllegalStateException.class);
            System.out.println("thrown");
        } else {
            assertThatCode(() -> {
                exceptionProcessor.process(new Message.Builder(1).field1("f1").build());
            }).doesNotThrowAnyException();
            System.out.println("Ok");
        }
    }

    @Test
    void check() {
        for (int i = 0; i < 1000000; i++) {
            assertThatCode(()->{
                handlerTest();
            }).doesNotThrowAnyException();
        }
    }
}
