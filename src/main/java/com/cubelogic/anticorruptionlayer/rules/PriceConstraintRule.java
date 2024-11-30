package com.cubelogic.anticorruptionlayer.rules;

import com.cubelogic.anticorruptionlayer.model.Transaction;
import com.cubelogic.anticorruptionlayer.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class PriceConstraintRule implements TransactionRule {

    @Autowired
    private TransactionRepository transactionRepository;

    public boolean isSuspiciousTransaction(Transaction transaction) {
        Optional<Transaction> equivalentTransaction =
                getTransactions(transaction).stream().filter(tran -> isOutsideAcceptablePriceRange(tran,transaction)).findAny();
        return equivalentTransaction.isPresent();
    }

    public boolean isOutsideAcceptablePriceRange(Transaction existingTransaction, Transaction proposedTransaction){
        double tenPercentOfProposedTradePrice = (double) 10 / 100 * proposedTransaction.getPrice();
        boolean isLowerThanMin = existingTransaction.getPrice() < (proposedTransaction.getPrice() - tenPercentOfProposedTradePrice);
        boolean isHigherThanMax = existingTransaction.getPrice() > (proposedTransaction.getPrice() + tenPercentOfProposedTradePrice);
        return isLowerThanMin || isHigherThanMax;
    }

    public List<Transaction> getTransactions(Transaction transaction){
        List<Transaction> transactions =
                transactionRepository.findAllCreatedThirtyMinutesBeforeNow(transaction.getTraderId());
        //This is a temp solution to being unable to filter in the query by Enum value
        return transactions.stream().filter(retrieved -> !retrieved.getSide().equals(transaction.getSide())).toList();
    }
}
