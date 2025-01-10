package com.example.demo.entity;

import com.example.demo.status.UserStatus;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String nickname;
    private String password;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false, columnDefinition = "default 'NORMAL'")
    private UserStatus status; // NORMAL, BLOCKED

    @Enumerated(value = EnumType.STRING)
    private Role role = Role.USER;

    public User(String role, String email, String nickname, String password) {
        this.role = Role.of(role);
        this.email = email;
        this.nickname = nickname;
        this.password = password;
    }

    public User() {}

    public User(Long id, String email, String nickname, String password, UserStatus status, Role role) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.status = status;
        this.role = role;
    }

    public void updateStatusToBlocked() {
        this.status = UserStatus.BLOCKED;
    }
}
