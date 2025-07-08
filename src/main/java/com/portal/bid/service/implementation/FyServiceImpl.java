package com.portal.bid.service.implementation;


import com.portal.bid.entity.Fy;
import com.portal.bid.repository.FyRepository;
import com.portal.bid.service.FyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FyServiceImpl implements FyService {

    @Autowired
    private FyRepository fyRepository;

    @Override
    public List<Fy> findAll() {
        return fyRepository.findAll();
    }

    @Override
    public Optional<Fy> findById(int id) {
        return fyRepository.findById(id);
    }

    @Override
    public Fy save(Fy fy) {
        return fyRepository.save(fy);
    }

    @Override
    public void deleteById(int id) {
        fyRepository.deleteById(id);
    }
}
