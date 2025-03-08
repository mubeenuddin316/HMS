package com.medorb.HMS.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "patients")
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "patient_id")
    private Integer patientId;

    @Column(nullable = false)
    private String name;

    private LocalDate dob; 
    private String gender; 
    private String contactNumber;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private String address;
    

    public Patient() {
    }
    

    public Integer getPatientId() {
		return patientId;
	}


	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public LocalDate getDob() {
		return dob;
	}


	public void setDob(LocalDate dob) {
		this.dob = dob;
	}


	public String getGender() {
		return gender;
	}


	public void setGender(String gender) {
		this.gender = gender;
	}


	public String getContactNumber() {
		return contactNumber;
	}


	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
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


	public String getAddress() {
		return address;
	}


	public void setAddress(String address) {
		this.address = address;
	}


	@Override
    public String toString() {
        return "Patient{" +
               "patientId=" + patientId +
               ", name='" + name + '\'' +
               ", dob=" + dob +
               ", gender='" + gender + '\'' +
               ", contactNumber='" + contactNumber + '\'' +
               ", email='" + email + '\'' +
               ", password='" + password + '\'' +
               ", address='" + address + '\'' +
               '}';
    }
}