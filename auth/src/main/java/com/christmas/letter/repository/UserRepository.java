package com.christmas.letter.repository;

import com.christmas.letter.model.UserEntity;
import org.socialsignin.spring.data.dynamodb.repository.DynamoDBCrudRepository;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.stereotype.Repository;

@Repository
@EnableScan
public interface UserRepository extends DynamoDBCrudRepository<UserEntity, String> {
}
