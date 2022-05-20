package com.meetcody.meetcodyserver.domain.user;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "oauth")
public class Oauth {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String accessToken;

    @Column(nullable = false)
    private int expiresIn;


    private String email;

    @Column(nullable = false)
    private LocalDateTime created_date;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
