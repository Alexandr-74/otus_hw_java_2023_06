package ru.otus.exceptions;

public class NotEnoughBanknotesException extends RuntimeException {
    private String title;
    private String details;

    public NotEnoughBanknotesException(String nominal) {
        title = "Не достаточно купюр в кассете";
        details = "Не достаточно валюты номиналом " + nominal;
    }

}
