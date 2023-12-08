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
public class Follow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "follow_id")
    private Long followId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "following_id" ,referencedColumnName = "users_id")
    private Users following;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "follower_id" ,referencedColumnName = "users_id")
    private Users follower;



}
