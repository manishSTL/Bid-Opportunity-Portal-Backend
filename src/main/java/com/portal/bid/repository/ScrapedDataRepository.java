package com.portal.bid.repository;

import com.portal.bid.entity.ScrapedData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScrapedDataRepository extends JpaRepository<ScrapedData, Long> {
    // You can add custom query methods here if needed
}
