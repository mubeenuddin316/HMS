package com.medorb.HMS.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.medorb.HMS.dto.HospitalWithBedsDTO;
import com.medorb.HMS.model.Hospital;
import com.medorb.HMS.service.BedService;
import com.medorb.HMS.service.HospitalService;

@Controller
public class HeroPageController {

    private final HospitalService hospitalService;
    private final BedService bedService;

    @Autowired
    public HeroPageController(HospitalService hospitalService,
    		                  BedService bedService) {
        this.hospitalService = hospitalService;
        this.bedService = bedService;
    }

    @GetMapping("/hero")
    public String showHeroPage(Model model) {
        List<Hospital> hospitals = hospitalService.getAllHospitals();
        List<HospitalWithBedsDTO> hospitalDTOs = new ArrayList<>();

        for (Hospital h : hospitals) {
            long totalBeds = bedService.countByHospital(h);
            long availableBeds = bedService.countByHospitalAndIsOccupied(h, false);
            
            HospitalWithBedsDTO dto = new HospitalWithBedsDTO();
            dto.setHospital(h);
            dto.setTotalBeds(totalBeds);
            dto.setAvailableBeds(availableBeds);
            hospitalDTOs.add(dto);
        }

        model.addAttribute("hospitalsWithBeds", hospitalDTOs);
        return "hero";
    }

}

