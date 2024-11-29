package com.cubelogic.anticorruptionlayer.repository;

import com.cubelogic.anticorruptionlayer.model.Side;
import com.cubelogic.anticorruptionlayer.model.Transaction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static com.cubelogic.anticorruptionlayer.Utils.createTransaction;

@DataJpaTest
public class TransactionRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private TransactionRepository transactionRepository;

    @BeforeEach
    void cleanup() {
        transactionRepository.deleteAll();
        transactionRepository.flush();
        entityManager.clear();
    }

    @Test
    public void testSave() {
        Transaction transaction = transactionRepository.save(createTransaction(Side.SELL));
        Assertions.assertEquals(Side.SELL, transaction.getSide());
    }

    @Test
    public void testFindAll() {
        Transaction transaction1 =  createTransaction(Side.SELL);
        Transaction transaction2 =  createTransaction(Side.SELL);
        Transaction transaction3 =  createTransaction(Side.SELL);
        Transaction transaction4 = createTransaction(Side.SELL);

        transactionRepository.saveAll(List.of(transaction1, transaction2, transaction3, transaction4));

        List<Transaction> result = transactionRepository.findAll();
        Assertions.assertEquals(4, result.size());

    }

    @Test
    public void testFindAllCreatedWithinTimeConstraint() {
        long traderId = 1;

        Transaction transaction1 =  createTransaction(Side.SELL);
        Transaction transaction2 =  createTransaction(Side.SELL);
        Transaction transaction3 =  createTransaction(Side.SELL);
        Transaction transaction4 = createTransaction(Side.SELL);

        transactionRepository.saveAll(List.of(transaction1, transaction2, transaction3, transaction4));

        List<Transaction> result = transactionRepository.findAllCreatedThirtyMinutesBeforeNow(traderId);
        Assertions.assertEquals(4, result.size());

    }


}
