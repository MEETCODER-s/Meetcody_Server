package com.meetcody.meetcodyserver.domain.phonebook;


import com.meetcody.meetcodyserver.domain.user.User;
import lombok.*;

import javax.persistence.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Phonebook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @Column
    private String familyName;

    @Column
    private String givenName;

    @Column
    private String email;

    @Column
    private String phone;

}
