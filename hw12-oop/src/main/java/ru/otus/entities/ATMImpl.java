package ru.otus.entities;

import ru.otus.exceptions.NotEnoughBanknotesException;
import ru.otus.exceptions.NotSupportedValueException;
import ru.otus.interfaces.ATM;
import ru.otus.interfaces.Cassette;
import ru.otus.utils.NominalCounter;

import java.util.*;

public class ATMImpl implements ATM {
    private final int[] appliedNominals;
    Set<Cassette> cassettes = new HashSet<>();

    public ATMImpl(Nominal[] appliedNominals) {
        cassettes.add(new CassetteImpl(Nominal.BANKNOTE5000));
        cassettes.add(new CassetteImpl(Nominal.BANKNOTE2000));
        cassettes.add(new CassetteImpl(Nominal.BANKNOTE1000));
        cassettes.add(new CassetteImpl(Nominal.BANKNOTE500));
        cassettes.add(new CassetteImpl(Nominal.BANKNOTE100));
        Arrays.sort(appliedNominals);
        this.appliedNominals = Arrays.stream(appliedNominals).map(NominalCounter::nominalToInt).mapToInt(n->n).toArray();
    }


    @Override
    public List<Banknote> issueCash(int value) throws NotSupportedValueException, NotEnoughBanknotesException {
        validateValue(value);

        List<Banknote> result = new ArrayList<>();
        int currentCassetteCount = 0;

        try {
            while (value != 0) {
                int currentCassette = appliedNominals[currentCassetteCount];
                if (value - currentCassette >= 0) {
                    result.add(cassettes
                            .stream()
                            .filter(cassette -> NominalCounter.nominalToInt(cassette.getNominal())==currentCassette)
                            .findAny()
                            .orElseThrow()
                            .getBanknote());
                    value -= currentCassette;
                } else {
                    currentCassetteCount += 1;
                }
            }
        } catch (NotEnoughBanknotesException e) {
            depositCash(result);
            throw e;
        }
        return result;
    }

    public List<Banknote> depositCash(List<Banknote> banknotes) {
        List<Banknote> badBanknotes = new ArrayList<>();
        for (Banknote banknote : banknotes) {
            int currBanknote = NominalCounter.nominalToInt(banknote.nominal());
            if (Arrays.stream(appliedNominals).anyMatch(bn->bn == currBanknote)) {
                cassettes.stream()
                        .filter(cas->cas.getNominal().equals(banknote.nominal()))
                        .findFirst()
                        .orElseThrow()
                        .addBanknote(banknote);
            } else {
                badBanknotes.add(banknote);
            }
        }

        return badBanknotes;
    }

    @Override
    public int checkBalance() {
        int summ = 0;
        for (Cassette cassette : cassettes) {
            summ += cassette.checkAllBanknotes().stream().mapToInt(b->NominalCounter.nominalToInt(b.nominal())).sum();
        }
        return summ;
    }

    private void validateValue(int value) throws NotSupportedValueException {
        if (value % appliedNominals[appliedNominals.length-1] != 0) {
            System.err.println("Не корректная сумма для выдачи наличных");
            throw new NotSupportedValueException(value);
        }
    }
}
