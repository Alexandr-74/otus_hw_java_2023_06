package ru.otus.exceptions;

public class NotSupportedValueException extends Throwable {
    private String title;
    private String details;

    public NotSupportedValueException(int value) {
        title = "Не корректная сумма для выдачи наличных";
        details = "Банкомат не может выдать сумму " + value;
    }
}
