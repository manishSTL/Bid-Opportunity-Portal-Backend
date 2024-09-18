package com.portal.bid.service;

import com.portal.bid.entity.Agp;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


public interface AgpService {
    List<Agp> findAll();
    Optional<Agp> findById(Long id);
    Agp save(Agp agp);
    void deleteById(Long id);
}
