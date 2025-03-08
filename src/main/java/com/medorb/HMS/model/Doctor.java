package com.medorb.HMS.model;

import jakarta.persistence.*;

@Entity
@Table(name = "doctors")
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "doctor_id")
    private Integer doctorId;

    // Many doctors -> One hospital
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_id", nullable = false)
    private Hospital hospital;

    @Column(nullable = false)
    private String name;

    private String specialization;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password; // Plain text for demo; in real app, hash it

    private String profileImagePath;

    @Lob
    private String doctorSchedule;
    
    public Doctor() {
    }
    
    public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}

	public Hospital getHospital() {
		return hospital;
	}

	public void setHospital(Hospital hospital) {
		this.hospital = hospital;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSpecialization() {
		return specialization;
	}

	public void setSpecialization(String specialization) {
		this.specialization = specialization;
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

	public String getProfileImagePath() {
		return profileImagePath;
	}

	public void setProfileImagePath(String profileImagePath) {
		this.profileImagePath = profileImagePath;
	}

	public String getDoctorSchedule() {
		return doctorSchedule;
	}

	public void setDoctorSchedule(String doctorSchedule) {
		this.doctorSchedule = doctorSchedule;
	}

	@Override
    public String toString() {
        return "Doctor{" +
               "doctorId=" + doctorId +
               ", hospital=" + (hospital != null ? hospital.getHospitalId() : null) +
               ", name='" + name + '\'' +
               ", specialization='" + specialization + '\'' +
               ", email='" + email + '\'' +
               ", password='" + password + '\'' +
               ", profileImagePath='" + profileImagePath + '\'' +
               ", doctorSchedule='" + doctorSchedule + '\'' +
               '}';
    }
}