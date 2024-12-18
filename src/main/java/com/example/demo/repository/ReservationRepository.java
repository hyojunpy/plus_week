package com.example.demo.repository;

import com.example.demo.entity.Reservation;
import com.example.demo.status.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long>, ReservationRepositoryCustom{

    List<Reservation> searchReservations(Long userId, Long itemId);

    default Reservation findByQuery(Long id) {
        return findByIdOrElseThrow(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND) );
    }

    @Query("SELECT r FROM Reservation r " +
            "WHERE r.item.id = :id " +
            "AND NOT (r.endAt <= :startAt OR r.startAt >= :endAt) " +
            "AND r.status = 'APPROVED'")
    List<Reservation> findConflictingReservations(
            @Param("id") Long id,
            @Param("startAt") LocalDateTime startAt,
            @Param("endAt") LocalDateTime endAt
    );

    @Query("SELECT R FROM Reservation R JOIN FETCH R.item I JOIN FETCH I.manager JOIN FETCH I.owner")
    List<Reservation> findAllReservationWithUserAndItem();

    @Query("select r " +
            "from Reservation r " +
            "join fetch r.user " +
            "join fetch r.item " +
            "where r.id = :id")
    Optional<Reservation> findByIdOrElseThrow(@Param("id") Long reservationId);

}
