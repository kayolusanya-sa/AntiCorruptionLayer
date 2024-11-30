package com.cubelogic.anticorruptionlayer.rules;

import com.cubelogic.anticorruptionlayer.model.Side;
import com.cubelogic.anticorruptionlayer.model.Transaction;
import com.cubelogic.anticorruptionlayer.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static com.cubelogic.anticorruptionlayer.Utils.createTransaction;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureTestDatabase
public class TransactionRulesTest {

    @BeforeEach
    void cleanup() {
        transactionRepository.deleteAll();
        transactionRepository.flush();
    }

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private TimeWindowRule timeWindowRule;

    @Autowired
    private PriceConstraintRule priceConstraintRule;

    @Test
    void testPositivePriceConstraintCheck(){
        Transaction transaction1 = createTransaction(1,22.5,1000, Side.SELL);
        Transaction transaction2 = createTransaction(1,22.5,1000, Side.SELL);
        Transaction transaction3 = createTransaction(1,22.5,1000, Side.SELL);
        Transaction transaction4 = createTransaction(1,22.5,1000, Side.SELL);
        Transaction transaction5 = createTransaction(1,22.5,1000, Side.BUY);
        transactionRepository.saveAll(List.of(transaction1, transaction2, transaction3, transaction4, transaction5));

        Transaction proposedTransaction =  createTransaction(1,26,1000, Side.BUY);
        assertTrue( priceConstraintRule.isSuspiciousTransaction(proposedTransaction));
    }

    @Test
    void testNegativePriceConstraintCheck(){
        Transaction transaction1 =  createTransaction(1,22.5,1000, Side.SELL);
        Transaction transaction2 =  createTransaction(1,22.5,1000, Side.SELL);
        Transaction transaction3 =  createTransaction(1,22.5,1000, Side.SELL);
        Transaction transaction4 = createTransaction(1,25.5,1000, Side.SELL);
        transactionRepository.saveAll(List.of(transaction1, transaction2, transaction3, transaction4));

        Transaction proposedTransaction =  createTransaction(1,24,1000, Side.BUY);
        assertFalse( priceConstraintRule.isSuspiciousTransaction(proposedTransaction));
    }

    @Test
    void testPositiveTimeWindowRuleCheck(){
        Transaction transaction1 =  createTransaction(1,25,1000, Side.SELL);
        Transaction transaction2 =  createTransaction(Side.SELL);
        Transaction transaction3 =  createTransaction(Side.SELL);
        Transaction transaction4 = createTransaction(Side.SELL);
        transactionRepository.saveAll(List.of(transaction1, transaction2, transaction3, transaction4));

        Transaction proposedTransaction =  createTransaction(1,25,1000, Side.BUY);
        assertTrue( timeWindowRule.isSuspiciousTransaction(proposedTransaction));
    }

    @Test
    void testNegativeTimeWindowRuleCheck(){
        Transaction transaction1 =  createTransaction(1,25,1000, Side.SELL);
        Transaction transaction2 =  createTransaction(Side.SELL);
        Transaction transaction3 =  createTransaction(Side.SELL);
        Transaction transaction4 = createTransaction(Side.SELL);
        transactionRepository.saveAll(List.of(transaction1, transaction2, transaction3, transaction4));

        Transaction attemptedTransaction =  createTransaction(1,25,1000, Side.SELL);
        assertFalse( timeWindowRule.isSuspiciousTransaction(attemptedTransaction));
    }
}
