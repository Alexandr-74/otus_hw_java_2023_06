package ru.otus.dto;

import ru.otus.interfaces.Banknote;

public record BanknoteImpl(String nominal) implements Banknote {
}
