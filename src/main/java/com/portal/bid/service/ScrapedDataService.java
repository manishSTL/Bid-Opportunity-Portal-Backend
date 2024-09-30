package com.portal.bid.service;

import com.portal.bid.entity.ScrapedData;
import com.portal.bid.repository.ScrapedDataRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ScrapedDataService {

    @Autowired
    private ScrapedDataRepository scrapedDataRepository;

    @Transactional
    public void processAndStore(ScrapedData data) {
        // Add any necessary processing logic here
        // For example, you might want to validate the data or transform it in some way
        System.out.println(data.getTenderId());
        // If the tenderId is not provided, you might want to generate one
        if (data.getTenderId() == null || data.getTenderId().isEmpty()) {
            System.out.println("hey iddddddddddddddddd");
            System.out.println(generateTenderId());

            data.setTenderId(generateTenderId());
        }

        // Save the data to the database
        scrapedDataRepository.save(data);
    }

    private String generateTenderId() {
        // Implement logic to generate a unique tender ID
        // This is just a simple example, you might want to use a more sophisticated approach
        return "TEN" + System.currentTimeMillis();
    }
}