package com.example.springsecuritytest.domain.batch;

import com.example.springsecuritytest.domain.batch.BatchResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BatchRepository extends JpaRepository<BatchResult, Long> {

    List<BatchResult> findAll();
    List<BatchResult> findByName(String category);
}
