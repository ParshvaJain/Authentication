package com.authentication.repository;

import com.authentication.model.ConfirmationToken;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends MongoRepository<ConfirmationToken,String> {

	ConfirmationToken findByconfirmationToken(String confToken);	
}
