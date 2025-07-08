package com.portal.bid.service.implementation;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.portal.bid.entity.Agp;
import com.portal.bid.repository.AgpRepository;
import com.portal.bid.service.AgpService;


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

    @Override
    public Agp updateAgp(Agp agp) {
        // Ensure the Agp exists
        if (agp.getId() == null) {
            throw new IllegalArgumentException("Cannot update Agp without an ID");
        }
        
        // You might want to add additional validation here
        // For example, checking if the record exists before updating
        Optional<Agp> existingAgp = agpRepository.findById(agp.getId());
        if (existingAgp.isEmpty()) {
            throw new IllegalArgumentException("Cannot find Agp with ID: " + agp.getId());
        }

        // Perform the update
        return agpRepository.save(agp);
    }
}
