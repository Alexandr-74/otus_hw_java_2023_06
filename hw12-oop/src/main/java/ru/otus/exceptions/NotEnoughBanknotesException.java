package ru.otus.exceptions;

import ru.otus.entities.Nominal;

public class NotEnoughBanknotesException extends RuntimeException {
    private String title;
    private String details;

    public NotEnoughBanknotesException(Nominal nominal) {
        title = "Не достаточно купюр в кассете";
        details = "Не достаточно валюты номиналом " + nominal;
    }

}
