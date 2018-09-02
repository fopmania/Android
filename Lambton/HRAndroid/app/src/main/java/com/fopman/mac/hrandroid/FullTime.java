package com.fopman.mac.hrandroid;

public class FullTime extends Employee {
	
	private int	salary;
	private int bonus;



	public int getSalary() {
		return salary;
	}

	public void setSalary(int salary) {
		this.salary = salary;
	}

	public int getBonus() {
		return bonus;
	}

	public void setBonus(int bonus) {
		this.bonus = bonus;
	}

	public FullTime(String name, int age, Vehicle v, int salary, int bonus) {
		super(name, age, v);
		this.salary = salary;
		this.bonus = bonus;
	}

	int calcEarnings(){
		return salary + bonus;
	}

	@Override
	public String toString() {
		return super.toString() + "\nFullTime [salary=" + salary + ", bonus=" + bonus + "]";
	}

	@Override
	public void displayData() {
		System.out.println( toString() );
	}


}
