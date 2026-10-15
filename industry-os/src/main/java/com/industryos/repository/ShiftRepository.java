package com.industryos.repository;

import com.industryos.entity.Shift;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShiftRepository extends JpaRepository<Shift, Integer> {
    Optional<Shift> findByShiftName(String shiftName);
}

