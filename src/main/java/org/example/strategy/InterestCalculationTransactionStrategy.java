/**
 * Класс InterestCalculationTransactionStrategy представляет стратегию транзакции для операции пополнения счета.
 * Расширяет класс Transaction и реализует интерфейс TransactionStrategy.
 *
 * @version 1.0
 * @since 2023-09-02
 * @author Андрей Колесинский
 */
package org.example.strategy;

import org.example.database.DatabaseHandler;
import org.example.models.Account;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class InterestCalculationTransactionStrategy extends Transaction implements TransactionStrategy {

    ScheduledExecutorService executor;

    /**
     * Конструктор класса InterestCalculationTransactionStrategy.
     * Создает новый объект InterestCalculationTransactionStrategy с заданными параметрами.
     * @param account объект Account, на который будет зачислена сумма транзакции
     */
    public InterestCalculationTransactionStrategy(Account account) {
        this.setType("Int.Cal.");
        this.setAccount1(account);
        this.setAccount2(account);
        this.setAmount(0);
        this.setCurrency(account1.getCurrency());
        this.setCalendar(Calendar.getInstance());

        DatabaseHandler dbHandler = new DatabaseHandler();
        this.setBank1(dbHandler.getBank(account.getBank_id()));
        this.setBank2(this.getBank1());

        this.setId(dbHandler.getNewTransactionId());
    }

    /**
     * Метод execute() выполняет фоновую проверку и далее операцию начисления процентов на счет, обновляет баланс счета в базе данных и создает новую транзакцию.
     * @return true, если операция выполнена успешно, false в противном случае
     */
    @Override
    public boolean execute() {

        boolean success = true;

        try {
            this.executor = Executors.newSingleThreadScheduledExecutor();
            Calendar lastInterestCal = Calendar.getInstance();
            lastInterestCal.setTime(account1.getLastInterestDate());

            this.executor.scheduleAtFixedRate(() -> {
                Date now = new Date();
                Calendar nowCalendar = Calendar.getInstance();
                nowCalendar.setTime(now);
                int percent = getPercent();

                if (nowCalendar.get(Calendar.MONTH) != lastInterestCal.get(Calendar.MONTH)) {

                    this.setAmount(account1.getBalance()*percent/100);
                    this.getAccount1().setBalance(account1.getBalance() + this.getAmount());

                }
            }, 0, 30, TimeUnit.SECONDS);
        } catch (Exception e) {
            success = false;
        }
        return success;
    }

    /**
     * Метод получает значение процента из файла YML
     */
    public int getPercent(){
        Yaml yaml = new Yaml();
        InputStream inputStream = this.getClass()
                .getClassLoader()
                .getResourceAsStream("config.yml"); // указываем путь к файлу


        Map<String, Object> obj = yaml.load(inputStream);// читаем файл и получаем объект Map


        return (int) obj.get("percent");// достаем значение по ключу "percent"
    }

    /**
     * Метод сохраняет изменения в базе данных
     */
    @Override
    public void safe() {
        DatabaseHandler dbHanler = new DatabaseHandler();
        dbHanler.updateAccountBalance(account1.getId(), account1.getBalance());
        dbHanler.updateAccountLastInterestDate(account1.getId());
        dbHanler.createNewTransaction(this);
    }

    /**
     * Метод stop() останавливает выполнение асинхронной проверки на необходимость начисления процентов.
     */
    public void stop(){
        executor.shutdown();
        try {
            if (!executor.awaitTermination(800, TimeUnit.MILLISECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
        }
    }

    /**
     * Метод print() формирует строку с информацией о транзакции в виде банковского чека.
     * @return строка с информацией о транзакции в виде банковского чека
     */
    @Override
    public String print() {
        return String.format("\n" + "-".repeat(44) + "\n" + "| " + " ".repeat(13) + "Банковский чек" +  " ".repeat(13) + " |\n" + "| %-4s %35s |\n", "Чек:",  this.getId()) +
                String.format("| %-10s %29s |\n", this.getCalendar().get(Calendar.DAY_OF_MONTH) + "-" + (this.getCalendar().get(Calendar.MONTH)+1) + "-" + this.getCalendar().get(Calendar.YEAR), this.getCalendar().get(Calendar.HOUR_OF_DAY) + ":" + this.getCalendar().get(Calendar.MINUTE) + ":" + this.getCalendar().get(Calendar.SECOND)) +
                String.format("| %-15s %24s |\n", "Тип транзакции:", this.getType()) +
                String.format("| %-5s %34s |\n",  "Банк:", this.getBank1().getName()) +
                String.format("| %-5s %34s |\n",  "Счет:", this.getAccount1().getName()) +
                String.format("| %-6s %29.2f %3s |\n",  "Сумма:", this.getAmount(), this.getCurrency()) +
                "|"+ "-".repeat(42) +"|\n";
    }
}
