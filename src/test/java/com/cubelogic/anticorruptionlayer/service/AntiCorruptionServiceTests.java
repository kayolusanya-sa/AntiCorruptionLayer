package com.cubelogic.anticorruptionlayer.service;

import com.cubelogic.anticorruptionlayer.model.Side;
import com.cubelogic.anticorruptionlayer.model.Transaction;
import com.cubelogic.anticorruptionlayer.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static com.cubelogic.anticorruptionlayer.Utils.createTransaction;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class AntiCorruptionServiceTests {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AntiCorruptionService antiCorruptionService;

    @BeforeEach
    void cleanup() {
        transactionRepository.deleteAll();
        transactionRepository.flush();
    }

    @Test
    void analyseTradesTestTimeWindowPositive(){
        Transaction transaction1 = createTransaction(1,25,1000, Side.SELL);
        Transaction transaction2 = createTransaction(Side.SELL);
        Transaction transaction3 = createTransaction(Side.SELL);
        Transaction transaction4 = createTransaction(Side.SELL);
        transactionRepository.saveAll(List.of(transaction1, transaction2, transaction3, transaction4));

        Transaction proposedTransaction1 = createTransaction(1,25,1000, Side.BUY);
        Transaction proposedTransaction2 = createTransaction(1,25,1000, Side.BUY);
        Transaction proposedTransaction3 = createTransaction(1,25,1000, Side.BUY);
        Transaction proposedTransaction4 = createTransaction(1,25,1000, Side.BUY);
        List<Transaction> proposedTransactions =
                List.of(proposedTransaction1, proposedTransaction2, proposedTransaction3, proposedTransaction4);
        List<Transaction> suspectTransactions = antiCorruptionService.analyseTrades(proposedTransactions);
        assertEquals(4,suspectTransactions.size());
    }

    @Test
    void analyseTradesTestPriceRangePositive(){
        Transaction transaction1 = createTransaction(Side.SELL);
        Transaction transaction2 = createTransaction(Side.SELL);
        Transaction transaction3 = createTransaction(Side.SELL);
        Transaction transaction4 = createTransaction(Side.SELL);
        transactionRepository.saveAll(List.of(transaction1, transaction2, transaction3, transaction4));

        Transaction proposedTransaction1 = createTransaction(1,25,1000, Side.BUY);
        Transaction proposedTransaction2 = createTransaction(1,25,1000, Side.BUY);
        Transaction proposedTransaction3 = createTransaction(1,25,1000, Side.BUY);
        Transaction proposedTransaction4 = createTransaction(1,25,1000, Side.BUY);
        List<Transaction> proposedTransactions =
                List.of(proposedTransaction1, proposedTransaction2, proposedTransaction3, proposedTransaction4);
        List<Transaction> suspectTransactions = antiCorruptionService.analyseTrades(proposedTransactions);
        assertEquals(4,suspectTransactions.size());
    }

    @Test
    void analyseTradesTestPriceRangeNegative(){
        Transaction transaction1 = createTransaction(Side.SELL);
        Transaction transaction2 = createTransaction(Side.SELL);
        Transaction transaction3 = createTransaction(Side.SELL);
        Transaction transaction4 = createTransaction(Side.SELL);
        transactionRepository.saveAll(List.of(transaction1, transaction2, transaction3, transaction4));

        Transaction proposedTransaction1 = createTransaction(1,91,1000, Side.BUY);
        Transaction proposedTransaction2 = createTransaction(1,90,1000, Side.BUY);
        Transaction proposedTransaction3 = createTransaction(1,90,1000, Side.BUY);
        Transaction proposedTransaction4 = createTransaction(1,90,1000, Side.BUY);
        List<Transaction> proposedTransactions =
                List.of(proposedTransaction1, proposedTransaction2, proposedTransaction3, proposedTransaction4);
        List<Transaction> suspectTransactions = antiCorruptionService.analyseTrades(proposedTransactions);
        assertEquals(3,suspectTransactions.size());
    }
}
