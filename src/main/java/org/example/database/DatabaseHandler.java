/**
 * Класс для работы с базой данных.
 * Наследует класс Configs.
 *
 * @version 1.0
 * @since 2023-09-02
 * @author Андрей Колесинский
 */
package org.example.database;

import org.example.configs.Configs;
import org.example.models.Account;
import org.example.models.Bank;
import org.example.models.User;
import org.example.strategy.DepositTransactionStrategy;
import org.example.strategy.Transaction;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DatabaseHandler extends Configs {
    Connection dbConnection;

    /**
     * Метод для получения соединения с базой данных.
     * @return объект типа Connection
     * @throws ClassNotFoundException если драйвер не найден
     * @throws SQLException если возникла ошибка при подключении к базе данных
     */
    public Connection getDbConnection()
        throws ClassNotFoundException, SQLException{
        String connectionString = "jdbc:mysql://" + dbHost  + ":"
                + dbPort + "/" + dbName ;
        Class.forName("com.mysql.cj.jdbc.Driver");

        dbConnection = DriverManager.getConnection(connectionString, dbUser, dbPass);
        return dbConnection;
    }

    /**
     * Метод для регистрации нового пользователя в базе данных.
     * @param user объект типа Пользователь
     * @throws RuntimeException если возникла ошибка при выполнении запроса к базе данных
     */
    public void signUpUser(User user){
        String insert = "INSERT INTO " + Const.USER_TABLE + "(" +
                Const.USER_NAME + ")" +
                "VALUES(?)";
        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(insert);
            prSt.setString(1, user.getName());
            prSt.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Метод для получения пользователя по идентификатору из базы данных.
     * @param id идентификатор пользователя
     * @return объект типа User
     * @throws RuntimeException если возникла ошибка при выполнении запроса к базе данных
     */
    public User getUser(int id){
        ResultSet resSet = null;

        String select = "SELECT * FROM " + Const.USER_TABLE + " WHERE " +
                Const.USER_ID + "=?";
        PreparedStatement prSt = null;
        try {
            prSt = getDbConnection().prepareStatement(select);
            prSt.setString(1, String.valueOf(id));

            resSet = prSt.executeQuery();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return pullOutUser(resSet);
    }

    /**
     * Метод для получения объекта "Пользователь" из результата запроса к базе данных.
     * @param resultSet результат запроса к базе данных
     * @return объект типа User
     * @throws RuntimeException если возникла ошибка при выполнении запроса к базе данных
     */
    private User pullOutUser(ResultSet resultSet) {
        try {
            if (resultSet.next()) {
                User user = new User();
                user.setId((Integer) resultSet.getObject(Const.USER_ID));
                user.setName((String) resultSet.getObject(Const.USER_NAME));
                return user;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    /**
     * Метод для создания нового счета в базе данных.
     * @param account объект класса "Счет"
     * @throws RuntimeException если возникла ошибка при выполнении запроса к базе данных
     */
    public void createNewAccount(Account account)
    {
        String insert = "INSERT INTO " + Const.ACCOUNT_TABLE + "(" +
                Const.ACCOUNT_NAME + "," + Const.ACCOUNT_CURRENCY + "," +
                Const.ACCOUNT_BANK + "," + Const.ACCOUNT_USER + "," +
                Const.ACCOUNT_BALANCE + "," + Const.ACCOUNT_OPENING_DATE + "," + Const.ACCOUNT_LAST_INTEREST_DATE +
                ")" + "VALUES(?,?,?,?,?,CURRENT_DATE, CURRENT_DATE)";
        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(insert);
            prSt.setString(1, account.getName());
            prSt.setString(2, account.getCurrency());
            prSt.setString(3, String.valueOf(account.getBank_id()));
            prSt.setString(4, String.valueOf(account.getUser_id()));
            prSt.setString(5, String.valueOf(account.getBalance()));
            prSt.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Метод для обновления баланса счета в базе данных.
     * @param accountId идентификатор счета
     * @param balance баланс счета
     * @throws RuntimeException если возникла ошибка при выполнении запроса к базе данных
     */
    public void updateAccountBalance(int accountId, double balance) {// Обновление баланса счета в базе данных
        String select = "UPDATE " + Const.ACCOUNT_TABLE +
                " SET " + Const.ACCOUNT_BALANCE + "=?" +
                " WHERE " +    Const.ACCOUNT_ID + "=?";
        PreparedStatement prSt = null;
        try {
            prSt = getDbConnection().prepareStatement(select);
            prSt.setString(1, String.valueOf(balance));
            prSt.setString(2, String.valueOf(accountId));
            prSt.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Метод для обновления даты последнего начисления процентов счета в базе данных.
     * @param accountId идентификатор счета
     * @throws RuntimeException если возникла ошибка при выполнении запроса к базе данных
     */
    public void updateAccountLastInterestDate(int accountId) {// Обновление баланса счета в базе данных
        String select = "UPDATE " + Const.ACCOUNT_TABLE +
                " SET " + Const.ACCOUNT_LAST_INTEREST_DATE + "=CURRENT_DATE" +
                " WHERE " +    Const.ACCOUNT_ID + "=?";
        PreparedStatement prSt = null;
        try {
            prSt = getDbConnection().prepareStatement(select);
            prSt.setString(1, String.valueOf(accountId));
            prSt.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Метод для получения счета по названию счета из базы данных.
     * @param name название счета
     * @return объект типа Account
     * @throws RuntimeException если возникла ошибка при выполнении запроса к базе данных
     */
    public Account getAccount(String name){
        ResultSet resSet = null;

        String select = "SELECT * FROM " + Const.ACCOUNT_TABLE + " WHERE " +
                Const.ACCOUNT_NAME + "=?";
        PreparedStatement prSt = null;
        try {
            prSt = getDbConnection().prepareStatement(select);
            prSt.setString(1, name);

            resSet = prSt.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return pullOutAccount(resSet);
    }

    /**
     * Метод для получения счета по идентификатору из базы данных.
     * @param id идентификатор счета
     * @return объект типа Account
     * @throws RuntimeException если возникла ошибка при выполнении запроса к базе данных
     */
    public Account getAccount(int id){
        ResultSet resSet = null;

        String select = "SELECT * FROM " + Const.ACCOUNT_TABLE + " WHERE " +
                Const.ACCOUNT_ID + "=?";
        PreparedStatement prSt = null;
        try {
            prSt = getDbConnection().prepareStatement(select);
            prSt.setString(1, String.valueOf(id));

            resSet = prSt.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return pullOutAccount(resSet);
    }

    /**
     * Метод для получения объекта "Счет" из результата запроса к базе данных.
     * @param resultSet результат запроса к базе данных
     * @return объект типа Account
     * @throws RuntimeException если возникла ошибка при выполнении запроса к базе данных
     */
    public Account pullOutAccount(ResultSet resultSet){
        try {
            if (resultSet.next()) {
                Account account = new Account();
                account.setId((Integer) resultSet.getObject(Const.ACCOUNT_ID));
                account.setName((String) resultSet.getObject(Const.ACCOUNT_NAME));
                account.setCurrency((String) resultSet.getObject(Const.ACCOUNT_CURRENCY));
                account.setOpeningDate((Date) resultSet.getObject(Const.ACCOUNT_OPENING_DATE));
                account.setBalance((Double) resultSet.getObject(Const.ACCOUNT_BALANCE));
                account.setUser_id((Integer) resultSet.getObject(Const.ACCOUNT_USER));
                account.setBank_id((Integer) resultSet.getObject(Const.ACCOUNT_BANK));
                account.setLastInterestDate((Date) resultSet.getObject(Const.ACCOUNT_LAST_INTEREST_DATE));
                return account;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    /**
     * Метод для создания нового банка в базе данных.
     * @param bank объект класса "Банк"
     * @throws RuntimeException если возникла ошибка при выполнении запроса к базе данных
     */
    public void createNewBank(Bank bank)
    {
        String insert = "INSERT INTO " + Const.BANK_TABLE + "(" +
                Const.BANK_NAME + "," + Const.BANK_ADDRESS + ")" +
                "VALUES(?,?)";
        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(insert);
            prSt.setString(1, bank.getName());
            prSt.setString(2, bank.getAddress());
            prSt.executeUpdate();

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Метод для получения банка по идентификатору из базы данных.
     * @param id идентификатор счета
     * @return объект типа Bank
     * @throws RuntimeException если возникла ошибка при выполнении запроса к базе данных
     */
    public Bank getBank(int id)
    {
        ResultSet resSet = null;

        String select = "SELECT * FROM " + Const.BANK_TABLE + " WHERE " +
                Const.BANK_ID + "=?";
        PreparedStatement prSt = null;
        try {
            prSt = getDbConnection().prepareStatement(select);
            prSt.setString(1, String.valueOf(id));

            resSet = prSt.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return pullOutBank(resSet);
    }

    /**
     * Метод для получения объекта "Банк" из результата запроса к базе данных.
     * @param resultSet результат запроса к базе данных
     * @return объект типа Bank
     * @throws RuntimeException если возникла ошибка при выполнении запроса к базе данных
     */
    private Bank pullOutBank(ResultSet resultSet) {
        try {
            if (resultSet.next()) {
                Bank bank = new Bank();
                bank.setId((Integer) resultSet.getObject(Const.BANK_ID));
                bank.setName((String) resultSet.getObject(Const.BANK_NAME));
                bank.setAddress((String) resultSet.getObject(Const.BANK_ADDRESS));
                return bank;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    /**
     * Метод для создания новой транзакции в базе данных.
     * @param transaction объект класса "Транзакция"
     * @throws RuntimeException если возникла ошибка при выполнении запроса к базе данных
     */
    public void createNewTransaction(Transaction transaction)
    {
        String insert = "INSERT INTO " + Const.TRANSACTION_TABLE + "(" +
                Const.TRANSACTION_TYPE + "," + Const.TRANSACTION_AMOUNT + "," + Const.TRANSACTION_CURRENCY + "," +
                Const.TRANSACTION_TIME + "," + Const.TRANSACTION_DATE + "," +
                Const.TRANSACTION_BANK1 + "," + Const.TRANSACTION_BANK2 + "," +
                Const.TRANSACTION_ACCOUNT1 + "," + Const.TRANSACTION_ACCOUNT2 + ")" +
                "VALUES(?,?,?,?,?,?,?,?,?)";
        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(insert);
            prSt.setString(1, transaction.getType());
            prSt.setString(2, String.valueOf(transaction.getAmount()));
            prSt.setString(3, transaction.getCurrency());

            prSt.setString(4, transaction.getCalendar().get(Calendar.HOUR_OF_DAY) + ":" + transaction.getCalendar().get(Calendar.MINUTE) + ":" + transaction.getCalendar().get(Calendar.SECOND));
            prSt.setString(5, transaction.getCalendar().get(Calendar.YEAR) + "-" + (transaction.getCalendar().get(Calendar.MONTH)+1) + "-" + transaction.getCalendar().get(Calendar.DAY_OF_MONTH));

            prSt.setString(6, String.valueOf(transaction.getBank1().getId()));
            prSt.setString(7, String.valueOf(transaction.getBank2().getId()));
            prSt.setString(8, String.valueOf(transaction.getAccount1().getId()));
            prSt.setString(9, String.valueOf(transaction.getAccount2().getId()));
            prSt.executeUpdate();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Метод для получения нового идентификатора транзакции
     * @return новый идентификатор транзакции
     * @throws RuntimeException если произошла ошибка при выполнении запроса к базе данных
     */
    public int getNewTransactionId()
    {
        String insert = "SELECT * FROM " + Const.TRANSACTION_TABLE + " ORDER BY " + Const.TRANSACTION_ID + " DESC LIMIT 1";
        ResultSet resSet = null;
        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(insert);
            resSet =  prSt.executeQuery();
            if (resSet.next()) {
                return (resSet.getInt(1)+1);
            }

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    /**
     * Метод для получения списка транзакций по идентификатору и дате отсчета из базы данных.
     * @param id идентификатор счета
     * @param calendar дата отсчета
     * @return список объектов типа User
     * @throws RuntimeException если возникла ошибка при выполнении запроса к базе данных
     */
    public List<Transaction> getTransactions(int id, Calendar calendar) {
        ResultSet resSet = null;
        String select = "SELECT * FROM " + Const.TRANSACTION_TABLE + " WHERE " +
               "(" + Const.TRANSACTION_ACCOUNT1 + "=?" + " OR " + Const.TRANSACTION_ACCOUNT2 + "=?" + ")" + " AND " +
                Const.TRANSACTION_DATE + ">=?";
        PreparedStatement prSt = null;
        try {
            prSt = getDbConnection().prepareStatement(select);
            prSt.setString(1, String.valueOf(id));
            prSt.setString(2, String.valueOf(id));
            prSt.setString(3, (calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH)+1) + "-" + calendar.get(Calendar.DAY_OF_MONTH) + " 00:00:00" ));
            resSet = prSt.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return pullOutTransactions(resSet);
    }

    /**
     * Метод для получения списка объектов "Транзакция" из результата запроса к базе данных.
     * @param resultSet результат запроса к базе данных
     * @return список объектов типа User
     * @throws RuntimeException если возникла ошибка при выполнении запроса к базе данных
     */
    private List<Transaction> pullOutTransactions(ResultSet resultSet) {
        List<Transaction> transactions = new ArrayList<>();
        Transaction transaction;
        try {
            while (resultSet.next()) {

                transaction = new DepositTransactionStrategy();
                transaction.setId((Integer) resultSet.getObject(Const.TRANSACTION_ID));
                transaction.setType((String) resultSet.getObject(Const.TRANSACTION_TYPE));
                transaction.setAmount((Double) resultSet.getObject(Const.TRANSACTION_AMOUNT));
                transaction.setCurrency((String) resultSet.getObject(Const.TRANSACTION_CURRENCY));

                Date date = resultSet.getDate(Const.TRANSACTION_DATE); // получаем объект типа Date из ResultSet
                Time time = resultSet.getTime(Const.TRANSACTION_TIME);
                Calendar calendar = Calendar.getInstance(); // создаем новый объект типа Calendar
                calendar.setTime(date); // устанавливаем дату из объекта типа Date в объект типа Calendar
               // calendar.setTime(time); // устанавливаем время из объекта типа Time в объект типа Calendar
                transaction.setCalendar(calendar); // устанавливаем объект типа Calendar в транзакцию
                transaction.setAccount1(this.getAccount((Integer) resultSet.getObject(Const.TRANSACTION_ACCOUNT1)));
                transaction.setAccount2(this.getAccount((Integer) resultSet.getObject(Const.TRANSACTION_ACCOUNT2)));
                transaction.setBank1(this.getBank((Integer) resultSet.getObject(Const.TRANSACTION_BANK1)));
                transaction.setBank2(this.getBank((Integer) resultSet.getObject(Const.TRANSACTION_BANK2)));
                transactions.add(transaction);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return transactions;
    }
}
