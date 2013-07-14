package com.github.tnerevival.core.companies;

/**
 * 
 * @author creatorfromhell, TheMonkey1415
 *
 */
public abstract class Job {
	String name;
	String title;
	Double paycheck;
	
	public Job(String name) {
		this(name, name, 7.50);
	}
	
	public Job(String name, String title) {
		this(name, title, 7.50);
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