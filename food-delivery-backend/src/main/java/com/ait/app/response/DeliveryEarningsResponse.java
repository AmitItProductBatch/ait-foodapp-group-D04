package com.ait.app.response;

public class DeliveryEarningsResponse {

	private double dailyEarnings;
	private double weeklyEarnings;
	private double monthlyEarnings;
	private double totalEarnings;
	private long totalCompletedDeliveries;

	public double getDailyEarnings() {
		return dailyEarnings;
	}

	public void setDailyEarnings(double dailyEarnings) {
		this.dailyEarnings = dailyEarnings;
	}

	public double getWeeklyEarnings() {
		return weeklyEarnings;
	}

	public void setWeeklyEarnings(double weeklyEarnings) {
		this.weeklyEarnings = weeklyEarnings;
	}

	public double getMonthlyEarnings() {
		return monthlyEarnings;
	}

	public void setMonthlyEarnings(double monthlyEarnings) {
		this.monthlyEarnings = monthlyEarnings;
	}

	public double getTotalEarnings() {
		return totalEarnings;
	}

	public void setTotalEarnings(double totalEarnings) {
		this.totalEarnings = totalEarnings;
	}

	public long getTotalCompletedDeliveries() {
		return totalCompletedDeliveries;
	}

	public void setTotalCompletedDeliveries(long totalCompletedDeliveries) {
		this.totalCompletedDeliveries = totalCompletedDeliveries;
	}
}
