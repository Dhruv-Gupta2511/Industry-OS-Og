package com.industryos.repository;

import com.industryos.entity.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShipmentRepository extends JpaRepository<Shipment, Integer> {
    List<Shipment> findByVehicleId(Integer vehicleId);
    List<Shipment> findByVehicleIdIn(List<Integer> vehicleIds);
    Optional<Shipment> findBySoId(Integer soId);
    List<Shipment> findByStatus(String status);
}

