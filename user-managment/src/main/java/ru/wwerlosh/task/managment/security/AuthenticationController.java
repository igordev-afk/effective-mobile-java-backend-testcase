package ru.wwerlosh.task.managment.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.wwerlosh.task.managment.validation.ResponseMessage;

@RestController
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/authenticate")
    public ResponseMessage authenticate(@RequestBody AuthenticationDTO authenticationDTO) {
        return authenticationService.authenticate(authenticationDTO);
    }

    @GetMapping("/validate_token")
    public ResponseEntity<?> validateToken(HttpServletRequest hsr) {
        String jwt = hsr.getHeader("Authorization");
        Long userId = Long.valueOf(hsr.getHeader("userId"));
        if (!authenticationService.isTokenValid(jwt, userId)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok().build();
    }
}
