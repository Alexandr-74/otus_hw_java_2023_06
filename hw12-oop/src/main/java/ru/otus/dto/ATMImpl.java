package ru.otus.dto;

import ru.otus.exceptions.NotEnoughBanknotesException;
import ru.otus.exceptions.NotSupportedValueException;
import ru.otus.interfaces.ATM;
import ru.otus.interfaces.Banknote;
import ru.otus.interfaces.Cassette;

import java.util.*;

public class ATMImpl implements ATM {
    private final Integer[] appliedNominals = {5000,2000,1000,500,100};
    private long summ = 0;
    Set<Cassette> cassettes = new HashSet<>();

    public ATMImpl() {
        cassettes.add( new CassetteImpl("5000"));
        cassettes.add(new CassetteImpl("2000"));
        cassettes.add(new CassetteImpl("1000"));
        cassettes.add(new CassetteImpl("500"));
        cassettes.add(new CassetteImpl("100"));
        Arrays.sort(appliedNominals);
    }


    @Override
    public List<Banknote> issueCash(String value) throws NotSupportedValueException, NotEnoughBanknotesException {
        long longValue = validateValue(value);

        List<Banknote> result = new ArrayList<>();
        int currentCassetteCount = appliedNominals.length-1;

        try {
            while (longValue != 0) {
                long currentCassette = appliedNominals[currentCassetteCount];
                if (longValue - currentCassette >= 0) {
                    result.add(cassettes
                            .stream()
                            .filter(cassette -> cassette.getNominal().equals(String.valueOf(currentCassette)))
                            .findAny()
                            .orElseThrow()
                            .getBanknote());
                    longValue -= currentCassette;
                    summ-=currentCassette;
                } else {
                    currentCassetteCount -= 1;
                }
            }
        } catch (NotEnoughBanknotesException e) {
            depositCash(result);
            throw e;
        }
        return result;
    }

    private long validateValue(String value) throws NotSupportedValueException {
        long longValue;
        try {
            longValue = Long.parseLong(value);
        } catch (NumberFormatException ex) {
            System.err.println("Не корректная сумма для выдачи наличных");
            throw new NotSupportedValueException(value);
        }

        if (longValue % appliedNominals[0] != 0) {
            System.err.println("Не корректная сумма для выдачи наличных");
            throw new NotSupportedValueException(value);
        }
        return longValue;
    }

    @Override
    public List<Banknote> depositCash(List<Banknote> banknotes) {
        List<Banknote> badBanknotes = new ArrayList<>();
        for (Banknote banknote : banknotes) {
            if (Arrays.stream(appliedNominals).anyMatch(bn->String.valueOf(bn).equals(banknote.nominal()))) {
                cassettes.stream()
                        .filter(cas->cas.getNominal().equals(banknote.nominal()))
                        .findFirst()
                        .orElseThrow()
                        .addBanknote(banknote);
                summ += Long.parseLong(banknote.nominal());
            } else {
                badBanknotes.add(banknote);
            }
        }

        return badBanknotes;
    }

    @Override
    public Long checkBalance() {
        return summ;
    }
}
