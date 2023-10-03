package ru.otus.processor.homework;

import ru.otus.model.Message;
import ru.otus.processor.Processor;

import java.time.OffsetTime;

public class ExceptionProcessor implements Processor {
    @Override
    public Message process(Message message) {
        var currentSecond = OffsetTime.now().getSecond();
        System.out.println("currentSecond is " + currentSecond);
        if (currentSecond%2==0) {
            throw new IllegalStateException();
        }

        return message;
    }
}
