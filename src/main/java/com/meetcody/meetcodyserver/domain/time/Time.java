package com.meetcody.meetcodyserver.domain.time;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Time {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = true)
    private String start;

    @Column(nullable = true)
    private String end;

    @Column(nullable = false)
    private Boolean isAccepted;

    @Column(nullable = false)
    private LocalDateTime startRange;

    @Column(nullable = false)
    private LocalDateTime endRange;

//    @Column(nullable = false)
    @ColumnDefault("0")
    private Integer durationHour;

    @Column(nullable = false)
    @ColumnDefault("0")
    private Integer durationMinute;

}
