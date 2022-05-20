package com.meetcody.meetcodyserver.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long>  {
    User findByEmail(String email);

    @Query("SELECT e FROM user e WHERE e.email=?1")
    public List<User> findUserByEmail (String email);

    @Query("SELECT e FROM user e WHERE e.phone=?1")
    public List<User> findUserByPhone (String phone);

    @Query("SELECT e FROM  user  e WHERE e.id=?1")
    public List<User> findUserById (Long id);

    
}
