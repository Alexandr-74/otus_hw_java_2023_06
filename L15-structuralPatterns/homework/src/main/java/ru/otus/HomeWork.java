package ru.otus;

import ru.otus.handler.ComplexProcessor;
import ru.otus.listener.ListenerPrinterConsole;
import ru.otus.model.Message;
import ru.otus.processor.homework.ChangeProcessor;
import ru.otus.processor.homework.ExceptionProcessor;

import java.util.List;

public class HomeWork {

    /*
    Реализовать to do:
      1. Добавить поля field11 - field13 (для field13 используйте класс ObjectForMessage)
      2. Сделать процессор, который поменяет местами значения field11 и field12
      3. Сделать процессор, который будет выбрасывать исключение в четную секунду (сделайте тест с гарантированным результатом)
            Секунда должна определяьться во время выполнения.
            Тест - важная часть задания
            Обязательно посмотрите пример к паттерну Мементо!
      4. Сделать Listener для ведения истории (подумайте, как сделать, чтобы сообщения не портились)
         Уже есть заготовка - класс HistoryListener, надо сделать его реализацию
         Для него уже есть тест, убедитесь, что тест проходит
    */

    public static void main(String[] args) {
        var processors =
                List.of(
                        new ChangeProcessor(),
                        new ExceptionProcessor());

        var complexProcessor = new ComplexProcessor(processors, ex -> {
            System.err.println("ex was thrown: " + ex);
        });
        var listenerPrinter = new ListenerPrinterConsole();
        complexProcessor.addListener(listenerPrinter);

        var message =
                new Message.Builder(1L)
                        .field1("field1")
                        .field2("field2")
                        .field3("field3")
                        .field6("field6")
                        .field10("field10")
                        .field11("field11")
                        .field12("field12")
                        .build();
        System.out.println("oldMessage:" + message);
        var result = complexProcessor.handle(message);
        System.out.println("result:" + result);

        complexProcessor.removeListener(listenerPrinter);
    }
}
