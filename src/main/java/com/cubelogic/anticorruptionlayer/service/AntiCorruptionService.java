package com.cubelogic.anticorruptionlayer.service;

import com.cubelogic.anticorruptionlayer.model.Transaction;
import com.cubelogic.anticorruptionlayer.rules.TransactionRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AntiCorruptionService implements TransactionAnalyser{

    @Autowired
    private List<TransactionRule> rules;

    public List<Transaction> analyseTrades(List<Transaction> transactions) {
        return transactions.stream().filter(this::isSuspicious).toList();
    }

    boolean isSuspicious(Transaction transaction){
        return rules.stream().anyMatch(rule -> rule.evaluateTrade(transaction));
    }
}
