package com.portal.bid.repository;

import com.portal.bid.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User,Long> {


    User findByEmail(String email);
    @Query("SELECT u FROM User u WHERE LOWER(TRIM(u.firstName)) = LOWER(TRIM(:firstName)) " +
            "AND (LOWER(TRIM(u.lastName)) = LOWER(TRIM(:lastName)) OR :lastName IS NULL OR :lastName = '')")
    User findByFirstNameAndOptionalLastName(@Param("firstName") String firstName, @Param("lastName") String lastName);

    List<User> findByParent(User parent);

//    User findByUsername(String email);
    @Query("SELECT u FROM User u WHERE u.hierarchy_level = :hierarchy_level")
    List<User> findByHierarchyLevel(@Param("hierarchy_level") String hierarchy_level);
}
