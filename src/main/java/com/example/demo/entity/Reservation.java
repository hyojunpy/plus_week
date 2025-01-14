package com.example.demo.entity;

import com.example.demo.status.ReservationStatus;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@DynamicUpdate
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDateTime startAt;

    private LocalDateTime endAt;

    @Enumerated(EnumType.STRING)
    private ReservationStatus status; // PENDING, APPROVED, CANCELED, EXPIRED

    public Reservation(Item item, User user, ReservationStatus status, LocalDateTime startAt, LocalDateTime endAt) {
        this.item = item;
        this.user = user;
        this.status = status;
        this.startAt = startAt;
        this.endAt = endAt;
    }

    public Reservation(Long id, Item item, User user, LocalDateTime startAt, LocalDateTime endAt, ReservationStatus status) {
        this.id = id;
        this.item = item;
        this.user = user;
        this.startAt = startAt;
        this.endAt = endAt;
        this.status = status;
    }

    public Reservation() {}

    public void updateStatus(ReservationStatus status) {
        this.status = status;
    }
}
