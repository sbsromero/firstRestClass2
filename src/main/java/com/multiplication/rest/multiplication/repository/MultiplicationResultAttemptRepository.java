package com.multiplication.rest.multiplication.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.multiplication.rest.multiplication.domain.MultiplicationResultAttempt;

public interface MultiplicationResultAttemptRepository extends CrudRepository<MultiplicationResultAttempt, Long> {

	List<MultiplicationResultAttempt> findTop5ByUserAliasOrderByIdDesc(String userAlias);
}
