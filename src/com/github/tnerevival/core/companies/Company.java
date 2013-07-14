package com.github.tnerevival.core.companies;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 
 * @author creatorfromhell
 *
 */
public class Company {
	List<Job> jobs = new ArrayList<Job>();
	HashMap<String, String> employees = new HashMap<String, String>();
	
	String name;
	String owner;
	Double tax;
	Double revenue;
	Double fees;
	Double profit;
	
	public Company(String name, String owner) {
		this.name = name;
		this.owner = owner;
	}

	/**
	 * @return the jobs
	 */
	public List<Job> getJobs() {
		return jobs;
	}

	/**
	 * @param jobs the jobs to set
	 */
	public void setJobs(List<Job> jobs) {
		this.jobs = jobs;
	}

	/**
	 * @return the employees
	 */
	public HashMap<String, String> getEmployees() {
		return employees;
	}

	/**
	 * @param employees the employees to set
	 */
	public void setEmployees(HashMap<String, String> employees) {
		this.employees = employees;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the owner
	 */
	public String getOwner() {
		return owner;
	}

	/**
	 * @param owner the owner to set
	 */
	public void setOwner(String owner) {
		this.owner = owner;
	}

	/**
	 * @return the tax
	 */
	public Double getTax() {
		return tax;
	}

	/**
	 * @param tax the tax to set
	 */
	public void setTax(Double tax) {
		this.tax = tax;
	}

	/**
	 * @return the revenue
	 */
	public Double getRevenue() {
		return revenue;
	}

	/**
	 * @param revenue the revenue to set
	 */
	public void setRevenue(Double revenue) {
		this.revenue = revenue;
	}

	/**
	 * @return the fees
	 */
	public Double getFees() {
		return fees;
	}

	/**
	 * @param fees the fees to set
	 */
	public void setFees(Double fees) {
		this.fees = fees;
	}

	/**
	 * @return the profit
	 */
	public Double getProfit() {
		return profit;
	}

	/**
	 * @param profit the profit to set
	 */
	public void setProfit(Double profit) {
		this.profit = profit;
	}
}