package com.github.tnerevival.core.signs;

import com.github.tnerevival.TNE;

public enum SignType {
	
	UNKNOWN("unknown", "", "", ""),
	BANK("bank", "tne.place.bank", "tne.use.bank", "Core.Signs.Bank"),
	BUY("buy", "tne.place.buy", "tne.use.buy", "Core.Signs.Buy"),
	SELL("sell", "tne.place.sell", "tne.use.sell", "Core.Signs.Sell"),
	SHOP("shop", "tne.place.shop", "tne.use.shop", "Core.Signs.Shop");
	
	private String name;
	private String placePermission;
	private String usePermission;
	private String configuration;

	SignType(String name, String placePermission, String usePermission, String configuration) {
		this.name = name;
		this.placePermission = placePermission;
		this.usePermission = usePermission;
		this.configuration = configuration;
	}
	
	public static SignType fromName(String name) {
		for(SignType type : values()) {
			if(type.getName().equalsIgnoreCase(name)) {
				return type;
			}
		}
		return UNKNOWN;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPlacePermission() {
		return placePermission;
	}

	public void setPlacePermission(String placePermission) {
		this.placePermission = placePermission;
	}

	public String getUsePermission() {
		return usePermission;
	}

	public void setUsePermission(String usePermission) {
		this.usePermission = usePermission;
	}

	public Boolean enabled(String world, String player) {
		return TNE.instance.api.getBoolean(configuration + ".Enabled", world, player);
	}

	public Double place(String world, String player) {
		return TNE.instance.api.getDouble(configuration + ".Place", world, player);
	}

	public Double use(String world, String player) {
		return TNE.instance.api.getDouble(configuration + ".Use", world, player);
	}
}