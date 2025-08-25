package com.portal.bid.service;

import java.util.List;
import java.util.Optional;

import com.portal.bid.entity.Fy;

public interface FyService {
    List<Fy> findAll();
    Optional<Fy> findById(int id);
    Fy save(Fy fy);
    void deleteById(int id);
    static Fy getFyById(Long selectedFyId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getFyById'");
    }
}
