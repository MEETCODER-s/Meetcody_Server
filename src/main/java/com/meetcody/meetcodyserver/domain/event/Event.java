package com.meetcody.meetcodyserver.domain.event;

import com.meetcody.meetcodyserver.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //TODO("참여자guest 번호가 왜 필요한지 다시 의문이 들기 시작했다..")

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @Column(nullable = false)
    private String googleEventId;

    @Column(nullable = false)
    private String etag;

    @Column(nullable = false)
    private String link;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private String title;

    private String description;

    @Column(nullable = false)
    private String creatorEmail;

    @Column(nullable = false)
    private String organizerEmail;

    @Column(nullable = false)
    private Boolean isOrganizer;

    @Column(nullable = false)
    private LocalDateTime start;

    @Column(nullable = false)
    private LocalDateTime end;

    @Column(nullable = false)
    private String iCalUid;

    private String hangoutsLink;

    private String hangoutsTitle;

}
