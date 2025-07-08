package com.portal.bid.repository;

import com.portal.bid.entity.Fy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FyRepository extends JpaRepository<Fy, Integer> {
}
