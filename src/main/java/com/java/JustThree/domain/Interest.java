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
public class Interest {
    @Id
    @GeneratedValue
    @Column(name="interest_id")
    private Long interestId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id",referencedColumnName = "users_id")
    private Users users;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "master_id",referencedColumnName = "master_id")
    private Webtoon webtoon;
}
