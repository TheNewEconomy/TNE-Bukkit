package com.github.tnerevival.core.signs;

public enum SignType {
	
	UNKNOWN("unknown", "", "", ""),
	AUCTION("auction", "[auction]", "tne.place.auction", "tne.use.auction"),
	BANK("bank", "[bank]", "tne.place.bank", "tne.use.bank"),
	BUY("buy", "[buy]", "tne.place.buy", "tne.use.buy"),
	LOTTERY("lottery", "[lottery]", "tne.place.lottery", "tne.use.lottery"),
	SELL("sell", "[sell]", "tne.place.sell", "tne.use.sell"),
	SHOP("shop", "[shop]", "tne.place.shop", "tne.use.shop");
	
	private String name;
	private String identifier;
	private String placePermission;
	private String usePermission;
	
	SignType(String name, String identifier, String placePermission, String usePermission) {
		this.name = name;
		this.identifier = identifier;
		this.placePermission = placePermission;
		this.usePermission = usePermission;
	}
	
	public static SignType fromLine(String identifier) {
		for(SignType type : values()) {
			if(type.getIdentifier().equalsIgnoreCase(identifier)) {
				return type;
			}
		}
		return UNKNOWN;
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

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
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