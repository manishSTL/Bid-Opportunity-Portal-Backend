package com.portal.bid.service;

import com.portal.bid.entity.Priority;

import java.util.List;
import java.util.Optional;

public interface PriorityService {
    List<Priority> findAll();
    Optional<Priority> findById(Integer id);
    Priority save(Priority entity);
    void deleteById(Integer id);
}
