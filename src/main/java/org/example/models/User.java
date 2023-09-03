/**
 * Класс, представляющий пользователя.
 * Содержит информацию об идентификаторе и имени пользователя.
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
public class User {

    /**
     * идентификатор пользователя.
     */
    private int id;

    /**
     * имя пользователя.
     */
    private String name;
}


