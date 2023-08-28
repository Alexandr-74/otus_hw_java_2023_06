package ru.otus.interfaces;

import ru.otus.entities.Banknote;
import ru.otus.exceptions.NotEnoughBanknotesException;
import ru.otus.exceptions.NotSupportedValueException;

import java.util.List;

public interface ATM {

    List<Banknote> issueCash(int value) throws NotSupportedValueException, NotEnoughBanknotesException;
    List<Banknote> depositCash(List<Banknote> banknotes);
    int checkBalance();
}
