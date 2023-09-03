/**
 * Класс, представляющий банк.
 * Содержит информацию об идентификаторе, названии и адресе банка.
 *
 * @version 1.0
 * @since 2023-09-02
 * @author Андрей Колесинский
 */

package org.example.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Bank {
    /**
     * идентификатор банка.
     */
    private int id;

    /**
     * Название банка.
     */
    private String name;

    /**
     * Адрес банка.
     */
    private String address;
}