package guzman.SalesDashboard.controllers;

import guzman.SalesDashboard.dtos.UserDTO;
import guzman.SalesDashboard.entities.UserEntity;
import guzman.SalesDashboard.services.UserService;
import lombok.RequiredArgsConstructor;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final ModelMapper modelMapper;
    
   

    @GetMapping
    public ResponseEntity<?> getUsers(@RequestParam(required = false) String username,
                                     @RequestParam(required = false) String email
                                        ) 
        {
        
        if (username != null) {
            UserEntity userEntity = userService.getUserByUsername(username);
            UserDTO userDTO = modelMapper.map(userEntity, UserDTO.class);
            return new ResponseEntity<>(userDTO, HttpStatus.OK);
        }
        
        if (email != null) {
            UserEntity userEntity = userService.getUserByEmail(email);
            UserDTO userDTO = modelMapper.map(userEntity, UserDTO.class);
            return new ResponseEntity<>(userDTO, HttpStatus.OK);
        }
        
        // Si no se proporcionan parámetros, devolver todos los usuarios
        System.out.println(">>> El endpoint /api/v1/users fue alcanzado. <<<");
        List<UserEntity> userEntities = userService.getAllUsers();
        List<UserDTO> userDTOs = modelMapper.map(userEntities, new TypeToken<List<UserDTO>>() {}.getType());
        return new ResponseEntity<>(userDTOs, HttpStatus.OK);
    }

   
}
