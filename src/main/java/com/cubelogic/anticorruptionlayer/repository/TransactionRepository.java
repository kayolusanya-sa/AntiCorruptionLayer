package com.cubelogic.anticorruptionlayer.repository;

import com.cubelogic.anticorruptionlayer.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("select t from Transaction t where t.timeStamp >= ?#{@transactionRepository.thirtyMinutesBefore()} and traderId = :traderId")
    List<Transaction> findAllCreatedThirtyMinutesBeforeNow(@Param("traderId") long traderId);

    default LocalDateTime thirtyMinutesBefore() {
        return LocalDateTime.now().minusMinutes(30);
    }
}
