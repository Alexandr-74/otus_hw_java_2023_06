package ru.otus.interfaces;

import ru.otus.exceptions.NotEnoughBanknotesException;
import ru.otus.exceptions.NotSupportedValueException;

import java.math.BigDecimal;
import java.util.List;

public interface ATM {

    List<Banknote> issueCash(String value) throws NotSupportedValueException, NotEnoughBanknotesException;
    List<Banknote> depositCash(List<Banknote> banknotes);
    Long checkBalance();
}
