package com.hackerearth.natwest.repository;

import org.springframework.data.repository.CrudRepository;

import com.hackerearth.natwest.entity.TransactionEntity;

public interface TransactionRepository extends CrudRepository<TransactionEntity, Integer> {

}
