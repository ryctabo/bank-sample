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

package com.ryctabo.bank.sample.service;

import com.ryctabo.bank.sample.model.Transaction;
import com.ryctabo.bank.sample.persistence.Database;
import com.ryctabo.bank.sample.service.validator.Validator;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Gustavo Pacheco (ryctabo at gmail.com)
 * @version 1.0-SNAPSHOT
 */
public class TransactionServiceImpl implements TransactionService {

    private Map<Long, Transaction> transactions = Database.getTransactions();

    @Override
    public List<Transaction> get(String accountNumber) {
        return get().stream()
                .filter(t -> t.getOwn().getNumber().equals(accountNumber))
                .collect(Collectors.toList());
    }

    @Override
    public List<Transaction> get(String accountNumber, Month month) {
        return get().stream()
                .filter(t -> t.getOwn().getNumber().equals(accountNumber))
                .filter(t -> t.getCreated().getMonth().equals(month))
                .collect(Collectors.toList());
    }

    @Override
    public List<Transaction> get() {
        return new ArrayList<>(transactions.values());
    }

    @Override
    public Transaction get(long id) {
        Validator.validateId(id);
        return this.transactions.get(id);
    }

    @Override
    public Transaction add(Transaction transaction) {
        long id = transactions.size() + 1;
        transaction.setId(id);
        transaction.setCreated(LocalDateTime.now());

        this.transactions.put(id, transaction);

        return transaction;
    }

}
