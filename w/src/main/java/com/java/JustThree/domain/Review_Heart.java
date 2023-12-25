package com.java.JustThree.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Builder(toBuilder = true)
@Entity
@Data
@Setter
public class Review_Heart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="review_heart_id")
    private Long reviewHeartId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id",referencedColumnName = "users_id")
    private Users users;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="review_id",referencedColumnName = "review_id")
    private Review review;
}
