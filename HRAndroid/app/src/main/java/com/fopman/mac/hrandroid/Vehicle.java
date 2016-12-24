package com.fopman.mac.hrandroid;

public class Vehicle implements IDisplayable {

	private String make;
	private String plate;

	public String getMake() {
		return make;
	}

	public void setMake(String make) {
		this.make = make;
	}

	public String getPlate() {
		return plate;
	}

	public void setPlate(String plate) {
		this.plate = plate;
	}

	
	public Vehicle(String make, String plate) {
		super();
		this.make = make;
		this.plate = plate;
	}


	@Override
	public String toString() {
		return "Vehicle [make=" + make + ", plate=" + plate + "]";
	}

	@Override
	public void displayData() {
       System.out.println("Make = " + this.make);
       System.out.println("Plate = " +  this.plate);
	}

}
