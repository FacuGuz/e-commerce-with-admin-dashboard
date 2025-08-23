package guzman.SalesDashboard.Auth;

import guzman.SalesDashboard.entities.UserEntity;
import guzman.SalesDashboard.jwt.JwtService;
import guzman.SalesDashboard.repositories.Role;
import guzman.SalesDashboard.repositories.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthResponse login(LoginRequest request) {
       authenticationManager.authenticate(
               new UsernamePasswordAuthenticationToken(
                       request.getEmail(),
                       request.getPassword()
               )
       );
       UserEntity user = userRepository.findByEmail(request.getEmail()).orElseThrow();
       String token = jwtService.getToken(user);
       return AuthResponse.builder()
               .token(token).build();
       
    }

    public AuthResponse register(RegisterRequest request) {

if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email ya en uso");
        }

        UserEntity user = UserEntity.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .role(Role.USER)
                .build();

        userRepository.save(user);
        return AuthResponse.builder()
                .token(jwtService.getToken(user))
                .build();
    }
}
