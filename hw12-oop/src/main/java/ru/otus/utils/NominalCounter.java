package ru.otus.utils;

import ru.otus.entities.Nominal;

public class NominalCounter {
    public static int nominalToInt(Nominal nominal) {
        return switch (nominal) {
            case BANKNOTE5000 -> 5000;
            case BANKNOTE2000 -> 2000;
            case BANKNOTE1000 -> 1000;
            case BANKNOTE500 -> 500;
            case BANKNOTE100 -> 100;
        };
    }
}
