package ru.otus.entities;

import ru.otus.exceptions.NotEnoughBanknotesException;
import ru.otus.interfaces.Cassette;

import java.util.ArrayDeque;
import java.util.List;

public class CassetteImpl implements Cassette {
    private final Nominal nominal;
    private final ArrayDeque<Banknote> banknotes = new ArrayDeque<>();
    CassetteImpl(Nominal nominal) {
        this.nominal = nominal;
    }

    public Nominal getNominal() {
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

    public List<Banknote> checkAllBanknotes() {
        return banknotes.clone().stream().toList();
    }

}
