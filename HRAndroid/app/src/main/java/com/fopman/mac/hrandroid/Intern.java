package com.fopman.mac.hrandroid;

public class Intern extends Employee {
	private String schoolName;

	public String getSchoolName() {
		return schoolName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}

	public Intern(String name, int age, Vehicle v, String schoolName) {
		super(name, age, v);
		this.schoolName = schoolName;
	}
	

	@Override
	public String toString() {
		return super.toString() + "\nIntern [School=" + schoolName + "]";
	}

	@Override
	public void displayData() {
		System.out.println( toString() );
	}



}
