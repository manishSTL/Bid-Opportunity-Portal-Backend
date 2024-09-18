package com.portal.bid.service;

import com.portal.bid.entity.Fy;

import java.util.List;
import java.util.Optional;

public interface FyService {
    List<Fy> findAll();
    Optional<Fy> findById(int id);
    Fy save(Fy fy);
    void deleteById(int id);
}
