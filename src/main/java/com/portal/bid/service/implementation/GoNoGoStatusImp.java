package com.portal.bid.service.implementation;

import com.portal.bid.entity.GoNoGoStatus;
import com.portal.bid.entity.PlanAction;
import com.portal.bid.repository.GoNoGoStatusRepository;
import com.portal.bid.service.GoNoGoStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GoNoGoStatusImp implements GoNoGoStatusService {

    @Autowired
    private GoNoGoStatusRepository goNoGoStatusRepository;

    @Override
    public GoNoGoStatus createEntry(GoNoGoStatus entry) {
        entry.setCreatedAt(LocalDateTime.now()); // Set the createdAt timestamp when creating a new entry
        return goNoGoStatusRepository.save(entry);
    }

    @Override
    public GoNoGoStatus updateEntry(GoNoGoStatus entry,Long id) {
        // Check if the entry exists in the repository

        Optional<GoNoGoStatus> existingEntry = goNoGoStatusRepository.findById(id);
        if (existingEntry.isPresent()) {
            GoNoGoStatus updatedEntry = existingEntry.get();
            updatedEntry.setFormId(entry.getFormId());
            updatedEntry.setDate(entry.getDate());
            updatedEntry.setStatus(entry.getStatus());
            updatedEntry.setCreatedBy(entry.getCreatedBy());
            updatedEntry.setCreatedAt(entry.getCreatedAt()); // Preserve original createdAt timestamp
            updatedEntry.setUpdatedAt(LocalDateTime.now());
            updatedEntry.setUpdatedBy(entry.getUpdatedBy());
            return goNoGoStatusRepository.save(updatedEntry);
        }
        return null;
    }

    @Override
    public GoNoGoStatus findbyID(Long id) {
        return goNoGoStatusRepository.findById(id).orElse(null);
    }

    @Override
    public List<GoNoGoStatus> findAll() {
        return goNoGoStatusRepository.findAll();
    }

    @Override
    public List<GoNoGoStatus> getAllGoNoGoStatus(Long formId) {
        return goNoGoStatusRepository.findAll().stream()
                .filter(plan -> plan.getFormId().equals(formId))
                .collect(Collectors.toList());
    }
}
