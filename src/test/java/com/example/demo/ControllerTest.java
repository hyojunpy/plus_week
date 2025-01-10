package com.example.demo;


import com.example.demo.controller.ReservationController;
import com.example.demo.dto.ReservationResponseDto;
import com.example.demo.entity.Item;
import com.example.demo.entity.Reservation;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.service.ReservationService;
import com.example.demo.status.ReservationStatus;
import com.example.demo.status.UserStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReservationController.class)
public class ControllerTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private ReservationService reservationService;

    private LocalDateTime startAt;
    private LocalDateTime endAt;

    @BeforeEach
    void Setup(){
        this.mvc = MockMvcBuilders.webAppContextSetup(context)
                .addFilters()
                .build();

        startAt = LocalDateTime.parse("2024-12-25T20:30:11");
        endAt = LocalDateTime.parse("2024-12-26T20:30:11");
    }

    @Test
    @DisplayName("예약 생성")
    void ReservationCreateTest() throws Exception{
        User user = createTestUser();
        Item item = createTestItem(user);
        Reservation reservation = createTestReservation(item, user, startAt, endAt);
        ReservationResponseDto reservationResponseDto = new ReservationResponseDto(reservation.getId(), reservation.getUser().getNickname(), reservation.getItem().getName(), ReservationStatus.PENDING, reservation.getStartAt(), reservation.getEndAt());

        given(reservationService.createReservation(item.getId(), user.getId(), startAt, endAt)).willReturn(reservationResponseDto);

        mvc.perform(post("/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createReservationBody(item.getId(), user.getId(), startAt, endAt)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(reservation.getId()))
                .andExpect(jsonPath("$.nickname").value(reservation.getUser().getNickname()))
                .andExpect(jsonPath("$.itemName").value(reservation.getItem().getName()))
                .andExpect(jsonPath("$.status").value(reservation.getStatus()))
                .andExpect(jsonPath("$.startAt").value(reservation.getStartAt()))
                .andExpect(jsonPath("$.endAt").value(reservation.getEndAt()));
    }

    @Test
    @DisplayName("아이템 없을 시 예약 생성")
    void ReservationCreateNotExistItemTest() throws Exception {
        User user = createTestUser();
        given(reservationService.createReservation(20L, user.getId(), startAt, endAt))
                .willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "아이템을 찾을 수 없습니다."));

        mvc.perform(post("/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createReservationBody(20L, user.getId(), startAt, endAt)))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(
                        Objects.requireNonNull(result.getResolvedException()).getMessage().contains("아이템이 존재하지 않습니다.")
                ));
    }

    @Test
    @DisplayName("아이템 없을 시 예약 생성")
    void ReservationCreateNotExistUserTest() throws Exception {
        Item item = createTestItem(createTestUser());
        given(reservationService.createReservation(item.getId(), 20L, startAt, endAt))
                .willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다."));

        mvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createReservationBody(item.getId(), 20L, startAt, endAt)))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(
                        Objects.requireNonNull(result.getResolvedException()).getMessage().contains("유저가 존재하지 않습니다.")
                ));
    }

    @Test
    @DisplayName("예약 상태 변경")
    void ReservationStatusPatchTest() throws Exception {
        User user = createTestUser();
        Item item = createTestItem(user);

        Reservation reservation = new Reservation(item, user, ReservationStatus.PENDING, startAt, endAt);

        ReservationStatus status = ReservationStatus.APPROVED;

        given(reservationService.updateReservationStatus(reservation.getId(), status))
                .willReturn(new ReservationResponseDto(reservation.getId(), reservation.getUser().getNickname(), reservation.getItem().getName(),
                        reservation.getStatus(), reservation.getStartAt(), reservation.getEndAt()));

        mvc.perform(patch("/reservations/{id}/update-status", reservation.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(patchReservation(status)))
                .andExpect(status().isOk())
                .andExpect(jsonPath(("$.id")).value(reservation.getId()))
                .andExpect(jsonPath("$.nickname").value(reservation.getUser().getNickname()))
                .andExpect(jsonPath("$.itemName").value(reservation.getItem().getName()))
                .andExpect(jsonPath("$.status").value(reservation.getStatus()))
                .andExpect(jsonPath("$.startAt").value(startAt.toString()))
                .andExpect(jsonPath("$.endAt").value(endAt.toString()));
    }

    @Test
    @DisplayName("예약 상태 변경 - PENDING 상태가 아닐 시")
    void ReservationStatusNotEqualsPendingPatchTest() throws Exception {
        User user = createTestUser();
        Item item = createTestItem(user);

        Reservation reservation = new Reservation(item, user, ReservationStatus.PENDING, startAt, endAt);

        ReservationStatus status = ReservationStatus.APPROVED;

        given(reservationService.updateReservationStatus(reservation.getId(), status))
                .willThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "PENDING 상태일때만 변경 가능합니다."));

        mvc.perform(patch("/reservations/{id}/update-status", reservation.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(patchReservation(status)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(
                        Objects.requireNonNull(result.getResolvedException()).getMessage().contains("PENDING 상태일때만 변경 가능합니다.")
                ));
    }

    private User createTestUser() {
        return new User(3L, "test@gmail.com", "tester", "test1234!", UserStatus.NORMAL, Role.USER);
    }

    private  Item createTestItem(User user) {
        return new Item("test", "testDescription", user, user, "PENDING");
    }

    private Reservation createTestReservation(Item item, User user, LocalDateTime startAt, LocalDateTime endAt) {
        return new Reservation(3L, item, user, startAt, endAt, ReservationStatus.PENDING);
    }

    private String createReservationBody(Long itemId, Long userId, LocalDateTime startAt, LocalDateTime endAt) {
        return """
                {
                    "itemId" : %d,
                    "userId" : %d,
                    "startAt": "%s",
                    "endAt" : "%s"
                }
                """.formatted(itemId, userId, startAt.format(DateTimeFormatter.ISO_LOCAL_DATE), endAt.format(DateTimeFormatter.ISO_LOCAL_DATE));
    }

    private String patchReservation(ReservationStatus reservationStatus) {
        return """
                {
                    "status" : %s
                }
                """.formatted(reservationStatus.toString());
    }
}
