package com.portal.bid.repository;

import com.portal.bid.entity.Deal;
import com.portal.bid.entity.GoNoGoMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GoNoGoMasterRepository extends JpaRepository<GoNoGoMaster, Integer> {
}
