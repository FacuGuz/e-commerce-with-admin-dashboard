package guzman.SalesDashboard.services;

import guzman.SalesDashboard.entities.UserEntity;
import guzman.SalesDashboard.entities.InvoiceEntity;

import java.util.List;

public interface UserService {
    UserEntity saveUser(UserEntity user);
    List<UserEntity> getAllUsers();
    UserEntity getUserByUsername(String username);
    UserEntity getUserByEmail(String email);
    UserEntity getUserById(Long id);
    Long getUserIdByEmail(String email);
    List<InvoiceEntity> getUserPurchases(Long userId);
}
