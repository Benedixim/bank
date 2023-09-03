/**
 * Класс Transaction представляет транзакцию между двумя счетами в банке.
 * Это абстрактный класс, поэтому он не может быть создан напрямую.
 * Подклассы должны обеспечивать реализацию метода print().
 *
 * @version 1.0
 * @since 2023-09-02
 * @author Андрей Колесинский
 */
package org.example.strategy;

import lombok.Data;
import org.example.models.Account;
import org.example.models.Bank;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.Calendar;
import java.util.Map;

@Data
public abstract class Transaction {

    /**
     * идентификатор транзакции.
     */
    protected int id;

    /**
     * Тип транзакции.
     */
    protected String type;

    /**
     * Сумма денег, участвующей в транзакции.
     */
    protected double amount;

    /**
     * Валюта, в которой проходит транзакции.
     */
    protected String currency;

    /**
     * Дата транзакции.
     */
    protected Calendar calendar;

    /**
     * Банк, в котором зарегистрирован первый счет, участвующий в транзакции.
     */
    protected Bank bank1;

    /**
     * Банк, в котором зарегистрирован второй счет, участвующий в транзакции.
     */
    protected Bank bank2;

    /**
     * Первый счет, участвующий в транзакции.
     */
    protected Account account1;

    /**
     * Второй счет, участвующий в транзакции.
     */
    protected Account account2;

    /**
     * Метод должен быть реализован подклассами, чтобы возвращать строковое представление транзакции.
     *
     * @return строковое представление транзакции
     */
    public abstract String print();

    /**
     * Метод записывает информацию о транзакции в файл.
     * @param id идентификатор транзакции, используемый для генерации имени файла
     */
    public void checkToFile(int id) {
        try {
            // создаем файл для чека
            File file = new File("check/check" + id + ".txt");
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }

            // открываем поток для записи в файл
            FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
            BufferedWriter bw = new BufferedWriter(fw);

            // записываем информацию в файл
            bw.write(print());
            bw.newLine(); // добавляем пустую строку для разделения чеков

            // закрываем потоки
            bw.close();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Метод записывает информацию о транзакции в файл.
     * @param amount сумма денег, которую нужно конвертировать в другую валюту
     * @param fromCurrency изначальная валют
     * @param toCurrency конечная валюта
     * @return переконвертирования сумма денег
     */
    public double convertCurrency(double amount, String fromCurrency, String toCurrency) {
        double convertedAmount = 0;
        Yaml yaml = new Yaml();
        InputStream inputStream = this.getClass()
                .getClassLoader()
                .getResourceAsStream("config.yml"); // указываем путь к файлу
        // Читаем данные из файла и преобразуем их в Map<String, Double>
        Map<String, Double> obj = yaml.load(inputStream);

        // Получаем курс валют
        double fromRate = obj.get(fromCurrency);
        double toRate = obj.get(toCurrency);

        // Конвертируем сумму
        convertedAmount = amount * (fromRate / toRate);

        return convertedAmount;
    }
}
