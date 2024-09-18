package com.portal.bid.service.implementation;

import com.portal.bid.entity.Agp;
import com.portal.bid.repository.AgpRepository;
import com.portal.bid.service.AgpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class AgpImp implements AgpService {
    @Autowired
    private AgpRepository agpRepository;
    @Override
    public List<Agp> findAll() {
        return agpRepository.findAll();
    }

    @Override
    public Optional<Agp> findById(Long id) {
        return agpRepository.findById(id);    }

    @Override
    public Agp save(Agp agp) {
        return agpRepository.save(agp);    }

    @Override
    public void deleteById(Long id) {
        agpRepository.deleteById(id);
    }
}
