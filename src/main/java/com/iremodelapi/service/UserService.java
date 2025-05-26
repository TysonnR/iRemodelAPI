package com.iremodelapi.service;

import com.iremodelapi.domain.*;
import com.iremodelapi.repository.*;
import com.iremodelapi.web.dto.RegisterDTO;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService
{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder)
    {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(RegisterDTO registerDTO)
    {
        // Check if user already exists
        if (userRepository.findByEmail(registerDTO.getEmail()).isPresent())
        {
            throw new RuntimeException("User with email " + registerDTO.getEmail() + " already exists");
        }

        User user;

        // Create the appropriate user type based on role
        if ("CONTRACTOR".equals(registerDTO.getRole()))
        {
            user = createContractor(registerDTO);
        }
        else if ("HOMEOWNER".equals(registerDTO.getRole()))
        {
            user = createHomeowner(registerDTO);
        }
        else
        {
            throw new RuntimeException("Invalid role: " + registerDTO.getRole());
        }

        return userRepository.save(user);
    }

    public User findByEmail(String email)
    {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }

    private Contractor createContractor(RegisterDTO registerDTO)
    {
        Contractor contractor = new Contractor();

        // Set User fields
        contractor.setEmail(registerDTO.getEmail());
        contractor.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        contractor.setFirstName(registerDTO.getFirstName());
        contractor.setLastName(registerDTO.getLastName());
        contractor.setPhoneNumber(registerDTO.getPhoneNumber());
        contractor.setRole(User.UserRole.CONTRACTOR);
        contractor.setZipCode(registerDTO.getZipCode());

        // Set Contractor-specific fields
        contractor.setCompanyName(registerDTO.getCompanyName() != null ?
                registerDTO.getCompanyName() : "");
        contractor.setRating(null); // New contractor starts with no rating
        contractor.setCompletedJobsCount(0);

        // Add service area (their zip code)
        contractor.addServiceArea(registerDTO.getZipCode());

        // Add specialty if provided
        if (registerDTO.getPrimarySpecialty() != null && !registerDTO.getPrimarySpecialty().isEmpty())
        {
            try
            {
                Contractor.Specialty specialty = Contractor.Specialty.valueOf(
                        registerDTO.getPrimarySpecialty().toUpperCase()
                );
                contractor.addSpecialty(specialty);
            }
            catch (IllegalArgumentException e) {
                // If specialty doesn't match enum, add GENERAL as default
                contractor.addSpecialty(Contractor.Specialty.GENERAL);
            }
        }

        return contractor;
    }

    private Homeowner createHomeowner(RegisterDTO registerDTO)
    {
        Homeowner homeowner = new Homeowner();

        // Set User fields
        homeowner.setEmail(registerDTO.getEmail());
        homeowner.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        homeowner.setFirstName(registerDTO.getFirstName());
        homeowner.setLastName(registerDTO.getLastName());
        homeowner.setPhoneNumber(registerDTO.getPhoneNumber());
        homeowner.setRole(User.UserRole.HOMEOWNER);
        homeowner.setZipCode(registerDTO.getZipCode());

        // Set Homeowner-specific defaults
        homeowner.setPreferredContactMethod("email");

        return homeowner;
    }
}