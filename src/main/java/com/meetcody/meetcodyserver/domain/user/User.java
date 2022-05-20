package com.meetcody.meetcodyserver.domain.user;

import com.meetcody.meetcodyserver.domain.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "user")
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(nullable = false)
    private String familyName;

    @Column(nullable = false)
    private String givenName;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String phone;

    @Column
    private String picture;

    public void update(String familyName, String givenName, String username, String email, String phone, String picture) {
        this.familyName = familyName;
        this.givenName = givenName;
        this.username = username;
        this.phone = phone;
        this.email = email;
        this.picture = picture;
    }

    public String getRoleKey() {
        return this.role.getKey();
    }
}
