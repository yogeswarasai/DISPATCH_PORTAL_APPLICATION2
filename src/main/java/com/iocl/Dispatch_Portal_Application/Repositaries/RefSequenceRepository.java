package com.iocl.Dispatch_Portal_Application.Repositaries;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.iocl.Dispatch_Portal_Application.Entity.RefSequence;


@Repository
public interface RefSequenceRepository extends JpaRepository<RefSequence, String>{
   
	Optional<RefSequence> findByLocCode(String locCode);
	
    boolean existsByLocCode(String locCode);

 
}
