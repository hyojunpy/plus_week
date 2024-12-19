package com.example.demo.status;

import lombok.Getter;

@Getter
public enum ReservationStatus {
    // 유저 상태 Enum
    PENDING,
    APPROVED,
    CANCELD,
    EXPIRED
}
