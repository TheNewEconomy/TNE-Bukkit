package com.github.tnerevival.companies;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author creatorfromhell, TheMonkey1415
 *
 */
public class Job implements Serializable {
	
	private static final long serialVersionUID = 1L;
	/**
	 * The name of this Job.
	 */
	String name;
	
	/**
	 * The Title the player will have in the company employee list for being apart of this Job.
	 */
	String title;
	
	/**
	 * The paycheck that each player will receive for doing this job.
	 */
	Double paycheck;
	
	/**
	 * A list of permissions that this job has.
	 */
	List<String> permissions = new ArrayList<String>();
	
	/**
	 * A list of the employees that are in this job.
	 */
	List<String> employees = new ArrayList<String>();
	
	public Job(String name) {
		this(name, name, 7.50);
	}
	
	public Job(String name, Double paycheck) {
		this(name, name, paycheck);
	}
	
	public Job(String name, String title, Double paycheck) {
		this.name = name;
		this.title = title;
		this.paycheck = paycheck;
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
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the paycheck
	 */
	public Double getPaycheck() {
		return paycheck;
	}

	/**
	 * @param paycheck the paycheck to set
	 */
	public void setPaycheck(Double paycheck) {
		this.paycheck = paycheck;
	}
}