package com.github.tnerevival.companies;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.github.tnerevival.TNE;
import com.github.tnerevival.account.Account;

/**
 * Holds all information pertaining to a company.
 * @author creatorfromhell
 *
 */
public class Company implements Serializable {
	
	private static final long serialVersionUID = 1L;

	/**
	 * A List of all the Jobs for this company.
	 */
	List<Job> jobs = new ArrayList<Job>();

	/**
	 * A map of this company's partners.
	 * Format: Partner's Name, Partnership Status(Accepted, pending)
	 * Why a HashMap? Instead of making two lists for pending partners, 
	 * and accepted partners this appeared to be the logical way.
	 */
	HashMap<String, String> partners = new HashMap<String, String>();
	
	/**
	 * A list of this company's competitors.
	 */
	List<String> competitors = new ArrayList<String>();
	
	/**
	 * A hashmap containing all applications for this company.
	 * Format: Player, Job. Default Job is set to any.
	 */
	HashMap<String, String> applications = new HashMap<String, String>();
	
	/**
	 * A HashMap containing all employees for this company.
	 * Format: Employee Name, Job Name.
	 */
	HashMap<String, String> employees = new HashMap<String, String>();
	
	String name;
	String owner;
	Boolean hiring;
	Double tax;
	Double revenue;
	Double fees;
	Double profit;
	
	public Company(String name, String owner) {
		this.name = name;
		this.owner = owner;
		this.hiring = true;
		this.tax = 0.0;
		this.revenue = 0.0;
		this.fees = 0.0;
		this.profit = 0.0;
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
	 * @return the partners
	 */
	public HashMap<String, String> getPartners() {
		return partners;
	}

	/**
	 * @param partners the partners to set
	 */
	public void setPartners(HashMap<String, String> partners) {
		this.partners = partners;
	}

	/**
	 * @return the competitors
	 */
	public List<String> getCompetitors() {
		return competitors;
	}

	/**
	 * @param competitors the competitors to set
	 */
	public void setCompetitors(List<String> competitors) {
		this.competitors = competitors;
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
	 * @return the hiring
	 */
	public Boolean getHiring() {
		return hiring;
	}

	/**
	 * @param hiring the hiring to set
	 */
	public void setHiring(Boolean hiring) {
		this.hiring = hiring;
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
	
	/**
	 * Used to delete this company.
	 */
	public void delete() {
		for(String s : employees.keySet()) {
			Account acc = TNE.instance.manager.accounts.get(s);
			Player player = Bukkit.getPlayer(UUID.fromString(s));
			if(player != null) {
				player.sendMessage(ChatColor.RED + "You've been kicked from your company due to the owner deleting it.");
			}
			acc.setCompany(null);
		}
		Account acc = TNE.instance.manager.accounts.get(owner);
		Player player = Bukkit.getPlayer(UUID.fromString(owner));
		player.sendMessage("Your company has been deleted!");
		acc.setCompany(null);
		TNE.instance.manager.companies.remove(name);
	}
	
	public Boolean jobExists(String name) {
		for(Job job : jobs) {
			if(job.getName().equals(name)) {
				return true;
			}
		}
		return false;
	}
	
	public void removeJob(String jobName, String reason) {
		for(Job job : jobs) {
			if(job.getName().equals(jobName)) {
				for(String s : job.employees) {
					Player player = Bukkit.getPlayer(UUID.fromString(s));
					Account acc = TNE.instance.manager.accounts.get(s);
					acc.setCompany(null);
					player.sendMessage("You have lost your job due to.");
					player.sendMessage("Reason: " + reason); 
					job.employees.remove(s);
					employees.remove(s);
				}
				jobs.remove(job);
			}
		}
	}
	
	public List<String> details() {
		List<String> detailsList = new ArrayList<String>();
		detailsList.add(ChatColor.GOLD + "~~~~[" + name + "]~~~~");
		detailsList.add("Profit: " + getProfit() + "  Employees: " + (employees.size()));
		detailsList.add("CEO: " + owner);
		for(Job j : jobs) {
			Integer jEmployees = 1;
			String jString = j.getName() + ": ";
			for(String s : j.employees) {
				jString += s;
				if(jEmployees < j.employees.size()) {
					jString += ", ";
				}
				jEmployees++;
			}
			detailsList.add(jString);
		}
		
		return detailsList;
	}
}