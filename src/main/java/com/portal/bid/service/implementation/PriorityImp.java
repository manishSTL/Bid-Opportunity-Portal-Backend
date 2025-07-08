package com.portal.bid.service.implementation;

import com.portal.bid.entity.Priority;
import com.portal.bid.repository.PriorityRepo;
import com.portal.bid.service.PriorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PriorityImp implements PriorityService {

    @Autowired
    private PriorityRepo priorityRepo;

    @Override
    public List<Priority> findAll() {
        return priorityRepo.findAll();
    }

    @Override
    public Optional<Priority> findById(Integer id) {
        return priorityRepo.findById(id);
    }

    @Override
    public Priority save(Priority entity) {
        return priorityRepo.save(entity);
    }

    @Override
    public void deleteById(Integer id) {
        priorityRepo.deleteById(id);

    }
}
