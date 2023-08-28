package ru.otus.exceptions;

import ru.otus.entities.Nominal;

public class NotSupportedBanknoteException {
    private String title;
    private String details;

    public NotSupportedBanknoteException(Nominal nominal) {
        title = "Купюра не поддерживается банкоматом";
        details = "Купюра " + nominal +"не поддерживается банкоматом";
    }
}
