package com.iremodelapi.web.controller;

import com.iremodelapi.domain.User;
import com.iremodelapi.security.JwtService;
import com.iremodelapi.service.UserService;
import com.iremodelapi.web.dto.AuthResponseDTO;
import com.iremodelapi.web.dto.LoginDTO;
import com.iremodelapi.web.dto.RegisterDTO;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController
{

    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;

    public AuthController(UserService userService, JwtService jwtService,
                          AuthenticationManager authenticationManager,
                          UserDetailsService userDetailsService)
    {
        this.userService = userService;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@RequestBody @Valid RegisterDTO registerDTO)
    {
        try {
            // Register the user (creates Contractor or Homeowner)
            User user = userService.registerUser(registerDTO);

            // Load UserDetails for JWT generation
            UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());

            // Generate JWT token
            String token = jwtService.generateToken(userDetails);

            AuthResponseDTO response = new AuthResponseDTO(
                    token,
                    user.getEmail(),
                    user.getRole().name()
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        }
        catch (RuntimeException e)
        {
            return ResponseEntity.badRequest().body(
                    new AuthResponseDTO(null, null, "ERROR: " + e.getMessage())
            );
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody @Valid LoginDTO loginDTO)
    {
        try {
            // Authenticate the user credentials
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword())
            );

            // Get UserDetails from authentication
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            // Get user for role info
            User user = userService.findByEmail(loginDTO.getEmail());

            // Generate JWT token
            String token = jwtService.generateToken(userDetails);

            AuthResponseDTO response = new AuthResponseDTO(
                    token,
                    user.getEmail(),
                    user.getRole().name()
            );

            return ResponseEntity.ok(response);

        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    new AuthResponseDTO(null, null, "ERROR! Authentication failed")
            );
        }
    }
}