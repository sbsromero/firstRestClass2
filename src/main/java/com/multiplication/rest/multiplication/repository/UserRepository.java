package com.multiplication.rest.multiplication.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.multiplication.rest.multiplication.domain.User;

public interface UserRepository extends CrudRepository<User, Long> {
	
	Optional<User> findByAlias(final String alias);

}
