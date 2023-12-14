package com.java.JustThree.domain;

import com.java.JustThree.repository.mypage.FollowRepository;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.userdetails.User;

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
    private Users following;// 팔로잉하는 사람들.

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "follower_id" ,referencedColumnName = "users_id")
    private Users follower;//팔로우 하는 사람들.


}
