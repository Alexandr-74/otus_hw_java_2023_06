package homework;


import java.util.*;

public class CustomerService {

    //todo: 3. надо реализовать методы этого класса
    //важно подобрать подходящую Map-у, посмотрите на редко используемые методы, они тут полезны
    private final TreeMap<Customer, String> map = new TreeMap<>((a,b)->(int) (b.getScores()-a.getScores()));
    public Map.Entry<Customer, String> getSmallest() {
        //Возможно, чтобы реализовать этот метод, потребуется посмотреть как Map.Entry сделан в jdk
        Map.Entry<Customer, String> res1 = map.lastEntry();
        return res1 != null
                ? new AbstractMap.SimpleEntry<>(res1.getKey().clone(), res1.getValue())
                : null; // это "заглушка, чтобы скомилировать"
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        Map.Entry<Customer, String> res1 = map.lowerEntry(customer);
        return res1 != null
                ? new AbstractMap.SimpleEntry<>(res1.getKey().clone(), res1.getValue())
                : null; // это "заглушка, чтобы скомилировать"
    }

    public void add(Customer customer, String data) {
        map.put(customer, data);
    }
}
