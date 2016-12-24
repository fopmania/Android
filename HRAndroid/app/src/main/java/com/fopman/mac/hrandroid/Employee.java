package com.fopman.mac.hrandroid;


public class Employee implements IDisplayable {
    private String name;
    private int age;
    private Vehicle v;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public Vehicle getVehicle() {
		return v;
	}

	public void setVehicle(Vehicle v) {
		this.v = v;
	}


	public Employee(String name, int age, Vehicle v) {
		this.name = name;
		this.age = age;
		this.v = v;
	}

	int calcEarnings(){
		return 1000;
	}


	@Override
	public String toString() {
		return "Employee : name=" + name + ", age=" + age + "\n" + (v != null ? v.toString() : "Vehicle [none]");
	}

	@Override
	public void displayData() {
		System.out.println( toString() );
	}

}
