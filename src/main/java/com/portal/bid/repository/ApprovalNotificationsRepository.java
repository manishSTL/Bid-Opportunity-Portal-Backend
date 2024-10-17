package com.portal.bid.repository;

import com.portal.bid.entity.ApprovalNotifications;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApprovalNotificationsRepository extends JpaRepository<ApprovalNotifications, Long> {

    @Query(value = "SELECT * FROM approval_notifications WHERE approval_id = :approvalId", nativeQuery = true)
    List<ApprovalNotifications> findByApprovalId(@Param("approvalId") Long approvalId);

    List<ApprovalNotifications> findByUserId(Long userId);

}

