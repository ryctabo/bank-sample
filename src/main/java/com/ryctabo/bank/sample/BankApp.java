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

package com.ryctabo.bank.sample;

import com.ryctabo.bank.sample.controller.TransactionProcessor;
import com.ryctabo.bank.sample.model.*;
import com.ryctabo.bank.sample.service.TransactionService;
import com.ryctabo.bank.sample.service.TransactionServiceImpl;
import com.ryctabo.bank.sample.service.UserService;
import com.ryctabo.bank.sample.service.UserServiceImpl;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

/**
 * @author Gustavo Pacheco (ryctabo at gmail.com)
 * @version 1.0-SNAPSHOT
 */
public class BankApp {

    private static Scanner console = new Scanner(System.in);

    private static UserService userService = new UserServiceImpl();

    private static TransactionService transactionService = new TransactionServiceImpl();

    private static TransactionProcessor processor = new TransactionProcessor();

    public static void main(String[] args) {
        int option;
        do {
            System.out.println("BANK APPLICATION:");
            System.out.println("1. Create an user");
            System.out.println("2. Create an account");
            System.out.println("3. Consult account");
            System.out.println("4. Consign");
            System.out.println("5. Retire");
            System.out.println("6. Transfer");
            System.out.println("7. Exit");

            String response;
            do {
                System.out.print("Select an option: ");
                response = console.nextLine();
            } while (!response.matches("[0-9]+"));

            option = Integer.parseInt(response);

            handleOption(option);
        } while (option != 7);
    }

    private static void handleOption(int option) {
        System.out.println();
        switch (option) {
            case 1:
                createAnUser();
                break;
            case 2:
                createAnAccount();
                break;
            case 3:
                handleTransaction(TransactionType.QUERY);
                break;
            case 4:
                handleTransaction(TransactionType.CONSIGNMENT);
                break;
            case 5:
                handleTransaction(TransactionType.RETREAT);
                break;
            case 6:
                handleTransaction(TransactionType.TRANSFER);
                break;
            case 7:
                break;
            default:
                System.out.println("This option is undefined!");
                System.out.println();
        }
    }

    private static void handleTransaction(TransactionType transactionType) {
        System.out.println("Account number:");
        String accountNumber = console.nextLine();

        userService.get().forEach(u -> u.getAccounts().forEach(a -> {
            if (accountNumber.equals(a.getNumber())) {
                switch (transactionType) {
                    case QUERY:
                        printAccount(a);
                        break;
                    case CONSIGNMENT:
                        consign(a);
                        break;
                    case RETREAT:
                        retire(a);
                        break;
                    case TRANSFER:
                        transfer(a);
                        break;
                }
            }
        }));
    }

    private static void transfer(Account account) {
        System.out.println("Account number to transfer:");
        String accountNumber = console.nextLine();

        Account account2 = null;
        for (User user : userService.get()) {
            for (Account ac : user.getAccounts()) {
                if (accountNumber.equals(ac.getNumber())) {
                    account2 = ac;
                }
            }
        }

        String message;

        if (account2 != null) {
            System.out.println("Amount to be transfer:");
            String response = console.nextLine();

            if (response.matches("[0-9]+")) {
                Transaction transaction = new Transaction(TransactionType.TRANSFER, account);
                transaction.setAmount(Double.parseDouble(response));
                transaction.setRecipient(account2);

                processor.process(transaction);

                NumberFormat numberFormat = NumberFormat.getCurrencyInstance();
                String amount = numberFormat.format(transaction.getAmount());
                message = String.format("You have transfer %s in the accounts [%s => %s]!",
                        amount, account.getNumber(), account2.getNumber());
            } else {
                message = "Amount must be a number!";
            }
        } else {
            message = "There isn't account to transfer, transaction cancelled!";
        }

        System.out.println();
        System.out.println(message);
        System.out.println();
    }

    private static void retire(Account account) {
        System.out.println("Amount to be retire:");
        String response = console.nextLine();

        String message;
        if (response.matches("[0-9]+")) {
            Transaction transaction = new Transaction(TransactionType.RETREAT, account);
            transaction.setAmount(Double.parseDouble(response));

            processor.process(transaction);

            NumberFormat numberFormat = NumberFormat.getCurrencyInstance();
            String amount = numberFormat.format(transaction.getAmount());
            message = String.format("You have retire %s in the account [%s]!", amount, account.getNumber());
        } else {
            message = "Amount must be a number!";
        }

        System.out.println();
        System.out.println(message);
        System.out.println();
    }

