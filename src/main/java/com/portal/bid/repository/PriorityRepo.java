package com.portal.bid.repository;

import com.portal.bid.entity.Priority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PriorityRepo extends JpaRepository<Priority,Integer> {
}
