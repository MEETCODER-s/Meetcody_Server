package com.meetcody.meetcodyserver.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface OauthRepository extends JpaRepository<Oauth, Long> {

    @Query("SELECT e FROM oauth e WHERE e.email=?1")
    public List<Oauth> findOauthByEmail (String email);

    @Query("DELETE FROM oauth e WHERE e.id=?1")
    public void deleteByExpiresIn (Long oauthId);
}
