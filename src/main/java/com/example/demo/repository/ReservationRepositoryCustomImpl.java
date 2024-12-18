package com.example.demo.repository;

import com.example.demo.dto.ReservationResponseDto;
import com.example.demo.entity.QItem;
import com.example.demo.entity.QReservation;
import com.example.demo.entity.QUser;
import com.example.demo.entity.Reservation;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class ReservationRepositoryCustomImpl implements ReservationRepositoryCustom{
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Reservation> search(Long userId, Long itemId) {
        QReservation reservation = QReservation.reservation;
        QUser qUser = QUser.user;
        QItem qItem = QItem.item;

        return jpaQueryFactory
                .selectFrom(reservation)
                .leftJoin(reservation.user, qUser).fetchJoin()
                .leftJoin(reservation.item, qItem).fetchJoin()
                .where(
                        userId != null ? reservation.user.id.eq(userId) : null,
                        itemId != null ? reservation.item.id.eq(itemId) : null
                )
                .fetch();
    }
}
