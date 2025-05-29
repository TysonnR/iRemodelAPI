package com.iremodelapi.web.dto;
import com.iremodelapi.domain.Job;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class JobResponseDTO {
    private Long id;
    private String title;
    private String description;
    private String category;
    private String zipCode;
    private BigDecimal budget;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Homeowner info (flattened to avoid proxy issues)
    private Long homeownerId;
    private String homeownerFirstName;
    private String homeownerLastName;
    private String homeownerEmail;
    private String homeownerPhone;

    // Constructors
    public JobResponseDTO() {}

    public JobResponseDTO(Long id, String title, String description, String category,
                          String zipCode, BigDecimal budget, String status,
                          LocalDateTime createdAt, LocalDateTime updatedAt,
                          Long homeownerId, String homeownerFirstName,
                          String homeownerLastName, String homeownerEmail,
                          String homeownerPhone) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.category = category;
        this.zipCode = zipCode;
        this.budget = budget;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.homeownerId = homeownerId;
        this.homeownerFirstName = homeownerFirstName;
        this.homeownerLastName = homeownerLastName;
        this.homeownerEmail = homeownerEmail;
        this.homeownerPhone = homeownerPhone;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public BigDecimal getBudget() {
        return budget;
    }

    public void setBudget(BigDecimal budget) {
        this.budget = budget;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Long getHomeownerId() {
        return homeownerId;
    }

    public void setHomeownerId(Long homeownerId) {
        this.homeownerId = homeownerId;
    }

    public String getHomeownerFirstName() {
        return homeownerFirstName;
    }

    public void setHomeownerFirstName(String homeownerFirstName) {
        this.homeownerFirstName = homeownerFirstName;
    }

    public String getHomeownerLastName() {
        return homeownerLastName;
    }

    public void setHomeownerLastName(String homeownerLastName) {
        this.homeownerLastName = homeownerLastName;
    }

    public String getHomeownerEmail() {
        return homeownerEmail;
    }

    public void setHomeownerEmail(String homeownerEmail) {
        this.homeownerEmail = homeownerEmail;
    }

    public String getHomeownerPhone() {
        return homeownerPhone;
    }

    public void setHomeownerPhone(String homeownerPhone) {
        this.homeownerPhone = homeownerPhone;
    }

    // Convenience method to get full homeowner name
    public String getHomeownerFullName() {
        return homeownerFirstName + " " + homeownerLastName;
    }

    // Static factory method
    public static JobResponseDTO fromJob(Job job) {
        return new JobResponseDTO(
                job.getId(),
                job.getTitle(),
                job.getDescription(),
                job.getCategory().toString(),
                job.getZipCode(),
                job.getBudget(),
                job.getStatus().toString(),
                job.getCreatedAt(),
                job.getUpdatedAt(),
                job.getHomeowner().getId(),
                job.getHomeowner().getFirstName(),
                job.getHomeowner().getLastName(),
                job.getHomeowner().getEmail(),
                job.getHomeowner().getPhoneNumber()
        );
    }
}