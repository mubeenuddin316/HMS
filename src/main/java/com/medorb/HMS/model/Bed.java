package com.medorb.HMS.model;

import jakarta.persistence.*;

@Entity
@Table(name = "beds")
public class Bed {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bed_id")
    private Integer bedId;

    @Column(name = "bed_number", nullable = false, unique = true)
    private String bedNumber;
    
    @Column(name = "ward")
    private String ward;
    
    @Column(name = "room_number")
    private String roomNumber;

    @Column(name = "bed_type")
    private String bedType; // e.g., "Standard", "Deluxe", "ICU Bed"

    @Column(name = "is_occupied")
    private boolean isOccupied; // Boolean to track bed occupancy

    @Column(name = "bed_status")
    private String bedStatus; // e.g., "Available", "Occupied", "Under Maintenance"

    @ManyToOne
    @JoinColumn(name = "hospital_id", nullable = false)
    private Hospital hospital; // Many Beds belong to One Hospital

    public Bed() {
        // Default constructor
    }

    public Integer getBedId() {
        return bedId;
    }

    public void setBedId(Integer bedId) {
        this.bedId = bedId;
    }

    public String getBedNumber() {
        return bedNumber;
    }

    public void setBedNumber(String bedNumber) {
        this.bedNumber = bedNumber;
    }

    public String getWard() {
        return ward;
    }

    public void setWard(String ward) {
        this.ward = ward;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getBedType() {
        return bedType;
    }

    public void setBedType(String bedType) {
        this.bedType = bedType;
    }

    public boolean isOccupied() {
        return isOccupied;
    }

    public void setOccupied(boolean occupied) {
        isOccupied = occupied;
    }

    public String getBedStatus() {
        return bedStatus;
    }

    public void setBedStatus(String bedStatus) {
        this.bedStatus = bedStatus;
    }

    public Hospital getHospital() {
        return hospital;
    }

    public void setHospital(Hospital hospital) {
        this.hospital = hospital;
    }

    @Override
    public String toString() {
        return "Bed{" +
               "bedId=" + bedId +
               ", bedNumber='" + bedNumber + '\'' +
               ", ward='" + ward + '\'' +
               ", roomNumber='" + roomNumber + '\'' +
               ", bedType='" + bedType + '\'' +
               ", isOccupied=" + isOccupied +
               ", bedStatus='" + bedStatus + '\'' +
               ", hospital=" + (hospital != null ? hospital.getHospitalId() : null) +
               '}';
    }
}