package com.medorb.HMS.service;

import java.util.List;

import com.medorb.HMS.dto.GenderDistributionDTO;
import com.medorb.HMS.dto.TimeSeriesDTO;


public interface AnalyticsService {
	
	public List<TimeSeriesDTO> getMonthlyAppointmentsVsQueue();
	List<GenderDistributionDTO> getGenderDistributionData();
	
	

}
