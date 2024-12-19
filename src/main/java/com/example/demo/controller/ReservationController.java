package com.example.demo.controller;

import com.example.demo.dto.ReservationRequestDto;
import com.example.demo.dto.ReservationResponseDto;
import com.example.demo.service.ReservationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservations")
public class ReservationController {
    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    //예약 생성
    @PostMapping
    public void createReservation(@RequestBody ReservationRequestDto reservationRequestDto) {
        reservationService.createReservation(reservationRequestDto.getItemId(),
                                            reservationRequestDto.getUserId(),
                                            reservationRequestDto.getStartAt(),
                                            reservationRequestDto.getEndAt());
    }

    //예약 상태 수정
    @PatchMapping("/{id}/update-status")
    public ResponseEntity<ReservationResponseDto> updateReservation(@PathVariable Long id, @RequestBody ReservationRequestDto requestDto) {
        ReservationResponseDto reservationResponseDto =  reservationService.updateReservationStatus(id, requestDto.getStatus());
        return new ResponseEntity<>(reservationResponseDto, HttpStatus.OK);
    }

    //예약 전체 조회
    @GetMapping
    public List<ReservationResponseDto> findAll() {
        return reservationService.getReservations();
    }

    //조건에 따른 예약 조회
    @GetMapping("/search")
    public List<ReservationResponseDto> searchAll(@RequestParam(required = false) Long userId,
                          @RequestParam(required = false) Long itemId) {
        return reservationService.searchAndConvertReservations(userId, itemId);
    }
}
