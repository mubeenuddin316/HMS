package com.medorb.HMS.service;

import com.medorb.HMS.model.Hospital;
import com.medorb.HMS.model.HospitalAdmin;
import com.medorb.HMS.model.SuperAdmin;
import com.medorb.HMS.repository.BedRepository;
import com.medorb.HMS.repository.DoctorRepository;
import com.medorb.HMS.repository.HospitalAdminRepository;
import com.medorb.HMS.repository.HospitalRepository;
import com.medorb.HMS.repository.PatientRepository;
import com.medorb.HMS.repository.SuperAdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class SuperAdminServiceImpl implements SuperAdminService {

	private final SuperAdminRepository superAdminRepository;
    private final HospitalRepository hospitalRepository;
    private final HospitalAdminRepository hospitalAdminRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final BedRepository bedRepository;
    private final AppointmentService appointmentService;

    @Autowired
    public SuperAdminServiceImpl(SuperAdminRepository superAdminRepository,
                                 HospitalRepository hospitalRepository,
                                 HospitalAdminRepository hospitalAdminRepository,
                                 PatientRepository patientRepository,
                                 DoctorRepository doctorRepository,
                                 BedRepository bedRepository,
                                 AppointmentService appointmentService) {
        this.superAdminRepository = superAdminRepository;
        this.hospitalRepository = hospitalRepository;
        this.hospitalAdminRepository = hospitalAdminRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.bedRepository = bedRepository;
        this.appointmentService = appointmentService;
    }

    @Override
    public List<SuperAdmin> getAllSuperAdmins() {
        return superAdminRepository.findAll();
    }

    @Override
    public Optional<SuperAdmin> getSuperAdminById(Integer superAdminId) {
        return superAdminRepository.findById(superAdminId);
    }

    @Override
    public SuperAdmin createSuperAdmin(SuperAdmin superAdmin) {
        // **IMPORTANT: Hash password before saving! (Production Security)**
        return superAdminRepository.save(superAdmin);
    }

    @Override
    public SuperAdmin updateSuperAdmin(Integer superAdminId, SuperAdmin superAdmin) {
        if (superAdminRepository.existsById(superAdminId)) {
            superAdmin.setSuperAdminId(superAdminId);
            // **IMPORTANT: Hash password if it's being updated! (Production Security)**
            return superAdminRepository.save(superAdmin);
        }
        return null; // Super Admin not found
    }

    @Override
    public void deleteSuperAdmin(Integer superAdminId) {
        superAdminRepository.deleteById(superAdminId);
    }

    @Override
    public Optional<SuperAdmin> getSuperAdminByEmail(String email) { // Correct return type to Optional<SuperAdmin>
        return superAdminRepository.findByEmail(email); // Directly return repository result (which is now Optional<SuperAdmin>)
    }

    // --- Super Admin's Control over Hospitals (Implemented using HospitalRepository) ---

    @Override
    public List<Hospital> getAllHospitals() {
        return hospitalRepository.findAll(); // Super Admin can fetch all hospitals
    }

    @Override
    public Optional<Hospital> getHospitalById(Integer hospitalId) {
        return hospitalRepository.findById(hospitalId); // Super Admin can fetch any hospital by ID
    }

    @Override
    public Hospital createHospitalBySuperAdmin(Hospital hospital) {
        return hospitalRepository.save(hospital); // Super Admin can create hospitals
    }

    @Override
    public Hospital updateHospitalBySuperAdmin(Integer hospitalId, Hospital hospital) {
        if (hospitalRepository.existsById(hospitalId)) {
            hospital.setHospitalId(hospitalId);
            return hospitalRepository.save(hospital); // Super Admin can update any hospital
        }
        return null; // Hospital not found
    }

    @Override
    public void deleteHospitalBySuperAdmin(Integer hospitalId) {
        hospitalRepository.deleteById(hospitalId); // Super Admin can delete any hospital
    }

    // --- Super Admin's Control over Hospital Admins (Implemented using HospitalAdminRepository) ---

    @Override
    public List<HospitalAdmin> getAllHospitalAdmins() {
        return hospitalAdminRepository.findAll(); // Super Admin can fetch all hospital admins system-wide
    }

    @Override
    public Optional<HospitalAdmin> getHospitalAdminById(Integer hospitalAdminId) {
        return hospitalAdminRepository.findById(hospitalAdminId); // Super Admin can fetch any hospital admin by ID
    }

    @Override
    public HospitalAdmin createHospitalAdminBySuperAdmin(HospitalAdmin hospitalAdmin) {
        // **IMPORTANT: Hash password before saving! (Production Security)**
        return hospitalAdminRepository.save(hospitalAdmin); // Super Admin can create hospital admins
    }

    @Override
    public HospitalAdmin updateHospitalAdminBySuperAdmin(Integer hospitalAdminId, HospitalAdmin hospitalAdmin) {
        if (hospitalAdminRepository.existsById(hospitalAdminId)) {
            hospitalAdmin.setHospitalAdminId(hospitalAdminId);
            // **IMPORTANT: Hash password if being updated! (Production Security)**
            return hospitalAdminRepository.save(hospitalAdmin); // Super Admin can update any hospital admin
        }
        return null; // Hospital Admin not found
    }

    @Override
    public void deleteHospitalAdminBySuperAdmin(Integer hospitalAdminId) {
        hospitalAdminRepository.deleteById(hospitalAdminId); // Super Admin can delete any hospital admin
    }
    
    // 🆕 Implement Dashboard Methods
    @Override
    public long getTotalHospitals() {
        return hospitalRepository.count();
    }

    @Override
    public long getTotalPatients() {
        return patientRepository.count();
    }

    @Override
    public long getTotalDoctors() {
        return doctorRepository.count();
    }

    @Override
    public long getOccupiedBeds() {
        return bedRepository.countByIsOccupiedTrue();
    }

    @Override
    public long getTotalBeds() {
        return bedRepository.count();
    }
    
    @Override
    public long getTodaysAppointmentsCount() {
        return appointmentService.countTodaysAppointments();
    }

}