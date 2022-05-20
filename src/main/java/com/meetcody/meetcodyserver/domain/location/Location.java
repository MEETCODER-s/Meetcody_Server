package com.meetcody.meetcodyserver.domain.location;

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
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String recomLocName;

    private Double recomLocLat;

    private Double recomLocLong;

    private String middleLocName;

    private Double middleLocLat;

    private Double middleLocLong;

    @Column(nullable = false)
    private Boolean isAccepted;

}
