package ru.otus.listener.homework;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import ru.otus.listener.Listener;
import ru.otus.model.Message;

public class HistoryListener implements Listener, HistoryReader {

    private final Set<Message> msgSet = new HashSet<>();

    @Override
    public void onUpdated(Message msg) {
        msgSet.add(msg.toBuilder().copy());
    }

    @Override
    public Optional<Message> findMessageById(long id) {
        return msgSet.stream().filter(msg -> id == msg.getId()).findAny();
    }
}
