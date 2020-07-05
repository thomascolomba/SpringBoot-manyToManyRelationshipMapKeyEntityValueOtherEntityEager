package com.thomas.manyToManyRelationshipMapKeyEntityValueOtherEntityEager.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.thomas.manyToManyRelationshipMapKeyEntityValueOtherEntityEager.domains.C;

public interface CRepository extends CrudRepository<C, Long>{
	List<C> findByC(String c);
}