    private static void consign(Account account) {
        System.out.println("Amount to be consigned:");
        String response = console.nextLine();

        String message;
        if (response.matches("[0-9]+")) {
            Transaction transaction = new Transaction(TransactionType.CONSIGNMENT, account);
            transaction.setAmount(Double.parseDouble(response));

            processor.process(transaction);

            NumberFormat numberFormat = NumberFormat.getCurrencyInstance();
            String amount = numberFormat.format(transaction.getAmount());
            message = String.format("You have consigned %s in the account [%s]!", amount, account.getNumber());
        } else {
            message = "Amount must be a number!";
        }

        System.out.println();
        System.out.println(message);
        System.out.println();
    }

    private static void printAccount(Account account) {
        System.out.println(String.format("ACCOUNT #%s", account.getNumber()));
        if (account instanceof SavingsAccount) {
            System.out.println("SAVINGS ACCOUNT");
        } else if (account instanceof CurrentAccount) {
            System.out.println("CURRENT ACCOUNT");
        } else {
            System.out.println("KID SAVINGS ACCOUNT");
        }
        System.out.println(String.format("Owner: %s", account.getUser().getPerson().getName()));
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance();
        System.out.println(String.format("Amount: %s", numberFormat.format(account.getAmount())));
        System.out.println();
        System.out.println("TRANSACTIONS - - - -");
        System.out.println();

        transactionService.get(account.getNumber())
                .forEach(BankApp::printTransaction);
    }

    private static void printTransaction(Transaction transaction) {
        final String FORMAT = "Transaction[%04d][%s]";
        System.out.println(String.format(FORMAT, transaction.getId(), transaction.getType()));
        System.out.println("Created: " + transaction.getCreated());

        NumberFormat numberFormat = NumberFormat.getCurrencyInstance();
        if (transaction.getAmount() != 0)
            System.out.println("Amount: " + numberFormat.format(transaction.getAmount()));

        if (transaction.getCost() != 0)
            System.out.println("Cost: " + numberFormat.format(transaction.getCost()));

        if (transaction.getAmount() != 0) {
            double discount = transaction.getAmount() + transaction.getCost();
            System.out.println("Total discount: " + numberFormat.format(discount));
        }

        if (transaction.getRecipient() != null)
            System.out.println("Recipient number account: " + transaction.getRecipient().getNumber());

        System.out.println("-------------------");
        System.out.println();
    }

    private static void createAnAccount() {
        System.out.println("Username:");
        String username = console.nextLine();
        User user = userService.get(username);

        String message;
        if (user != null) {
            long id = user.getAccounts().size() + 1;

            int number = (int) ((Math.random() * 9999) + 1);
            String accountNumber = String.format("%04d", number);

            System.out.println("Select an account type:");
            System.out.println("1. Savings account");
            System.out.println("2. Current account");

            String regex;
            if (user.getPerson().getAge() < 14) {
                System.out.println("3. Kid savings account");
                regex = "[1-2]";
            } else {
                regex = "[1-3]";
            }

            String option = console.nextLine();
            if (option.matches(regex)) {
                Account account = null;
                switch (Integer.parseInt(option)) {
                    case 1:
                        account = new SavingsAccount(id, accountNumber);
                        break;
                    case 2:
                        account = new CurrentAccount(id, accountNumber);
                        break;
                    case 3:
                        account = new KidsSavingsAccount(id, accountNumber);
                        break;
                }
                user.addAccount(account);

                message = String.format("The account with number %s has been created!", accountNumber);
            } else {
                message = "The option is undefined, this process is cancelled";
            }
        } else {
            message = String.format("The user with username '%s' was not found.", username);
        }

        System.out.println();
        System.out.println(message);
        System.out.println();
    }

    private static void createAnUser() {
        System.out.println("Write the following fields:");

        System.out.println("Username:");
        String username = console.nextLine();

        System.out.println("Full name:");
        String name = console.nextLine();

        System.out.println("Birthdate (DD-MM-YYYY)");
        String birthdateString = console.nextLine();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate birthdate = LocalDate.parse(birthdateString, formatter);

        Person person = new Person(name, birthdate);
        User user = new User(username, person);
        userService.add(user);

        System.out.println();
        System.out.println(String.format("The user %s has been created!", username));
        System.out.println();
    }

}
