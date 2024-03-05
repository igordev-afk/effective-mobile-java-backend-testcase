package ru.wwerlosh.task.managment.security;

import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import ru.wwerlosh.task.managment.user.User;
import ru.wwerlosh.task.managment.user.UserRepository;
import ru.wwerlosh.task.managment.validation.ResponseMessage;
import ru.wwerlosh.task.managment.validation.UserNotFoundException;

@Service
@Slf4j
public class AuthenticationService {

    private final UserRepository userRepository;

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    public AuthenticationService(UserRepository userRepository, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public ResponseMessage authenticate(AuthenticationDTO authenticationDTO) {
        User user = userRepository.findByEmail(authenticationDTO.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + authenticationDTO.getEmail()));
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authenticationDTO.getEmail(), authenticationDTO.getPassword()));
        String jwt = jwtService.generateToken(user);
        log.info("User {} authenticated successfully", authenticationDTO.getEmail());
        return new ResponseMessage(jwt);
    }

    public boolean isTokenValid(String jwt, Long userId) {
        String email = jwtService.extractUserName(jwt);
        return userRepository.existsByEmailAndId(email, userId);
    }
}
