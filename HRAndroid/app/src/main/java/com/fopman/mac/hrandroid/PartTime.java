package com.fopman.mac.hrandroid;

public class PartTime extends Employee {
	private int hoursWorked;
	private int rate;

	public int getHours() {
		return hoursWorked;
	}

	public void setHours(int hoursWorked) {
		this.hoursWorked = hoursWorked;
	}

	public int getRate() {
		return rate;
	}

	public void setRate(int rate) {
		this.rate = rate;
	}

	public PartTime(String name, int age, Vehicle v, int hoursWorked, int rate) {
		super(name, age, v);
		this.hoursWorked = hoursWorked;
		this.rate = rate;
	}

	int calcEarnings(){
		return hoursWorked + rate;
	}


	@Override
	public String toString() {
		return super.toString() + "\nPartTime [hoursWorked=" + hoursWorked + ", rate=" + rate + "]";
	}

	@Override
	public void displayData() {
		System.out.println( toString() );
	}


	
}
