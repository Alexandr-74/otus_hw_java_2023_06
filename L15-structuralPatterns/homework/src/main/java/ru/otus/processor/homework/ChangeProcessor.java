package ru.otus.processor.homework;

import ru.otus.model.Message;
import ru.otus.processor.Processor;

@SuppressWarnings("java:S106")
public class ChangeProcessor implements Processor {

    @Override
    public Message process(Message message) {
        String tmp = message.getField11();
        return  message.toBuilder().field11(message.getField12()).field12(tmp).build();
    }
}
