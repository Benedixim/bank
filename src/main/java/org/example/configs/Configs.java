/**
 * Класс Configs представляет собой конфигурационные настройки для подключения к базе данных.
 * Содержит поля dbHost, dbPort, dbUser, dbPass и dbName, которые соответствуют хосту, порту,
 * имени пользователя, паролю и названию базы данных соответственно.
 * По умолчанию заданы значения для локальной базы данных MySQL.
 *
 * @version 1.0
 * @since 2023-09-02
 * @author Андрей Колесинский
 */
package org.example.configs;

public class Configs {
    protected String dbHost = "localhost";
    protected String dbPort = "3306";

    protected String dbUser = "root";

    protected String dbPass = "12345";
    protected String dbName = "task";
}
