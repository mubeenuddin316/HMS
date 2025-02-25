package com.medorb.HMS;

import com.medorb.HMS.model.Hospital;
import com.medorb.HMS.repository.HospitalRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.Optional;

@SpringBootApplication
public class HospitalManagementSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(HospitalManagementSystemApplication.class, args);
    }

    @Bean
    public CommandLineRunner databaseCheck(HospitalRepository hospitalRepository) { // Renamed bean method for clarity
    	
        return args -> {
              System.out.println("----- Starting Database Connection and JPA Entity Check -----");
//
//            // 1. Create and Save a Hospital
//            System.out.println("\n1. Creating and Saving a new Hospital...");
//            Hospital hospital1 = new Hospital();
//            hospital1.setName("Ver Hospital");
//            hospital1.setAddress("Ver Address");
//            hospital1.setCity("Ver City");
//            hospital1.setPhoneNumber("123-446-7890");
//            hospital1.setEmail("ver@hospitala.com");
//            Hospital savedHospital = hospitalRepository.save(hospital1);
//            System.out.println("Hospital saved. ID: " + savedHospital.getHospitalId());
//
//            // 2. Find Hospital by ID
//            System.out.println("\n2. Finding Hospital by ID...");
//            Optional<Hospital> foundHospitalOpt = hospitalRepository.findById(savedHospital.getHospitalId());
//            if (foundHospitalOpt.isPresent()) {
//                Hospital foundHospital = foundHospitalOpt.get();
//                System.out.println("Hospital found: " + foundHospital);
//            } else {
//                System.out.println("Hospital NOT found for ID: " + savedHospital.getHospitalId()); // This should not happen if save was successful
//            }
//
//            // 3. Find All Hospitals
//            System.out.println("\n3. Finding all Hospitals...");
//            List<Hospital> allHospitals = hospitalRepository.findAll();
//            if (allHospitals.isEmpty()) {
//                System.out.println("No hospitals found in database (initially, might be okay).");
//            } else {
//                System.out.println("All Hospitals found:");
//                allHospitals.forEach(hospital -> System.out.println(hospital));
//            }

            System.out.println("\n----- Database Connection and JPA Entity Check Completed -----");
        };
    }
}