package ru.otus.dto;

import ru.otus.exceptions.NotEnoughBanknotesException;
import ru.otus.interfaces.Banknote;
import ru.otus.interfaces.Cassette;

import java.util.ArrayDeque;

public class CassetteImpl implements Cassette {
    private final String nominal;
    private ArrayDeque<Banknote> banknotes = new ArrayDeque<>();
    CassetteImpl(String nominal) {
        this.nominal = nominal;
    }

    public String getNominal() {
        return nominal;
    }

    public void addBanknote(Banknote banknote) {
            this.banknotes.push(banknote);
    }

    public Banknote getBanknote() {
        if (banknotes.size() == 0) {
            throw new NotEnoughBanknotesException(nominal);
        }
        return this.banknotes.poll();
    }

}
