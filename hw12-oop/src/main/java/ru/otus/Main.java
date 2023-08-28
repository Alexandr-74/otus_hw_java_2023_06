package ru.otus;

import ru.otus.entities.ATMImpl;
import ru.otus.entities.Banknote;
import ru.otus.entities.Nominal;
import ru.otus.utils.NominalCounter;
import ru.otus.exceptions.NotEnoughBanknotesException;
import ru.otus.exceptions.NotSupportedValueException;
import ru.otus.interfaces.ATM;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        ATM atm = new ATMImpl(new Nominal[]{Nominal.BANKNOTE5000, Nominal.BANKNOTE2000, Nominal.BANKNOTE1000, Nominal.BANKNOTE500, Nominal.BANKNOTE100});
        System.out.println(atm.checkBalance());

        atm.depositCash(List.of(new Banknote(Nominal.BANKNOTE2000),
                new Banknote(Nominal.BANKNOTE5000),
                new Banknote(Nominal.BANKNOTE500)
                ,new Banknote(Nominal.BANKNOTE100),
                new Banknote(Nominal.BANKNOTE2000)));

        assert atm.checkBalance() == 9600;
        System.out.println("Внесли 9600, в банкомате: " + atm.checkBalance());

        try {
            List<Banknote> babosiki = atm.issueCash(7300);
            System.out.println(babosiki.stream().mapToInt(b-> NominalCounter.nominalToInt(b.nominal())).sum());
        } catch (NotSupportedValueException | NotEnoughBanknotesException e) {
            System.out.println("Ошибка!");
        }
        assert atm.checkBalance() == 9600;
        System.out.println("Попытались снять 7300, но не хватило банкнот, в банкомате: " + atm.checkBalance());

        atm.depositCash(List.of(new Banknote(Nominal.BANKNOTE100),
                new Banknote(Nominal.BANKNOTE100),
                new Banknote(Nominal.BANKNOTE100),
                new Banknote(Nominal.BANKNOTE100),
                new Banknote(Nominal.BANKNOTE100)));

        assert atm.checkBalance() == 10100;
        System.out.println("Внесли 5 купюр по 100, в банкомате: " + atm.checkBalance());

        try {
            List<Banknote> babosiki = atm.issueCash(7300);
            System.out.println("Сняли " + babosiki.stream().mapToInt(b->NominalCounter.nominalToInt(b.nominal())).sum());
        } catch (NotSupportedValueException | NotEnoughBanknotesException e) {
            System.out.println("Ошибка!");
        }

        assert atm.checkBalance() == 10100-7300;

        System.out.println("Снова попытались снять 7300, все ок, сняли, в банкомате осталось: " + atm.checkBalance());
    }
}
