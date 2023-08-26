package ru.otus.interfaces;

public interface Cassette {
    String getNominal();
    void addBanknote(Banknote banknote);
    Banknote getBanknote();
}
