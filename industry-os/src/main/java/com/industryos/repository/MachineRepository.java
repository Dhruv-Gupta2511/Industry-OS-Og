package com.industryos.repository;

import com.industryos.entity.Machine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MachineRepository extends JpaRepository<Machine, Integer> {
    List<Machine> findByLineId(Integer lineId);
    List<Machine> findByLineIdIn(List<Integer> lineIds);
    long countByStatus(String status);
}

