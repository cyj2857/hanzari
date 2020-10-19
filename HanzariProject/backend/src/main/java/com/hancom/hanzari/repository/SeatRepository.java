package com.hancom.hanzari.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hancom.hanzari.model.Seat;

@Repository
public interface SeatRepository extends JpaRepository<Seat, String> {

}