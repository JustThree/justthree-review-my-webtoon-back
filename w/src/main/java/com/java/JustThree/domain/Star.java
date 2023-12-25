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
public class Star {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="star_id")
    private Long starId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="master_id",referencedColumnName = "master_id")
    private Webtoon webtoon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id" ,referencedColumnName ="users_id")
    private Users users;

    @Column(name = "star_val")
    private int starVal;
}
