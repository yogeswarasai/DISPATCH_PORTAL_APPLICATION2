package com.iocl.Dispatch_Portal_Application.DTO;

public class MstCourierContractRateDto {
    public MstCourierContractRateDto(String courierContNo, double fromWtGms, double toWtGms, double fromDistanceKm,
			double toDistanceKm, double rate) {
		super();
		this.courierContNo = courierContNo;
		this.fromWtGms = fromWtGms;
		this.toWtGms = toWtGms;
		this.fromDistanceKm = fromDistanceKm;
		this.toDistanceKm = toDistanceKm;
		this.rate = rate;
	}
	private String courierContNo;

	    private double fromWtGms;
	    private double toWtGms;
	    private double fromDistanceKm;
	    private double toDistanceKm;
	    private double rate;
		public double getFromWtGms() {
			return fromWtGms;
		}
		public void setFromWtGms(double fromWtGms) {
			this.fromWtGms = fromWtGms;
		}
		public double getToWtGms() {
			return toWtGms;
		}
		public void setToWtGms(double toWtGms) {
			this.toWtGms = toWtGms;
		}
		public double getFromDistanceKm() {
			return fromDistanceKm;
		}
		public void setFromDistanceKm(double fromDistanceKm) {
			this.fromDistanceKm = fromDistanceKm;
		}
		public double getToDistanceKm() {
			return toDistanceKm;
		}
		public void setToDistanceKm(double toDistanceKm) {
			this.toDistanceKm = toDistanceKm;
		}
		public double getRate() {
			return rate;
		}
		public void setRate(double rate) {
			this.rate = rate;
		}

	    
}
