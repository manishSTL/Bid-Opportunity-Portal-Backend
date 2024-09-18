package com.portal.bid.repository;

import com.portal.bid.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {


    User findByEmail(String email);
    @Query("SELECT u FROM User u WHERE LOWER(TRIM(u.firstName)) = LOWER(TRIM(:firstName)) " +
            "AND (LOWER(TRIM(u.lastName)) = LOWER(TRIM(:lastName)) OR :lastName IS NULL OR :lastName = '')")
    User findByFirstNameAndOptionalLastName(@Param("firstName") String firstName, @Param("lastName") String lastName);


//    User findByUsername(String email);
}