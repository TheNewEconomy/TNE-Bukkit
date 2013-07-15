package com.github.tnerevival.core.areas;

import com.github.tnerevival.TheNewEconomy;

public enum AreaType {
	B("Bank", TheNewEconomy.instance.config.getDouble("area.bank.price")),
	C("Company", TheNewEconomy.instance.config.getDouble("area.company.price")),
	P("Personal", TheNewEconomy.instance.config.getDouble("area.personal.price")),
	S("Shop", TheNewEconomy.instance.config.getDouble("area.shop.price")),
	T("Trade", TheNewEconomy.instance.config.getDouble("area.trade.price"));
	
	String name;
	Double price;
	
	AreaType(String name, Double price) {
		this.name = name;
		this.price = price;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @return the price
	 */
	public Double getPrice() {
		return price;
	}
}