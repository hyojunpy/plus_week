package com.example.demo.repository;

import com.example.demo.dto.ReservationResponseDto;
import com.example.demo.entity.Reservation;

import java.util.List;

public interface ReservationRepositoryCustom {
    List<Reservation> search(Long userId, Long itemId);
}
