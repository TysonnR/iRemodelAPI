package com.iremodelapi.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class RegisterDTO
{
    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email address")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Please provide a valid phone number")
    private String phoneNumber;

    @NotBlank(message = "Role is required")
    @Pattern(regexp = "^(HOMEOWNER|CONTRACTOR)$", message = "Role must be either HOMEOWNER or CONTRACTOR")
    private String role;

    @NotBlank(message = "ZIP code is required")
    @Pattern(regexp = "^\\d{5}(-\\d{4})?$", message = "Please provide a valid ZIP code")
    private String zipCode;

    // Only filled if role = "CONTRACTOR"
    @Size(max = 120, message = "Company name cannot exceed 120 characters")
    private String companyName;

    // Only filled if role = "CONTRACTOR"
    @Size(max = 50, message = "Specialty cannot exceed 50 characters")
    private String primarySpecialty;

    public RegisterDTO()
    {
        // Default constructor
    }

    public RegisterDTO(String firstName, String lastName, String email, String password,
                        String phoneNumber, String role, String zipCode, String companyName,
                        String primarySpecialty)
    {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.zipCode = zipCode;
        this.companyName = companyName;
        this.primarySpecialty = primarySpecialty;
    }

    // Getters and Setters
    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getPhoneNumber()
    {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber)
    {
        this.phoneNumber = phoneNumber;
    }

    public String getRole()
    {
        return role;
    }

    public void setRole(String role)
    {
        this.role = role;
    }

    public String getZipCode()
    {
        return zipCode;
    }

    public void setZipCode(String zipCode)
    {
        this.zipCode = zipCode;
    }

    public String getCompanyName()
    {
        return companyName;
    }

    public void setCompanyName(String companyName)
    {
        this.companyName = companyName;
    }

    public String getPrimarySpecialty()
    {
        return primarySpecialty;
    }

    public void setPrimarySpecialty(String primarySpecialty)
    {
        this.primarySpecialty = primarySpecialty;
    }

    @Override
    public String toString()
    {
        return "RegisterDTO{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", role='" + role + '\'' +
                ", zipCode='" + zipCode + '\'' +
                ", companyName='" + companyName + '\'' +
                ", primarySpecialty='" + primarySpecialty + '\'' +
                '}';
    }

}
