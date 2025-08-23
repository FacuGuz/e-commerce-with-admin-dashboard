package guzman.SalesDashboard.services.implementations;

import guzman.SalesDashboard.dtos.UserDTO;
import guzman.SalesDashboard.entities.UserEntity;
import guzman.SalesDashboard.exception.BusinessException;
import guzman.SalesDashboard.repositories.UserRepository;
import guzman.SalesDashboard.services.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
   private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public UserEntity saveUser(UserEntity user) {
        if (user.getUsername() == null || user.getEmail() == null || user.getPassword() == null) {
            throw new BusinessException("Debe completar todos los campos.", HttpStatus.BAD_REQUEST);
        }
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new BusinessException("Ese email ya se encuentra en uso.", HttpStatus.CONFLICT);
        }
        return userRepository.save(user);
    }

    @Override
    public List<UserEntity> getAllUsers() {
        List<UserEntity> userEntities = userRepository.findAll();
        if (userEntities.isEmpty()) {
            throw new BusinessException("No se han encontrado usuarios.", HttpStatus.NOT_FOUND);
        }
        return modelMapper.map(userEntities, new TypeToken<List<UserDTO>>() {}.getType());
    }

    @Override
    public UserEntity getUserByUsername(String username) {
        Optional<UserEntity> usuario = userRepository.findByUsername(username);
        if (!usuario.isPresent()) {
            throw new BusinessException("No se han encontrado un usuario con ese nombre.", HttpStatus.NOT_FOUND);
        }
        return modelMapper.map(usuario, new TypeToken<UserDTO>() {}.getType());
        
    }

    @Override
    public UserEntity getUserByEmail(String email) {
       Optional<UserEntity> usuario = userRepository.findByEmail(email);
        if (!usuario.isPresent()) {
            throw new BusinessException("No se han encontrado un usuario con ese email.", HttpStatus.NOT_FOUND);
        }
        return modelMapper.map(usuario, new TypeToken<UserDTO>() {}.getType());
    }


}
