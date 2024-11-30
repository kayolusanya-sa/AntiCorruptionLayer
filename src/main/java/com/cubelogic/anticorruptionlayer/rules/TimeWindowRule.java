package com.cubelogic.anticorruptionlayer.rules;

import com.cubelogic.anticorruptionlayer.model.Transaction;
import com.cubelogic.anticorruptionlayer.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class TimeWindowRule implements TransactionRule {
    @Autowired
    private TransactionRepository transactionRepository;
    //can't go to the DB for every transaction, needs to be cached
    @Override
    public boolean isSuspiciousTransaction(Transaction transaction) {
        Optional<Transaction> equivalentTransaction =
                getTransactions(transaction).stream().filter(tran -> isReversalTrade(tran,transaction)).findAny();
        return equivalentTransaction.isPresent();
    }

    public boolean isReversalTrade(Transaction existingTransaction, Transaction proposedTransaction){
        return existingTransaction.getTraderId() == proposedTransaction.getTraderId()
                && existingTransaction.getVolume() == proposedTransaction.getVolume()
                && existingTransaction.getPrice() == proposedTransaction.getPrice()
                && !existingTransaction.getSide().equals(proposedTransaction.getSide());
    }

    public List<Transaction> getTransactions(Transaction transaction){
        return transactionRepository.findAllCreatedThirtyMinutesBeforeNow(transaction.getTraderId());
    }
}
