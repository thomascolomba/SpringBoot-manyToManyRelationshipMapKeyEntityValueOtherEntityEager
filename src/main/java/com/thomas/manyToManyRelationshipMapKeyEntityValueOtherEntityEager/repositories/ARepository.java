package com.thomas.manyToManyRelationshipMapKeyEntityValueOtherEntityEager.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.thomas.manyToManyRelationshipMapKeyEntityValueOtherEntityEager.domains.A;

public interface ARepository extends CrudRepository<A, Long> {
	public List<A> findByA(String a);
}