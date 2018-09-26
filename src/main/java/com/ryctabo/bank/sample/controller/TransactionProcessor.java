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

package com.ryctabo.bank.sample.controller;

import com.ryctabo.bank.sample.model.Account;
import com.ryctabo.bank.sample.model.Transaction;
import com.ryctabo.bank.sample.service.TransactionService;
import com.ryctabo.bank.sample.service.TransactionServiceImpl;

/**
 * @author Gustavo Pacheco (ryctabo at gmail.com)
 * @version 1.0-SNAPSHOT
 */
public class TransactionProcessor implements Processor<Transaction> {

    private TransactionService service = new TransactionServiceImpl();

    @Override
    public void process(Transaction transaction) {
        Account account = transaction.getOwn();

        if (transaction.getType().isChargePerTransaction()) {
            long count = service.get(account.getNumber()).stream()
                    .filter(t -> t.getType().isChargePerTransaction())
                    .count();
            if (count >= account.getFreeTransactions()) {
                double cost = transaction.getAmount() * account.getPercentOfChargePerTransaction();
                transaction.setCost(cost);
            }
        }

        switch (transaction.getType()) {
            case CONSIGNMENT:
                double amount = account.getAmount() + transaction.getAmount();
                account.setAmount(amount);
                break;
            case RETREAT:
            case TRANSFER:
                double diff = account.getAmount() - transaction.getAmount() - transaction.getCost();
                account.setAmount(diff);

                Account recipient = transaction.getRecipient();
                if (recipient != null)
                    recipient.setAmount(transaction.getAmount());
                break;
        }

        service.add(transaction);
    }

}
