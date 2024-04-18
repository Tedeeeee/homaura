package com.shoppingmall.homaura.member.entity;

import com.shoppingmall.homaura.member.utils.CryptoStringConverter;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "member")
@Builder
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, unique = true)
    @Convert(converter = CryptoStringConverter.class)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Convert(converter = CryptoStringConverter.class)
    private String name;

    @Column(unique = true)
    @Convert(converter = CryptoStringConverter.class)
    private String nickname;

    @Column(nullable = false)
    @Convert(converter = CryptoStringConverter.class)
    private String phone;

    @Column(nullable = false)
    @Convert(converter = CryptoStringConverter.class)
    private String address;

    private LocalDateTime createAt;

    private LocalDateTime updateAt;

    @Enumerated(EnumType.STRING)
    private Role role;
}