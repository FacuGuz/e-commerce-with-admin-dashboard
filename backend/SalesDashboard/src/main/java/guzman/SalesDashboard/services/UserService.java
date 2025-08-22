package guzman.SalesDashboard.services;

import guzman.SalesDashboard.entities.UserEntity;

import java.util.List;

public interface UserService {

    public UserEntity saveUser(UserEntity user);
    public List<UserEntity> getAllUsers();
    public UserEntity getUserByUsername(String username);
}
