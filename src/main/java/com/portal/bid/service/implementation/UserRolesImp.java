package com.portal.bid.service.implementation;

import com.portal.bid.entity.UserRoles;
import com.portal.bid.repository.UserRoleRepo;
import com.portal.bid.service.UserRolesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserRolesImp implements UserRolesService {

    @Autowired
    private UserRoleRepo userRoleRepo;

    @Override
    public UserRoles save(UserRoles userroles) {
       return userRoleRepo.save(userroles);
    }

    @Override
    public Optional<UserRoles> updateUser(int id, UserRoles userroles) {
        Optional<UserRoles> userRoles = userRoleRepo.findById(id);
        if(userRoles.isPresent()){
            UserRoles userRoles1 = userRoles.get();
            userRoles1.setRoleID(userroles.getRoleID());
            userRoles1.setUserID(userroles.getUserID());
            return Optional.of(userRoleRepo.save(userRoles1));
        }
    return Optional.empty();
    }

    @Override
    public Optional<UserRoles> get(int id) {
        return userRoleRepo.findById(id);
    }

    @Override
    public void delete(int id) {
        userRoleRepo.deleteById(id);

    }

    @Override
    public List<UserRoles> getAll() {
        return  userRoleRepo.findAll();
    }

    @Override
    public List<Integer> getAllofid(int id) {
        return  userRoleRepo.findRoleIdsByUserId(id);
    }
}
