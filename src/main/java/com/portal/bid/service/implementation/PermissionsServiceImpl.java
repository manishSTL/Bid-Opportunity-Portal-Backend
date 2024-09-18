package com.portal.bid.service.implementation;

import com.portal.bid.entity.Permissions;
import com.portal.bid.repository.PermissionsRepository;
import com.portal.bid.service.PermissionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PermissionsServiceImpl implements PermissionsService {

    private final PermissionsRepository permissionsRepository;

    @Autowired
    public PermissionsServiceImpl(PermissionsRepository permissionsRepository) {
        this.permissionsRepository = permissionsRepository;
    }

    @Override
    public List<Permissions> getAllPermissions() {
        return permissionsRepository.findAll();
    }

    @Override
    public Optional<Permissions> getPermissionById(int id) {
        return permissionsRepository.findById(id);
    }

    @Override
    public Permissions createPermission(Permissions permission) {
        return permissionsRepository.save(permission);
    }

    @Override
    public Permissions updatePermission(int id, Permissions permission) {
        if (permissionsRepository.existsById(id)) {
            Permissions existingPermission = permissionsRepository.findById(id).get();

            // Update only the fields that are not null
            if (permission.getPermission_name() != null) {
                existingPermission.setPermission_name(permission.getPermission_name());
            }
            if (permission.getCreatedBy() != null) {
                existingPermission.setCreatedBy(permission.getCreatedBy());
            }
            if (permission.getUpdatedBy() != null) {
                existingPermission.setUpdatedBy(permission.getUpdatedBy());
            }
            // Note: `createdAt` and `updatedAt` are managed automatically by the entity lifecycle methods

            return permissionsRepository.save(existingPermission);
        }
        return null;
    }


    @Override
    public boolean deletePermission(int id) {
        if (permissionsRepository.existsById(id)) {
            permissionsRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public String allPermission(int id){
        return permissionsRepository.findPermissionNameById(id);
    }

//    @Override
//    public Map<String, String> loadPermissionEndpointMap() {
//        List<Permissions> permissions = permissionsRepository.findAll();
//
//        // Convert the list of permissions to a map
//        return permissions.stream()
//                .collect(Collectors.toMap(Permissions::getPermission_name, Permissions::getEndpoint));
//    }
}
