package guzman.SalesDashboard.services;

import guzman.SalesDashboard.entities.UserEntity;

import java.util.List;

public interface UserService {
    UserEntity saveUser(UserEntity user);
    List<UserEntity> getAllUsers();
    UserEntity getUserByUsername(String username);
    UserEntity getUserByEmail(String email);
    UserEntity getUserById(Long id);
    Long getUserIdByEmail(String email);
}
