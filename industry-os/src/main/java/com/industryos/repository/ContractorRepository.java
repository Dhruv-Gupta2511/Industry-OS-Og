package com.industryos.repository;

import com.industryos.entity.Contractor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContractorRepository extends JpaRepository<Contractor, Integer> {
    List<Contractor> findByPlantId(Integer plantId);
}

