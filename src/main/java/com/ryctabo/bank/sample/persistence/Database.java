/*
 * Copyright (c) 2018 Gustavo Pacheco
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.ryctabo.bank.sample.persistence;

import com.ryctabo.bank.sample.model.*;

import java.time.LocalDate;
import java.time.Month;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Gustavo Pacheco (ryctabo at gmail.com)
 * @version 1.0-SNAPSHOT
 */
public class Database {

    private static final Map<Long, User> USERS = new HashMap<>();

    private static final Map<Long, Transaction> TRANSACTIONS = new HashMap<>();

    static {
        //Initializing database
        LocalDate birthdate = LocalDate.of(1993, Month.FEBRUARY, 11);
        Person person = new Person("Gustavo Pacheco", birthdate);
        User user = new User("ryctabo", person);
        user.setId(1L);

        user.addAccount(new SavingsAccount(1L, "0001"));
        user.addAccount(new CurrentAccount(2L, "0002"));
        user.addAccount(new KidsSavingsAccount(3L, "0003"));

        USERS.put(1L, user);
    }

    public static Map<Long, User> getUsers() {
        return USERS;
    }

    public static Map<Long, Transaction> getTransactions() {
        return TRANSACTIONS;
    }

}
