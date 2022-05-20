package com.meetcody.meetcodyserver.domain.friend;


import com.meetcody.meetcodyserver.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendRepository extends JpaRepository<Friend, Long> {

    @Query("SELECT e FROM user e WHERE e.phone=?1")
    public List<User> findUserByPhone (String phone);
}
