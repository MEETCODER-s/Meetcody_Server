package com.meetcody.meetcodyserver.domain.calendar;

import com.meetcody.meetcodyserver.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Calendar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @Column(nullable = false)
    private String googleCalId;

    @Column(nullable = false)
    private String etag;

    @Column(nullable = false)
    private String timezone;

    @Column(nullable = false)
    private Boolean isHangoutsmeet;

}
