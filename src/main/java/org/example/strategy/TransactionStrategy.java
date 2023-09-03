/**
 * интерфейс стратегии выполнения транзакции
 *
 * @version 1.0
 * @since 2023-09-02
 * @author Андрей Колесинский
 */
package org.example.strategy;

public interface TransactionStrategy {

    /**
     * Метод для выполнения транзакции
     * @return true, если транзакция выполнена успешно, иначе false
     */
    boolean execute();

    /**
     * Метод для сохранения транзакции в базе данных
     */
    void safe();
}
