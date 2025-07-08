package com.portal.bid.service;

import com.portal.bid.dto.ApprovalFormInfoDTO;
import com.portal.bid.dto.ApprovalNotificationDTO;
import com.portal.bid.dto.ApprovalRequestDTO;
import com.portal.bid.entity.ApprovalRequest;
import com.portal.bid.entity.ApprovalNotifications;
import com.portal.bid.entity.User;
import com.portal.bid.repository.ApprovalRequestRepository;
import com.portal.bid.repository.ApprovalNotificationsRepository;
import com.portal.bid.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ApprovalRequestService {

    @Value("${file.upload-dir}")
    private String uploadDir;
    @Autowired
    private UserService userService;

    @Autowired
    private ApprovalRequestRepository approvalRequestRepository;

    @Autowired
    private ApprovalNotificationsRepository approvalNotificationsRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private  OpportunityService formService;



    @Transactional
    public ApprovalRequestDTO createApprovalRequest(MultipartFile file, Long formId, String primaryOwner) throws Exception {
        // Generate a unique file name
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

        // Create the full path
        Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
        Path filePath = uploadPath.resolve(fileName);

        // Create directories if they don't exist
        Files.createDirectories(uploadPath);

        // Write the file
        Files.copy(file.getInputStream(), filePath);

        // Create and save ApprovalRequest with the file path
        ApprovalRequest approvalRequest = new ApprovalRequest(filePath.toString(), formId, primaryOwner);
        approvalRequest = approvalRequestRepository.save(approvalRequest);

        // Find L1 users and create notifications
        List<User> l1Users = userRepository.findByHierarchyLevel("L1");
        for (User user : l1Users) {
            if (user.getStatus() == User.Status.ACTIVE) {
                ApprovalNotifications notification = new ApprovalNotifications(approvalRequest.getId(), (long) user.getId());
                approvalNotificationsRepository.save(notification);
            }


        }

        return new ApprovalRequestDTO(approvalRequest);
    }


    public String checkActionPerformed(Long formId) {
        Optional<ApprovalRequest> optionalRequest = approvalRequestRepository.findByFormId(formId);

        // Print detailed information about the ApprovalRequest if present
        if (optionalRequest.isPresent()) {
            ApprovalRequest request = optionalRequest.get();
            System.out.println("Found ApprovalRequest: " + request); // This will now use the overridden toString()
//            return "yes".equalsIgnoreCase(request.getActionPerformed());
            if(request.getActionPerformed().equals("no")) return "no";
            else if(request.getActionPerformed().equals("yes")) return  "yes";
        } else {
            System.out.println("No ApprovalRequest found for formId: " + formId);
        }

        return "none";  // Return false if the request is not found
    }
    public Long getApprovalRequestIdByFormId(Long formId) {
        // This method should interact with your database to find the approval request ID
        // Example: return approvalRequestRepository.findApprovalRequestIdByFormId(formId);
        return approvalRequestRepository.findApprovalRequestIdByFormId(formId);
    }


    public List<ApprovalNotificationDTO> getApprovalNotifications(Long formId) {
        // Assuming you have a method to fetch approval request ID by form ID
        Long approvalRequestId = getApprovalRequestIdByFormId(formId);
        System.out.println("ApprRRID:  "+approvalRequestId);
        if (approvalRequestId == null) {
            return new ArrayList<>(); // Return empty if no approval request found
        }

        List<ApprovalNotifications> notifications = approvalNotificationsRepository.findByApprovalId(approvalRequestId);
        System.out.println("i m hereeeeeeeee1234311");
        List<ApprovalNotificationDTO> notificationDTOs = new ArrayList<>();

        for (ApprovalNotifications notification : notifications) {
            // Fetch the user details using userService
            Optional<User> userOptional = userService.getUserById(notification.getUserId()); // Get user by ID

            // Use map to extract the user's name if present, otherwise provide a default
            String userName = userOptional.map(user -> user.getFirstName() + " " + user.getLastName())
                    .orElse("Unknown User");

            // Create the DTO with the notification and user name
            ApprovalNotificationDTO dto = new ApprovalNotificationDTO(notification, userName);
            notificationDTOs.add(dto);
        }


        return notificationDTOs;
    }

    public List<ApprovalFormInfoDTO> getAllApprovalFormInfo() {
        List<ApprovalRequest> approvalRequests = approvalRequestRepository.findAll();

        return approvalRequests.stream().map(request -> {
            String formName = formService.getFormName(request.getFormId()); // Implement this method in FormService
            return new ApprovalFormInfoDTO(request.getId(), formName);
        }).collect(Collectors.toList());
    }

}