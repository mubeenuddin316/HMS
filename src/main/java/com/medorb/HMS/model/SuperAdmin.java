package com.medorb.HMS.model;

import jakarta.persistence.*;

@Entity
@Table(name = "super_admins") // You can adjust table name if needed
public class SuperAdmin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "super_admin_id") // Adjust column name if needed
    private Integer superAdminId;

    @Column(name = "admin_name", nullable = false) // Adjust column name if needed
    private String adminName;

    @Column(name = "email", nullable = false, unique = true) // Adjust column name if needed
    private String email;

    @Column(name = "password", nullable = false) // Password column
    private String password;

    // No direct relationship to Hospital. Super Admins have system-wide access.

    public SuperAdmin() {
        // Default constructor
    }

    public Integer getSuperAdminId() {
        return superAdminId;
    }

    public void setSuperAdminId(Integer superAdminId) {
        this.superAdminId = superAdminId;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "SuperAdmin{" +
               "superAdminId=" + superAdminId +
               ", adminName='" + adminName + '\'' +
               ", email='" + email + '\'' +
               ", password='" + (password != null ? "[PROTECTED]" : null) + '\'' + // Protect password
               '}';
    }
}