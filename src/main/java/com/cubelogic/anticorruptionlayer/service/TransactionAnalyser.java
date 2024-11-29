package com.cubelogic.anticorruptionlayer.service;

import com.cubelogic.anticorruptionlayer.model.Transaction;

import java.util.List;

public interface TransactionAnalyser {
    List<Transaction> analyseTrades(List<Transaction> transactions);
}
