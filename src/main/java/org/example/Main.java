/**
 * Класс Main является точкой входа в программу и содержит методы для работы с банковскими счетами.
 */
package org.example;

import org.example.database.DatabaseHandler;
import org.example.models.Account;
import org.example.models.Bank;
import org.example.models.User;
import org.example.strategy.*;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Главный метод программы, который загружает данные из файла, а затем вызывает метод menuChoice().
 */
public class Main {
    public static void main(String[] args) {
        //loadData();
        menuChoice();
    }

    /**
     * Метод auth() запрашивает у пользователя номер счета и проверяет его наличие в базе данных.
     * @param in - объект класса Scanner для считывания пользовательского ввода.
     * @return объект класса Account, соответствующий введенному номеру счета.
     */
    public static Account auth(Scanner in)
    {
        Account account;
        while(true) {
            System.out.println("Введите номер счета");
            System.out.print("--> ");

            DatabaseHandler dbHandler = new DatabaseHandler();
            account = dbHandler.getAccount(in.nextLine());
            if (account != null) return account;
            else System.out.println("Введенный Вами номер счета не существует!");
        }
    }

    /**
     * Метод menuChoice() выводит меню выбора действий с банковским счетом и запускает соответствующие методы.
     */
    private static void menuChoice() {
        Scanner in = new Scanner(System.in);
        Account account = auth(in);
        InterestCalculationTransactionStrategy interestCalculationTransactionStrategy = new InterestCalculationTransactionStrategy(account);
        interestCalculationTransactionStrategy.execute();

        while(true) {
            System.out.println("Меню");
            System.out.println("1. Пополнить счет");
            System.out.println("2. Снять деньги");
            System.out.println("3. Перевести деньги на другой счет");
            System.out.println("4. Получить выписку");
            System.out.println("5. Выход");
            int choice = getInt(in, 5);

            double amount = 0;
            String currency = null;

            if (choice == 1 || choice == 2 || choice == 3) {
                amount = getAmount(in);
                currency = getCurrency(in);
            }

            TransactionStrategy strategy = null;

            switch (choice) {
                case 1:
                    strategy = new DepositTransactionStrategy(account, amount, currency);
                    break;
                case 2:
                    strategy = new WithdrawTransactionStrategy(account, amount, currency);
                    break;
                case 3:
                    Account account2 = auth(in);
                    strategy = new TransferTransactionStrategy(account, account2, amount, currency);
                    break;
                case 4:
                    String period = getPeriod(in);
                    account.saveToFile(period);
                    account.print(period);
                    break;
                case 5:
                    interestCalculationTransactionStrategy.stop();
                    return;

                default:
                    break;
            }

            if (choice == 1 || choice == 2 || choice == 3)
                if (!strategy.execute()) System.out.println("Операция невозможна!");
                else {strategy.safe(); System.out.println(((Transaction) strategy).print());}
            assert strategy != null;
            ((Transaction) strategy).checkToFile(((Transaction) strategy).getId());
        }
    }

    /**
     * Метод getPeriod() запрашивает у пользователя период выписки.
     * @param in - объект класса Scanner для считывания пользовательского ввода.
     * @return введенный пользователем период выписки.
     */
    private static String getPeriod(Scanner in) {
        List<String> periods = new ArrayList<>();
        periods.add("month");
        periods.add("year");
        periods.add("all");

        while(true) {
            System.out.println("Выберите период: " + periods);
            System.out.print("--> ");
            String period = in.nextLine();
            if (!periods.contains(period))
                System.out.println("Введенный вами период не подходит! Попробуйте ещё раз!");
            else return period;
        }
    }

    /**
     * Метод getAmount() запрашивает у пользователя сумму транзакции.
     * @param in - объект класса Scanner для считывания пользовательского ввода.
     * @return введенную пользователем сумму транзакции.
     */
    public static double getAmount(Scanner in)
    {
        double amount;
        while (true) {
            try {
                System.out.println("Введите сумму");
                System.out.print("--> ");
                amount = in.nextDouble();
                in.nextLine();
                break;
            } catch (InputMismatchException e) {
                System.out.println("Ошибка: введите число");
                in.nextLine();
            }
        }
        return amount;
    }

    /**
     * Метод getCurrency() запрашивает у пользователя валюту транзакции.
     * @param in - объект класса Scanner для считывания пользовательского ввода.
     * @return введенную пользователем валюту транзакции.
     */
    public static String getCurrency(Scanner in)
    {
        List<String> currencies = new ArrayList<>();
        currencies.add("BYN");
        currencies.add("USD");
        currencies.add("EUR");
        currencies.add("RUB");

        while(true) {
            System.out.println("Выберите валюту: " + currencies);
            System.out.print("--> ");
            String currency = in.nextLine();
            if (!currencies.contains(currency))
                System.out.println("Введенная вами валюта не существует! Попробуйте ещё раз!");
            else return currency;
        }
    }

