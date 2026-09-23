package com.exam.demo.repository;

import com.exam.demo.model.Show;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShowRepository extends MongoRepository<Show, String> {
    Show findById(Long id);
}