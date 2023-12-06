package com.java.JustThree.domain;

import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import org.springframework.security.core.userdetails.User;

import java.math.BigInteger;

public class Star {
    @Id
    @Column(name="star_id")
    private Long starId;
    @ManyToOne(fetch = FetchType.LAZY)
    private Webtoon webtoon;
    @ManyToOne(fetch = FetchType.LAZY)
    private Users users;
    @Column(name = "star_val")
    private int starVal;
}
