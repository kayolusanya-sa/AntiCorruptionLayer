package com.cubelogic.anticorruptionlayer.rules;

import com.cubelogic.anticorruptionlayer.model.Transaction;

import java.util.List;

public interface TransactionRule {
    boolean evaluateTrade(Transaction transaction);
    List<Transaction> getTransactions(Transaction transaction);
}
