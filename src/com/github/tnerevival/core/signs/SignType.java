package com.github.tnerevival.core.signs;

public enum SignType {
	
	UNKNOWN("unknown", "", ""),
	BANK("bank", "tne.place.bank", "tne.use.bank"),
	BUY("buy", "tne.place.buy", "tne.use.buy"),
	SELL("sell", "tne.place.sell", "tne.use.sell"),
	SHOP("shop", "tne.place.shop", "tne.use.shop");
	
	private String name;
	private String placePermission;
	private String usePermission;
	
	SignType(String name, String placePermission, String usePermission) {
		this.name = name;
		this.placePermission = placePermission;
		this.usePermission = usePermission;
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
}