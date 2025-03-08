package com.medorb.HMS.model;

import jakarta.persistence.*;

@Entity
@Table(name = "hospital_admins")
public class HospitalAdmin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hospital_admin_id")
    private Integer hospitalAdminId;

    @Column(name = "admin_name", nullable = false)
    private String adminName;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "phone_number")
    private String phoneNumber;

    // Many admins -> One hospital
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_id", nullable = false)
    private Hospital hospital;

    public HospitalAdmin() {
        // Default constructor
    }

    public Integer getHospitalAdminId() {
        return hospitalAdminId;
    }

    public void setHospitalAdminId(Integer hospitalAdminId) {
        this.hospitalAdminId = hospitalAdminId;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Hospital getHospital() {
        return hospital;
    }

    public void setHospital(Hospital hospital) {
        this.hospital = hospital;
    }

    @Override
    public String toString() {
        return "HospitalAdmin{" +
               "hospitalAdminId=" + hospitalAdminId +
               ", adminName='" + adminName + '\'' +
               ", email='" + email + '\'' +
               ", password='" + (password != null ? "[PROTECTED]" : null) + '\'' + // Protect password in toString
               ", phoneNumber='" + phoneNumber + '\'' +
               ", hospital=" + (hospital != null ? hospital.getHospitalId() : null) + // Display hospitalId
               '}';
    }
}