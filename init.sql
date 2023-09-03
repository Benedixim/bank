CREATE TABLE `accounts` (
                            `account_id` int NOT NULL AUTO_INCREMENT,
                            `name` varchar(34) NOT NULL,
                            `currency` varchar(3) DEFAULT NULL,
                            `openingDate` date DEFAULT NULL,
                            `balance` double DEFAULT NULL,
                            `user___fk` int DEFAULT NULL,
                            `bank___fk` int DEFAULT NULL,
                            `lastInterestDate` date DEFAULT NULL,
                            PRIMARY KEY (`account_id`),
                            KEY `user___fk` (`user___fk`),
                            KEY `bank___fk` (`bank___fk`),
                            CONSTRAINT `bank___fk` FOREIGN KEY (`bank___fk`) REFERENCES `banks` (`bank_id`),
                            CONSTRAINT `user___fk` FOREIGN KEY (`user___fk`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `banks` (
                         `bank_id` int NOT NULL AUTO_INCREMENT,
                         `name` varchar(40) NOT NULL,
                         `address` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_ru_0900_ai_ci DEFAULT NULL,
                         PRIMARY KEY (`bank_id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `users` (
                         `user_id` int NOT NULL AUTO_INCREMENT,
                         `name` varchar(50) NOT NULL,
                         PRIMARY KEY (`user_id`),
                         UNIQUE KEY `id_UNIQUE` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=135 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `transactions` (
                                `transaction_id` int NOT NULL AUTO_INCREMENT,
                                `type` varchar(20) NOT NULL,
                                `amount` double NOT NULL,
                                `currency` varchar(3) DEFAULT NULL,
                                `time` time DEFAULT NULL,
                                `date` date DEFAULT NULL,
                                `bank1___fk` int DEFAULT NULL,
                                `bank2___fk` int DEFAULT NULL,
                                `account1___fk` int DEFAULT NULL,
                                `account2___fk` int DEFAULT NULL,
                                PRIMARY KEY (`transaction_id`),
                                KEY `account1___fk` (`account1___fk`),
                                KEY `account2___fk` (`account2___fk`),
                                KEY `bank1___fk` (`bank1___fk`),
                                KEY `bank2___fk` (`bank2___fk`),
                                CONSTRAINT `account1___fk` FOREIGN KEY (`account1___fk`) REFERENCES `accounts` (`account_id`),
                                CONSTRAINT `account2___fk` FOREIGN KEY (`account2___fk`) REFERENCES `accounts` (`account_id`),
                                CONSTRAINT `bank1___fk` FOREIGN KEY (`bank1___fk`) REFERENCES `banks` (`bank_id`),
                                CONSTRAINT `bank2___fk` FOREIGN KEY (`bank2___fk`) REFERENCES `banks` (`bank_id`)
) ENGINE=InnoDB AUTO_INCREMENT=74 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
