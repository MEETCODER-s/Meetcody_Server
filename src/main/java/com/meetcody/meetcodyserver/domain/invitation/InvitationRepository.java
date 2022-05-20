package com.meetcody.meetcodyserver.domain.invitation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface InvitationRepository extends JpaRepository<Invitation, Long> {


    @Query("SELECT e FROM Invitation e WHERE e.user.id=?1")
    public List<Invitation> findAllByUserId(Long userid);

    @Query("SELECT e FROM Invitation e WHERE e.time.id=?1 AND e.status='NEEDACTION' ")
    public List<Invitation> findAllByTimeIdACeepted(Long timeId);

    @Query("SELECT e FROM Invitation e WHERE e.time.id=?1 AND e.role='INVITER'")
    public Invitation findByTimeIdAndRoleEqualsINVITER(Long timeId);

    @Transactional
    @Modifying
    @Query("UPDATE Invitation e SET e.startLocation=?1, e.status='ACCEPT' WHERE e.id=?2")
    public void updateStartlocationById (String startloc, Long id);

    @Query("SELECT e FROM Invitation e WHERE e.time.id=?1 AND e.status='ACCEPT'")
    public List<Invitation> findAllByTimeIdAndStatusEqualsACCEPT(Long timeId);

    public List<Invitation> findAllById(Long id);

    @Transactional
    @Modifying
    @Query("UPDATE Location e SET e.isAccepted=true WHERE e.id=?1")
    public void updateLocaionAceepted(Long locationid );

    @Transactional
    @Modifying
    @Query("UPDATE Time e SET  e.isAccepted=true WHERE e.id=?1")
    public void updateTimeAceepted(Long timeid );


    @Transactional
    @Modifying
    @Query("UPDATE Invitation e SET e.startLocationLat=?1, e.startLocationLong=?2 WHERE e.id=?3")
    public void updateStartLatLongById(Double lati, Double longi, Long id);


    @Transactional
    @Modifying
    @Query("UPDATE Invitation e SET e.status='DECLINE', e.status='ACCEPT'  WHERE  e.id=?1")
    public void updateNometter(Long id);

    @Query("SELECT e FROM Invitation e WHERE e.time.id=?1 AND e.status='DECLINE'")
    public List<Invitation> findAllByTimeIdAndStatusEqualsDECLINE (Long timeid);

//    @Query("SELECT e FROM Invitation  e WHERE e.id=?1 ")
//    public List<Invitation> findAllById(Long id);

}
