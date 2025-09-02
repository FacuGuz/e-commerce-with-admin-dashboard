package guzman.SalesDashboard.implementations;

import guzman.SalesDashboard.entities.UserEntity;
import guzman.SalesDashboard.repositories.UserRepository;
import guzman.SalesDashboard.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

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
        System.out.println("=== UserServiceImpl.getUserByEmail ===");
        System.out.println("Buscando usuario con email: '" + email + "'");
        
        try {
            var result = userRepository.findByEmail(email);
            System.out.println("Resultado de findByEmail: " + result);
            
            if (result.isPresent()) {
                UserEntity user = result.get();
                System.out.println("Usuario encontrado en BD: " + user);
                System.out.println("User ID en BD: " + user.getId());
                return user;
            } else {
                System.out.println("No se encontró usuario en BD con email: " + email);
                return null;
            }
        } catch (Exception e) {
            System.out.println("ERROR en getUserByEmail: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
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
}
