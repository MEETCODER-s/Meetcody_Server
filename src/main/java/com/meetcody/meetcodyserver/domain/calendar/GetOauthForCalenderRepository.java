package com.meetcody.meetcodyserver.domain.calendar;

import com.meetcody.meetcodyserver.domain.user.Oauth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GetOauthForCalenderRepository extends JpaRepository<Oauth, Long> {

    @Query("SELECT e FROM oauth  e WHERE e.user.id=?1")
    public List<Oauth> getGoogleOauthById (Long user_id);

}