    /**
     * Метод getInt() запрашивает у пользователя целое число в заданном диапазоне.
     * @param in - объект класса Scanner для считывания пользовательского ввода.
     * @param max - максимальное значение, которое может ввести пользователь.
     * @return введенное пользователем число.
     */
    public static int getInt(Scanner in, int max)
    {
        int choice;
        while (true) {
            try {
                System.out.print("--> ");
                choice = in.nextInt();
                in.nextLine();
                if (choice < 1 || choice > max) {
                    throw new InputMismatchException();
                }
                break;
            } catch (InputMismatchException e) {
                System.out.println("Ошибка: введите число от 1 до " + max);
                in.nextLine();
            }
        }
        return choice;
    }

    /**
     * Метод loadUsersFromYaml() загружает список пользователей из файла YAML.
     * @return список пользователей.
     */
    public static List<User> loadUsersFromYaml() {
        Yaml yaml = new Yaml();
        InputStream inputStream = User.class.getClassLoader().getResourceAsStream("users.yml");
        Map<String, Object> data = yaml.load(inputStream);

        List<User> users = new ArrayList<>();
        List<Map<String, String>> userList = (List<Map<String, String>>) data.get("users");

        for (Map<String, String> userMap : userList) {
            String name = userMap.get("name");
            User user = new User();
            user.setName(name);
            users.add(user);
        }

        return users;
    }

    /**
     * Метод loadAccountsFromYaml() загружает список банковских счетов из файла YAML.
     * @return список банковских счетов.
     */
    public static List<Account> loadAccountsFromYaml() {

            Yaml yaml = new Yaml();
            InputStream inputStream = Account.class.getClassLoader().getResourceAsStream("accounts.yml");
            Map<String, Object> data = yaml.load(inputStream);

            List<Account> accounts = new ArrayList<>();
            List<Map<String, String>> accountList = (List<Map<String, String>>) data.get("accounts");


            for (Map<String, String> account : accountList) {
                String name = (String) ((java.util.Map<?, ?>) account).get("name");
                String currency = (String) ((java.util.Map<?, ?>) account).get("currency");
                double balance = (double) ((java.util.Map<?, ?>) account).get("balance");

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

                Date openingDate, lastInterestDate;
                try {
                    openingDate = dateFormat.parse(account.get("openingDate"));
                    lastInterestDate = dateFormat.parse(account.get("lastInterestDate"));

                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }

                int bank_id = (int) ((java.util.Map<?, ?>) account).get("bank_id");
                int user_id = (int) ((java.util.Map<?, ?>) account).get("user_id");

                Account acc = new Account();
                acc.setName(name);
                acc.setCurrency(currency);
                acc.setBalance(balance);
                acc.setLastInterestDate(lastInterestDate);
                acc.setOpeningDate(openingDate);
                acc.setBank_id(bank_id);
                acc.setUser_id(user_id);
                accounts.add(acc);
            }

            return accounts;
    }

    /**
     * Метод loadBanksFromYaml() загружает список банков из файла YAML.
     * @return список банков.
     */
    public static List<Bank> loadBanksFromYaml() {
        Yaml yaml = new Yaml();
        InputStream inputStream = Bank.class.getClassLoader().getResourceAsStream("banks.yml");
        Map<String, Object> data = yaml.load(inputStream);

        List<Bank> banks = new ArrayList<>();
        List<Map<String, String>> bankList = (List<Map<String, String>>) data.get("banks");

        for (Map<String, String> bankMap : bankList) {
            String name = bankMap.get("name");
            String address = bankMap.get("address");
            Bank bank = new Bank();
            bank.setName(name);
            bank.setAddress(address);
            banks.add(bank);
        }

        return banks;
    }

    /**
     * Метод loadData() загружает данные из файлов YAML в базу данных.
     */
    public static void loadData(){

        DatabaseHandler dbHandler = new DatabaseHandler();

        List<User> users;
        users = loadUsersFromYaml();
        System.out.println(users);
        for(User u: users){
            dbHandler.signUpUser(u);
        }

        List<Bank> banks;
        banks = loadBanksFromYaml();
        System.out.println(banks);
        for (Bank bank : banks) {
            dbHandler.createNewBank(bank);
        }

        List<Account> accounts;
        accounts = loadAccountsFromYaml();
        System.out.println(accounts);
        for (Account acc : accounts) {
            dbHandler.createNewAccount(acc);
        }

    }
}