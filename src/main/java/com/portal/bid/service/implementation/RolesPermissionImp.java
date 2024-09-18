package com.portal.bid.service.implementation;

import com.portal.bid.entity.RolesPermission;
import com.portal.bid.repository.RolesPermissionRepo;
import com.portal.bid.service.RolesPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class RolesPermissionImp implements RolesPermissionService {

    @Autowired
    private RolesPermissionRepo rolesPermissionRepo;

    @Override
    public RolesPermission save(RolesPermission rolesPermission) {
        return rolesPermissionRepo.save(rolesPermission);
    }

    @Override
    public List<RolesPermission> findAll() {
        return rolesPermissionRepo.findAll();
    }

    @Override
    public Optional<RolesPermission> findByID(int id) {
        return rolesPermissionRepo.findById(id);
    }

    @Override
    public Optional<RolesPermission> update(int id, RolesPermission rp) {
        Optional<RolesPermission> rolesPermission1 = rolesPermissionRepo.findById(id);
        if(rolesPermission1.isPresent()){
            RolesPermission rolesPermission = rolesPermission1.get();
            rolesPermission.setPermissionId(rp.getPermissionId());
            rolesPermission.setRoleId(rp.getRoleId());
            return Optional.of(rolesPermissionRepo.save(rolesPermission));

        }
        return Optional.empty();
    }

    @Override
    public void deletebyID(int id) {
        rolesPermissionRepo.deleteById(id);

    }

    public List<Integer> getPermissions(int id){
        return rolesPermissionRepo.findPermissionIdsByRoleId(id);
    }


}
