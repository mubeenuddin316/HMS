package com.medorb.HMS.dto;

import com.medorb.HMS.model.Hospital;

public class HospitalWithBedsDTO {
	
	    private Hospital hospital;
	    private long availableBeds;
	    private long totalBeds;
	    
	    
		public Hospital getHospital() {
			return hospital;
		}
		public void setHospital(Hospital hospital) {
			this.hospital = hospital;
		}
		public long getAvailableBeds() {
			return availableBeds;
		}
		public void setAvailableBeds(long availableBeds) {
			this.availableBeds = availableBeds;
		}
		public long getTotalBeds() {
			return totalBeds;
		}
		public void setTotalBeds(long totalBeds) {
			this.totalBeds = totalBeds;
		}
	    
	    

}
