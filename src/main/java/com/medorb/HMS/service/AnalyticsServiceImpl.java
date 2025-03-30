package com.medorb.HMS.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.medorb.HMS.dto.GenderDistributionDTO;
import com.medorb.HMS.dto.HospitalGenderCountDTO;
import com.medorb.HMS.dto.TimeSeriesDTO;
import com.medorb.HMS.repository.AnalyticsRepository;
import com.medorb.HMS.repository.AppointmentRepository;

@Service
public class AnalyticsServiceImpl implements AnalyticsService {
	
	private final AnalyticsRepository analyticsRepository;
	private final AppointmentRepository appointmentRepository;
	
	    @Autowired
	    public AnalyticsServiceImpl(AnalyticsRepository analyticsRepository, AppointmentRepository appointmentRepository) {
	    	this.appointmentRepository = appointmentRepository;
	    	this.analyticsRepository = analyticsRepository;
	    
	    }
        
	    
	    @Override
	    public List<TimeSeriesDTO> getMonthlyAppointmentsVsQueue() {
	        // 1) Fetch raw data
	        List<Object[]> apptRows = analyticsRepository.getMonthlyAppointmentCounts(); 
	        List<Object[]> queueRows = analyticsRepository.getMonthlyOpdQueueCounts();

	        // 2) Convert to maps: periodLabel -> count
	        Map<String, Long> apptMap = new HashMap<>();
	        for (Object[] row : apptRows) {
	            String period = (String) row[0];
	            Long count = ((Number) row[1]).longValue();
	            apptMap.put(period, count);
	        }

	        Map<String, Long> queueMap = new HashMap<>();
	        for (Object[] row : queueRows) {
	            String period = (String) row[0];
	            Long count = ((Number) row[1]).longValue();
	            queueMap.put(period, count);
	        }

	        // 3) Combine the results
	        //    For each period in either map, build a TimeSeriesDTO
	        Set<String> allPeriods = new HashSet<>();
	        allPeriods.addAll(apptMap.keySet());
	        allPeriods.addAll(queueMap.keySet());

	        List<TimeSeriesDTO> result = new ArrayList<>();
	        for (String period : allPeriods) {
	            long apptCount = apptMap.getOrDefault(period, 0L);
	            long queueCount = queueMap.getOrDefault(period, 0L);
	            result.add(new TimeSeriesDTO(period, apptCount, queueCount));
	        }

	        // 4) Sort by period if needed
	        // e.g. if "period" is "2025-03", you can custom-sort or rely on string order
	        result.sort(Comparator.comparing(TimeSeriesDTO::getPeriodLabel));

	        return result;
	    }
	    
	    @Override
	    public List<GenderDistributionDTO> getGenderDistributionData() {
	        List<Object[]> totalResults = analyticsRepository.findTotalAppointmentCountByGender();
	        List<GenderDistributionDTO> distributionList = new ArrayList<>();
	        for (Object[] row : totalResults) {
	            String gender = (String) row[0];
	            long totalCount = (Long) row[1];

	            List<Object[]> hospitalRows = analyticsRepository.findHospitalCountsByGender(gender);
	            List<HospitalGenderCountDTO> hospitalCounts = new ArrayList<>();
	            for (Object[] hrow : hospitalRows) {
	                String hospitalName = (String) hrow[0];
	                long count = (Long) hrow[1];
	                hospitalCounts.add(new HospitalGenderCountDTO(hospitalName, count));
	            }
	            distributionList.add(new GenderDistributionDTO(gender, totalCount, hospitalCounts));
	        }
	        return distributionList;
	    }

}
