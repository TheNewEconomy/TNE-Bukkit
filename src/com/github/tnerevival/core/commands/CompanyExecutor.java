package com.github.tnerevival.core.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.github.tnerevival.TheNewEconomy;
import com.github.tnerevival.core.accounts.Account;
import com.github.tnerevival.core.companies.Company;
import com.github.tnerevival.core.companies.Job;

public class CompanyExecutor implements CommandExecutor {
	 
	private TheNewEconomy plugin;

	public CompanyExecutor(TheNewEconomy plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
	           Player player = (Player) sender;
	           Account acc = TheNewEconomy.instance.eco.accounts.get(player.getName());
	           if(cmd.getName().equalsIgnoreCase("company")) {
	        	   Company company = acc.getCompany();
	        	   if(args.length > 0) {
	        		   if(args[0].equalsIgnoreCase("view")) {
	        			   if(args.length == 1) {
	        				   if(company != null) {
		        				   for(String s : company.details()) {
		        					   player.sendMessage(s);
		        				   }
	        				   } else {
	        					   player.sendMessage("You must be apart of a company in order to do that.");
	        				   }
	        			   } else {
	        				   sendHelp(player);
	        			   }
	        		   } else if(args[0].equalsIgnoreCase("create")) {
	        			   if(args.length == 2) {
	        				   if(TheNewEconomy.instance.eco.companies.containsKey(args[1])) {
	        					   player.sendMessage("I'm sorry, but that name is already in use.");
	        				   } else {
	        					   Company newCompany = new Company(args[1], player.getName());
	        					   acc.setCompany(newCompany);
	        					   TheNewEconomy.instance.eco.companies.put(args[1], newCompany);
	        					   player.sendMessage("Company has been created successfully!");
	        				   }
	        			   } else {
	        				   player.sendMessage("Correct usage is /company create [name].");
	        			   }
	        		   } else if(args[0].equalsIgnoreCase("delete")) {
	        			   if(args.length == 1) {
	        				   if(company != null) {
	        					   if(company.getOwner().equals(player.getName())) {
	        						   company.delete();
	        					   } else {
	        						   player.sendMessage("You must be the CEO of your company to do that.");
	        					   }
	        				   } else {
	        					   player.sendMessage("You must be apart of a company in order to do that.");
	        				   }
	        			   } else {
	        				   sendHelp(player);
	        			   }
	        		   } else if(args[0].equalsIgnoreCase("rename")) {
	        			   if(args.length == 2) {
	        				   if(company.getOwner().equals(player.getName())) {
		        				   if(TheNewEconomy.instance.eco.companies.containsKey(args[1])) {
		        					   player.sendMessage("I'm sorry, but that name is already in use.");
		        				   } else {
		        					   TheNewEconomy.instance.eco.companies.remove(company.getName());
		        					   company.setName(args[1]);
		        					   TheNewEconomy.instance.eco.companies.put(args[1], company);
		        					   player.sendMessage("You have successfully renamed your company to " + args[1] + ".");
		        				   }
		        			   } else {
		        				   player.sendMessage("You must be the CEO of your company to do that.");
		        			   }
	        			   } else {
	        				   player.sendMessage("Correct usage is /company rename [new name].");
	        			   }
	        		   } else if(args[0].equalsIgnoreCase("hire")) {
	        			   
	        		   } else if(args[0].equalsIgnoreCase("fire")) {
	        			   
	        		   } else if(args[0].equalsIgnoreCase("jobs")) {
	        			   if(args.length > 1) {
	        				   if(args[1].equalsIgnoreCase("create")) {
	        					   if(company.getOwner().equals(player.getName())) {
		        					   if(args.length == 3) {
		        						   if(company.jobExist(args[2])) {
		        							   player.sendMessage("I'm sorry, but that job already exists!");
		        						   } else {
		        							   Job job = new Job(args[2]);
		        							   company.getJobs().add(job);
		        							   player.sendMessage("Successfully create the job!");
		        						   }
		        					   } else if(args.length == 4) {
		        						   if(company.jobExist(args[2])) {
		        							   player.sendMessage("I'm sorry, but that job already exists!");
		        						   } else {
		        							   Job job = new Job(args[2], Double.valueOf(args[3]));
		        							   company.getJobs().add(job);
		        							   player.sendMessage("Successfully create the job!");
		        						   }
		        					   } else {
		        						   sendHelp(player);
		        					   }
	        					   } else {
	        						   player.sendMessage("You must be the CEO of your company to do that.");
	        					   }
	        				   } else if(args[1].equalsIgnoreCase("remove")) {
	        					   if(company.getOwner().equals(player.getName())) {
		        					   if(args.length == 3) {
		        						   if(company.jobExist(args[2])) {
		        							   company.removeJob(args[2], "Job has been removed.");
		        							   player.sendMessage("Job removed, and all employees with that job have been fired.");
		        						   } else {
		        							   player.sendMessage("I'm sorry, but that job does not exist.");
		        						   }
		        					   } else if(args.length == 4) {
		        						   if(company.jobExist(args[2])) {
		        							   company.removeJob(args[2], args[3]);
		        							   player.sendMessage("Job removed, and all employees with that job have been fired.");
		        						   } else {
		        							   player.sendMessage("I'm sorry, but that job does not exist.");
		        						   }
		        					   } else {
		        						   sendHelp(player);
		        					   }
	        					   } else {
	        						   player.sendMessage("You must be the CEO of your company to do that.");
	        					   }
	        				   } else {
	        					   sendHelp(player);
	        				   }
	        			   } else {
	        				   player.sendMessage("Your company currently has the following jobs.");
	        				   String jobs = "Jobs: ";
	        				   Integer jobCount = 0;
	        				   for(Job j : company.getJobs()) {
	        					   jobCount++;
	        					   jobs += j.getName();
	        					   if(jobCount < company.getJobs().size()) {
	        						   jobs += ", ";
	        					   }
	        				   }
	        				   player.sendMessage(jobs);
	        			   }
	        		   } else {
	        			   sendHelp(player);
	        		   }
	        	   } else {
	        		   sendHelp(player);
	        	   }
	           }
		}
		return false;
	}
	
	void sendHelp(Player player) {
		player.sendMessage(ChatColor.GOLD + "~~~~Company Help~~~~");
		player.sendMessage(ChatColor.GOLD + "/company view - View details about your current company.");
		player.sendMessage(ChatColor.GOLD + "/company create [name] - Create a new company.");
		player.sendMessage(ChatColor.GOLD + "/company delete - Delete your company.");
		player.sendMessage(ChatColor.GOLD + "/company rename [name] - Rename your company.");
		player.sendMessage(ChatColor.GOLD + "/company hire [player] [job] - Hire an employee.");
		player.sendMessage(ChatColor.GOLD + "/company fire [player] <reason> - Fire an employee.");
		player.sendMessage(ChatColor.GOLD + "/company jobs - View your company's jobs.");
		player.sendMessage(ChatColor.GOLD + "/company jobs create [name] <pay> - Create a new job.");
		player.sendMessage(ChatColor.GOLD + "/company jobs remove [name] <reason> - Remove a job from your company.");
	}
}