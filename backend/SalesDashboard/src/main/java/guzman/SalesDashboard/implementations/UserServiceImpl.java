package guzman.SalesDashboard.implementations;

import guzman.SalesDashboard.entities.UserEntity;
import guzman.SalesDashboard.entities.InvoiceEntity;
import guzman.SalesDashboard.repositories.UserRepository;
import guzman.SalesDashboard.repositories.InvoiceRepository;
import guzman.SalesDashboard.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final InvoiceRepository invoiceRepository;

    @Override
    public UserEntity saveUser(UserEntity user) {
        return userRepository.save(user);
    }

    @Override
    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public UserEntity getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    @Override
    public UserEntity getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    @Override
    public UserEntity getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public Long getUserIdByEmail(String email) {
        UserEntity user = getUserByEmail(email);
        return user != null ? user.getId() : null;
    }

    @Override
    public List<InvoiceEntity> getUserPurchases(Long userId) {
        return invoiceRepository.findByUserId(userId);
    }
}
