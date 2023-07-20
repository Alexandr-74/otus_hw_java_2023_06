package homework;


import java.util.*;

public class CustomerReverseOrder {

    //todo: 2. надо реализовать методы этого класса
    //надо подобрать подходящую структуру данных, тогда решение будет в "две строчки"
    private final ArrayDeque<Customer> set = new ArrayDeque<>();

    public void add(Customer customer) {
        set.add(customer);
    }

    public Customer take() {
        return set.pollLast(); // это "заглушка, чтобы скомилировать"
    }
}
