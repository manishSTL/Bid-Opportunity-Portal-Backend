package com.portal.bid.repository;

import com.portal.bid.entity.ApprovalRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ApprovalRequestRepository extends JpaRepository<ApprovalRequest, Long> {

    @Query(value = "SELECT * FROM approval_request WHERE form_id = :formId", nativeQuery = true)
    Optional<ApprovalRequest> findByFormId(@Param("formId") Long formId);

    @Query(value = "SELECT id FROM approval_request WHERE form_id = :formId", nativeQuery = true)
    Long findApprovalRequestIdByFormId(@Param("formId") Long formId);

    Optional<ApprovalRequest> findById(Long id);

}
