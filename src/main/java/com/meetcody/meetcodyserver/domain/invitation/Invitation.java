package com.meetcody.meetcodyserver.domain.invitation;

import com.meetcody.meetcodyserver.domain.BaseTimeEntity;
import com.meetcody.meetcodyserver.domain.invitation.enums.*;
import com.meetcody.meetcodyserver.domain.location.Location;
import com.meetcody.meetcodyserver.domain.time.Time;
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
public class Invitation extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name="time_id")
    private Time time;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;

    // TODO("장소번호 추가")

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private Boolean isLocation;

    @Column(nullable = false)
    private Boolean isTime;

    @Column(nullable = false)
    private LocalDateTime expired;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TimeSlot timeSlot;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Form form;

    @Column(nullable = false)
    private LocalDateTime created;

    @Column
    private String startLocation;

    @Column
    private Long totalInvitees;

    @Column
    private Double startLocationLong;

    @Column
    private Double startLocationLat;

    public String getTimeSlotKey() {
        return this.timeSlot.getKey();
    }

    public String getRoleKey() {
        return this.role.getKey();
    }

    public String getStatusKey() {
        return this.status.getKey();
    }

    public String getCategoryKey() {
        return this.category.getKey();
    }
    public String getFormKey() {
        return this.form.getKey();
    }
}
