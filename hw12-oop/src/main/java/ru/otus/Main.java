package ru.otus;

import ru.otus.dto.ATMImpl;
import ru.otus.dto.BanknoteImpl;
import ru.otus.exceptions.NotEnoughBanknotesException;
import ru.otus.exceptions.NotSupportedValueException;
import ru.otus.interfaces.ATM;
import ru.otus.interfaces.Banknote;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        ATM atm = new ATMImpl();
        System.out.println(atm.checkBalance());

        atm.depositCash(List.of(new BanknoteImpl("2000"),
                new BanknoteImpl("5000"),
                new BanknoteImpl("500")
                ,new BanknoteImpl("100"),
                new BanknoteImpl("2000")));

        assert atm.checkBalance() == 9600;
        System.out.println("Внесли 9600, в банкомате: " + atm.checkBalance());

        try {
            List<Banknote> babosiki = atm.issueCash("7300");
            System.out.println(babosiki.stream().mapToInt(b->Integer.parseInt(b.nominal())).sum());
        } catch (NotSupportedValueException | NotEnoughBanknotesException e) {
            System.out.println("Ошибка!");
        }
        assert atm.checkBalance() == 9600;
        System.out.println("Попытались снять 7300, но не хватило банкнот, в банкомате: " + atm.checkBalance());

        atm.depositCash(List.of(new BanknoteImpl("100"),
                new BanknoteImpl("100"),
                new BanknoteImpl("100"),
                new BanknoteImpl("100"),
                new BanknoteImpl("100")));

        assert atm.checkBalance() == 10100;
        System.out.println("Внесли 5 купюр по 100, в банкомате: " + atm.checkBalance());

        try {
            List<Banknote> babosiki = atm.issueCash("7300");
            System.out.println("Сняли " + babosiki.stream().mapToInt(b->Integer.parseInt(b.nominal())).sum());
        } catch (NotSupportedValueException | NotEnoughBanknotesException e) {
            System.out.println("Ошибка!");
        }

        assert atm.checkBalance() == 10100-7300;

        System.out.println("Снова попытались снять 7300, все ок, сняли, в банкомате осталось: " + atm.checkBalance());
    }
}
