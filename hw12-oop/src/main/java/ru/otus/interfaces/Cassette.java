package ru.otus.interfaces;

import ru.otus.entities.Banknote;
import ru.otus.entities.Nominal;

import java.util.List;

public interface Cassette {
    Nominal getNominal();
    void addBanknote(Banknote banknote);
    Banknote getBanknote();
    List<Banknote> checkAllBanknotes();
}
