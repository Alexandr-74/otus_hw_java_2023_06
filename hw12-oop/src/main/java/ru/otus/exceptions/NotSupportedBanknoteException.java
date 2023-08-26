package ru.otus.exceptions;

public class NotSupportedBanknoteException {
    private String title;
    private String details;

    public NotSupportedBanknoteException(String nominal) {
        title = "Купюра не поддерживается банкоматом";
        details = "Купюра " + nominal +"не поддерживается банкоматом";
    }
}
