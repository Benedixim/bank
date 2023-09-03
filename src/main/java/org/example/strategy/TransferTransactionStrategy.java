/**
 * Класс TransferTransactionStrategy представляет стратегию транзакции для операции пополнения счета.
 * Расширяет класс Transaction и реализует интерфейс TransactionStrategy.
 *
 * @version 1.0
 * @since 2023-09-02
 * @author Андрей Колесинский
 */
package org.example.strategy;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.database.DatabaseHandler;
import org.example.models.Account;

import java.util.Calendar;

@EqualsAndHashCode(callSuper = true)
@Data
public class TransferTransactionStrategy extends Transaction implements TransactionStrategy {

    /**
     * Сумма денег к начислению на второй счет.
     */
    protected double amount2;

    /**
     * Конструктор класса TransferTransactionStrategy.
     * Создает новый объект TransferTransactionStrategy с заданными параметрами.
     * @param account1 объект Account, с которого будет снята сумма транзакции
     * @param account2 объект Account, на который будет зачислена сумма транзакции
     * @param amount сумма транзакции
     * @param currency валюта транзакции
     */
    public TransferTransactionStrategy(Account account1, Account account2, double amount, String currency) {
        this.setType("transfer");
        this.setAccount1(account1);
        this.setAccount2(account2);
        this.setAmount(amount);

        /*
          Сравнение валюты, в которой открыт счет, и выбранную валюту для перевода.
          Далее со счета будет снята (начислена) переконвертированная сумма в той валюте, в который открыт счет.
         */
        if(!this.getAccount1().getCurrency().equals(currency)) this.setAmount(this.convertCurrency(amount, currency, this.getAccount1().getCurrency()));
        else this.setAmount(amount);

        if(!this.getAccount2().getCurrency().equals(currency)) this.setAmount2(this.convertCurrency(amount, currency, this.getAccount2().getCurrency()));
        else this.setAmount2(amount);

        this.setCurrency(this.getAccount1().getCurrency());

        this.setCurrency(currency);
        this.setCalendar(Calendar.getInstance());

        DatabaseHandler dbHandler = new DatabaseHandler();
        this.setBank1(dbHandler.getBank(account1.getBank_id()));
        this.setBank2(dbHandler.getBank(account2.getBank_id()));

        this.setId(dbHandler.getNewTransactionId());
    }

    /**
     * Метод execute() выполняет операцию перевода денег на счет, обновляет баланс счетов в базе данных и создает новую транзакцию.
     * @return true, если операция выполнена успешно, false в противном случае
     */
    @Override
    public boolean execute() {

        boolean success = true;

        try {
            if(account1.getBalance() < amount || account1.equals(account2)) throw new RuntimeException();
            account1.setBalance(account1.getBalance() - amount);
            account2.setBalance(account2.getBalance() + amount2);
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
        DatabaseHandler dbHandler = new DatabaseHandler();
        dbHandler.updateAccountBalance(account1.getId(), account1.getBalance());
        dbHandler.updateAccountBalance(account2.getId(), account2.getBalance());
        dbHandler.createNewTransaction(this);
    }

    /**
     * Метод print() формирует строку с информацией о транзакции в виде банковского чека.
     * @return строка с информацией о транзакции в виде банковского чека
     */
    @Override
    public String print() {
        return String.format("-".repeat(56) + "\n" + "| " + " ".repeat(19) + "Банковский чек" +  " ".repeat(19) + " |\n" + "| %-4s %47s |\n", "Чек:",  this.getId()) +
                String.format("| %-10s %41s |\n", this.getCalendar().get(Calendar.DAY_OF_MONTH) + "-" + (this.getCalendar().get(Calendar.MONTH)+1) + "-" + this.getCalendar().get(Calendar.YEAR), this.getCalendar().get(Calendar.HOUR_OF_DAY) + ":" + this.getCalendar().get(Calendar.MINUTE) + ":" + this.getCalendar().get(Calendar.SECOND)) +
                String.format("| %-15s %36s |\n", "Тип транзакции:", this.getType()) +
                String.format("| %-17s %34s |\n",  "Банк отправителя:", this.getBank1().getName()) +
                String.format("| %-16s %35s |\n",  "Банк получателя:", this.getBank2().getName()) +
                String.format("| %-17s %34s |\n",  "Счет отправителя:", this.getAccount1().getName()) +
                String.format("| %-16s %35s |\n",  "Счет получателя:", this.getAccount2().getName()) +
                String.format("| %-8s %39.2f %3s |\n",  "Сумма 1:", (-1*this.getAmount()), this.getAccount1().getCurrency()) +
                String.format("| %-8s %39.2f %3s |\n",  "Сумма 2:", this.getAmount2(), this.getAccount2().getCurrency()) + "|"+
                "-".repeat(54) +"|\n";
    }
}
