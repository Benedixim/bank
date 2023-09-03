/**
 * Класс DepositTransactionStrategy представляет стратегию транзакции для операции пополнения счета.
 * Расширяет класс Transaction и реализует интерфейс TransactionStrategy.
 *
 * @version 1.0
 * @since 2023-09-02
 * @author Андрей Колесинский
 */
package org.example.strategy;

import org.example.database.DatabaseHandler;
import org.example.models.Account;

import java.util.Calendar;


public class DepositTransactionStrategy extends Transaction implements TransactionStrategy {

    /**
     * Конструктор класса DepositTransactionStrategy.
     * Создает новый объект DepositTransactionStrategy с заданными параметрами.
     * @param account объект Account, на который будет зачислена сумма транзакции
     * @param amount сумма транзакции
     * @param currency валюта транзакции
     */
    public DepositTransactionStrategy(Account account, double amount, String currency) {
        this.setType("deposit");
        this.setAccount1(account);
        this.setAccount2(account);

        if(!this.getAccount1().getCurrency().equals(currency)) this.setAmount(this.convertCurrency(amount, currency, this.getAccount1().getCurrency()));
        else this.setAmount(amount);
        this.setCurrency(this.getAccount1().getCurrency());
        this.setCalendar(Calendar.getInstance());

        DatabaseHandler dbHandler = new DatabaseHandler();
        this.setBank1(dbHandler.getBank(account.getBank_id()));
        this.setBank2(this.getBank1());

        this.setId(dbHandler.getNewTransactionId());
    }

    /**
     * Пустой конструктор класса DepositTransactionStrategy.
     */
    public DepositTransactionStrategy() {}

    /**
     * Метод execute() выполняет операцию пополнения счета, обновляет баланс счета в базе данных и создает новую транзакцию.
     * @return true, если операция выполнена успешно, false в противном случае
     */
    @Override
    public boolean execute() {

        boolean success = true;

        try {
            account1.setBalance(account1.getBalance() + amount);
        } catch (Exception e) {
            success = false;
        }

        return success;
    }


    /**
     * Метод сохраняет изменения в базе данных
     */
    @Override
    public void safe() {
        DatabaseHandler dbHanler = new DatabaseHandler();
        dbHanler.updateAccountBalance(account1.getId(), account1.getBalance());
        dbHanler.createNewTransaction(this);
    }

    /**
     * Метод print() формирует строку с информацией о транзакции в виде банковского чека.
     * @return строка с информацией о транзакции в виде банковского чека
     */
    @Override
    public String print() {
        return String.format("-".repeat(44) + "\n" + "| " + " ".repeat(13) + "Банковский чек" +  " ".repeat(13) + " |\n" + "| %-4s %35s |\n", "Чек:",  this.getId()) +
                String.format("| %-10s %29s |\n", this.getCalendar().get(Calendar.DAY_OF_MONTH) + "-" + (this.getCalendar().get(Calendar.MONTH)+1) + "-" + this.getCalendar().get(Calendar.YEAR), this.getCalendar().get(Calendar.HOUR_OF_DAY) + ":" + this.getCalendar().get(Calendar.MINUTE) + ":" + this.getCalendar().get(Calendar.SECOND)) +
                String.format("| %-15s %24s |\n", "Тип транзакции:", this.getType()) +
                String.format("| %-5s %34s |\n",  "Банк:", this.getBank1().getName()) +
                String.format("| %-5s %34s |\n",  "Счет:", this.getAccount1().getName()) +
                String.format("| %-6s %29.2f %3s |\n",  "Сумма:", this.getAmount(), this.getCurrency()) +
                "|"+ "-".repeat(42) +"|\n";
    }
}