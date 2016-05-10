package com.github.tnerevival.core.signs;

public enum SignType {
	
	UNKNOWN("", "", ""),
	AUCTION("[auction]", "tne.place.auction", "tne.use.auction"),
	BANK("[bank]", "tne.place.bank", "tne.use.bank"),
	LOTTERY("[lottery]", "tne.place.lottery", "tne.use.lottery");
	
	private String identifier;
	private String placePermission;
	private String usePermission;
	
	SignType(String identifier, String placePermission, String usePermission) {
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