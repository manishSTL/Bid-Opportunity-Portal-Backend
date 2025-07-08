package com.portal.bid.repository;

import com.portal.bid.entity.Form;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface FormRepository extends JpaRepository<Form,Long> {

    @Query("SELECT f FROM Form f WHERE " +
            "f.opportunity LIKE %:opportunity% OR " +
            "f.industrySegment = :industrySegment OR " +
            "f.businessUnit = :businessUnit OR " +
            "f.submissionDate = :submissionDate")
    List<Form> findPotentialDuplicates(
            @Param("opportunity") String opportunity,
            @Param("industrySegment") String industrySegment,
            @Param("businessUnit") String businessUnit,
            @Param("submissionDate") LocalDate submissionDate
    );
}


