package com.christmas.letter.repository;

import com.christmas.letter.model.entity.LetterEntity;
import org.socialsignin.spring.data.dynamodb.repository.DynamoDBCrudRepository;
import org.socialsignin.spring.data.dynamodb.repository.DynamoDBPagingAndSortingRepository;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.socialsignin.spring.data.dynamodb.repository.EnableScanCount;
import org.springframework.stereotype.Repository;

@Repository
@EnableScanCount
@EnableScan
public interface LetterRepository extends DynamoDBCrudRepository<LetterEntity, String>,
        DynamoDBPagingAndSortingRepository<LetterEntity, String> {
}
