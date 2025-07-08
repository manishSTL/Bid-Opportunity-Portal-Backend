package com.portal.bid.service;

import java.util.List;
import java.util.Optional;

import com.portal.bid.entity.Agp;


public interface AgpService {
    List<Agp> findAll();
    Optional<Agp> findById(Long id);
    Agp save(Agp agp);
    void deleteById(Long id);
    Agp updateAgp(Agp agp);
}
