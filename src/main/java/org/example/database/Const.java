/**
 * Класс "Const" содержит константы для названий таблиц и столбцов в базе данных.
 *
 * @version 1.0
 * @since 2023-09-02
 * @author Андрей Колесинский
 */

package org.example.database;

public class Const {

    /**
     * Название таблицы "users".
     */
    public static final String USER_TABLE = "users";

    public static final String USER_ID = "user_id";
    public static final String USER_NAME = "name";

    /**
     * Название таблицы "accounts".
     */
    public static final String ACCOUNT_TABLE = "accounts";

    public static final String ACCOUNT_ID = "account_id";
    public static final String ACCOUNT_NAME = "name";
    public static final String ACCOUNT_CURRENCY = "currency";
    public static final String ACCOUNT_OPENING_DATE = "openingDate";
    public static final String ACCOUNT_BALANCE = "balance";
    public static final String ACCOUNT_USER = "user___fk";
    public static final String ACCOUNT_BANK = "bank___fk";
    public static final String ACCOUNT_LAST_INTEREST_DATE = "lastInterestDate";

    /**
     * Название таблицы "banks".
     */
    public static final String BANK_TABLE = "banks";

    public static final String BANK_ID = "bank_id";
    public static final String BANK_NAME = "name";
    public static final String BANK_ADDRESS = "address";

    /**
     * Название таблицы "transactions".
     */
    public static final String TRANSACTION_TABLE = "transactions";

    public static final String TRANSACTION_ID = "transaction_id";
    public static final String TRANSACTION_TYPE = "type";
    public static final String TRANSACTION_AMOUNT = "amount";
    public static final String TRANSACTION_CURRENCY = "currency";
    public static final String TRANSACTION_TIME = "time";
    public static final String TRANSACTION_DATE = "date";
    public static final String TRANSACTION_BANK1 = "bank1___fk";
    public static final String TRANSACTION_BANK2 = "bank2___fk";
    public static final String TRANSACTION_ACCOUNT1 = "account1___fk";
    public static final String TRANSACTION_ACCOUNT2 = "account2___fk";
}
