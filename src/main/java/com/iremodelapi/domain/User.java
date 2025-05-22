package com.iremodelapi.domain;

import java.time.LocalDateTime;
import java.util.Objects;
import jakarta.persistence.*;

// Annotations to mark this class as a JPA entity, specify the table name, and define inheritance strategy
@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)

public class User
{
    // @Id indentifies the attribute as the primary key, and GeneratedValue specifes how its generated and
    // automatically incremented
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // @Column specifies the column name in the database table, cannot be null, and must be unique
    @Column(nullable = false, unique = true)
    private String email;

    // @Column specifies the column name in the database table, cannot be null
    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false)
    private String phoneNumber;

    // @Enumerated specifies that the attribute is an enum, and the EnumType.STRING specifies that the enum
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    // Relationship to Messages / all users can send & receive messages
    @OneToMany(mappedBy = "sender", fetch = FetchType.LAZY)
    private List<Message> sentMessages = new ArrayList<>();

    @OneToMany(mappedBy = "recipient", fetch = FetchType.LAZY)
    private List<Message> receivedMessages = new ArrayList<>();

    // Relationship to Notifications / all users can receive notifications
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Notification> notifications = new ArrayList<>();

    public User()
    {
        // Default constructor
    }

    public User(String email, String password, String fullName, String phoneNumber, UserRole role) {
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.role = role;
    }

    // Getters and Setters for each attribute
    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
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

    public String getFullName()
    {
        return fullName;
    }

    public void setFullName(String fullName)
    {
        this.fullName = fullName;
    }

    public String getPhoneNumber()
    {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber)
    {
        this.phoneNumber = phoneNumber;
    }

    public UserRole getRole()
    {
        return role;
    }

    public void setRole(UserRole role)
    {
        this.role = role;
    }

    public LocalDateTime getCreatedAt()
    {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt)
    {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt()
    {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt)
    {
        this.updatedAt = updatedAt;
    }

    public List<Message> getSentMessages()
    {
        return sentMessages;
    }

    public void setSentMessages(List<Message> sentMessages)
    {
        this.sentMessages = sentMessages;
    }

    public List<Message> getReceivedMessages()
    {
        return receivedMessages;
    }

    public void setReceivedMessages(List<Message> receivedMessages)
    {
        this.receivedMessages = receivedMessages;
    }

    public List<Notification> getNotifications()
    {
        return notifications;
    }

    public void setNotifications(List<Notification> notifications)
    {
        this.notifications = notifications;
    }

    // Helper methods for relationship management
    public void addNotification(Notification notification)
    {
        notifications.add(notification);
        notification.setUser(this);
    }

    public void removeNotification(Notification notification)
    {
        notifications.remove(notification);
        notification.setUser(null);
    }

    // Lifecycle callbacks to set createdAt and updatedAt timestamps
    @PrePersist
    protected void onCreate()
    {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate()
    {
        updatedAt = LocalDateTime.now();
    }

    @Override
    public String toString()
    {
        return "User{" + "id=" + id + ", email='" + email + '\'' + ", fullName='" + fullName + '\'' +
                ", role=" + role + '}';
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return email.equals(user.email);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(email);
    }

    public enum UserRole
    {
        HOMEOWNER,
        CONTRACTOR,
        ADMIN
    }

}
