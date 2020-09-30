package repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import model.Seat;

@Repository
public interface SeatRepository extends JpaRepository<Seat, String> {

}