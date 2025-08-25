package guzman.SalesDashboard.services.implementations;

import guzman.SalesDashboard.entities.UserEntity;
import guzman.SalesDashboard.exception.BusinessException;
import guzman.SalesDashboard.repositories.UserRepository;
import guzman.SalesDashboard.services.UserService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserEntity saveUser(UserEntity user) {
        if (user.getUsername() == null || user.getEmail() == null || user.getPassword() == null) {
            throw new BusinessException("Complete all the fields.", HttpStatus.BAD_REQUEST);
        }
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new BusinessException("E-mail already in use.", HttpStatus.CONFLICT);
        }
        return userRepository.save(user);
    }

    @Override
    public List<UserEntity> getAllUsers() {
        List<UserEntity> users = userRepository.findAll();
        if (users.isEmpty()) {
            throw new BusinessException("Users not found.", HttpStatus.NOT_FOUND);
        }
        System.out.println("Usuarios encontrados en el repositorio: " + users.size()); 

        return users;
    }

    @Override
    public UserEntity getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new BusinessException("Couldn't find an user with that username.", HttpStatus.NOT_FOUND));
    }

    @Override
    public UserEntity getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException("Couldn't find an user with that email.", HttpStatus.NOT_FOUND));
    }
}


