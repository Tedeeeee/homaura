package com.shoppingmall.homaura.member.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.shoppingmall.homaura.member.utils.CryptoStringConverter;
import com.shoppingmall.homaura.wishlist.entity.WishList;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @Column(nullable = false)
    private String memberUUID;

    @JsonIgnore
    @OneToMany(mappedBy = "member")
    private List<WishList> wishLists = new ArrayList<>();

    public void changePhone(String phone) {
        this.phone = phone;
    }

    public void changeAddress(String address) {
        this.address = address;
    }

    public void changePassword(String password) {
        this.password = password;
    }

    public void updateTime(LocalDateTime updateAt) {
        this.updateAt = updateAt;
    }
}
