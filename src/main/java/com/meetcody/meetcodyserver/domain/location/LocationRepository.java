package com.meetcody.meetcodyserver.domain.location;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {

    @Transactional
    @Modifying
    @Query("UPDATE Location e SET e.recomLocName=?1 WHERE e.id=?2")
    public void updateRecomLoc(String selectedLoc, Long locid);
}
