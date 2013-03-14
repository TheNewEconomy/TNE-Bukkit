package com.github.tnerevival.properties;

import com.github.tnerevival.TheNewEconomy;



public class TNEMainProperties {
	
	String mainPropertiesFile = TheNewEconomy.pluginDirectory + "/economy.properties";
	String mainPropertiesTitle = "~~~The New Economy Main Properties~~~";
	TNEProperties mainProperties = new TNEProperties(mainPropertiesFile, mainPropertiesTitle);
	
	/**
	 * The singular form of the currency name for the server.
	 */
	static String currencySingular;
	
	/**
	 * The plural form of the currency name for the server.
	 */
	static String currencyPlural;
	
	/**
	 * Whether or not mobs should give/take money upon death.
	 */
	static Boolean mobMoneyDrops;
	
	/**
	 * The amount of money a player receives when they join the server.
	 */
	static double startingMoney;
	
	/**
	 * The amount of money in the public fund.
	 */
	static double startingPublicFund;

	/**
	 * The maximum side length for a trading area.
	 */
	static int maxTradeAreaLength;

	/**
	 * The maximum size for a trading area (in square blocks).
	 */
	static int maxTradeAreaSize;

	/**
	 * The maximum side length for a banking area.
	 */
	static int maxBankAreaLength;

	/**
	 * The maximum size for a banking area (in square blocks).
	 */
	static int maxBankAreaSize;

	/**
	 * Cost per block for a trading area.
	 */
	static double tradeAreaCost;

	/**
	 * Cost per block for a personal area.
	 */
	static double personalAreaCost;

	/**
	 * Cost per block for a banking area.
	 */
	static double bankAreaCost;

	/**
	 * Percent of area cost that must be paid for transfer.
	 */
	static double transferFee;

	/**
	 * Percent of area cost that must be paid to rename an area (1st rename is
	 * free).
	 */
	static double renameFee;

	/**
	 * The maximum number of offers any player can have active at once.
	 */
	static int maxOffers;
	
	/**
	 * The percentage of area cost that vertical space costs.
	 */
    static double verticalCost;

	/**
	 * The time between lotteries in minutes.
	 */
	static double lotteryInterval;

	/**
	 * The number of players online for the lottery winnings to be half of the
	 * public fund.
	 */
	static int playersUntilHalf;

	/**
	 * The maximum number of areas a player can own.
	 */
	static int maxAreas;

	/**
	 * The sales tax for buying items.
	 */
	static double salesTax;

	/**
	 * The minimum length for auctions in seconds.
	 */
	static double minAuctionLength;

	/**
	 * The maximum length for auctions in seconds.
	 */
	static double maxAuctionLength;
	
	/**
	 * The amount of money in the public fund.
	 */
	static double publicFund;

	/**
	 * The time of the last lottery.
	 */
	static long lastLottery;
	
	public TNEMainProperties() {
		work(mainProperties);
		
	}

	void work(TNEProperties prop) {
		
		try {
			values(prop);
			prop.initiate();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}

	void values(TNEProperties prop) {
		
		currencySingular = prop.getString("Currency-Singular", "Dollar");
		currencyPlural = prop.getString("Currency-Plural", "Dollars");
		mobMoneyDrops = prop.getBoolean("Mob-Money-Drops", false);
		startingMoney = prop.getDouble("Start-Money", 10.0);
		publicFund = prop.getDouble("Starting-Public-Fund", 1500.0);
		maxTradeAreaLength = prop.getInteger("Max-Trade-Area-Length", 15);
		maxTradeAreaSize = prop.getInteger("Max-Trade-Area-Size", 144);
		maxBankAreaLength = prop.getInteger("Max-Bank-Area-Length", 15);
		maxBankAreaSize = prop.getInteger("Max-Bank-Area-Size", 400);
		tradeAreaCost = prop.getDouble("Trade-Area-Cost", 1.0);
		personalAreaCost = prop.getDouble("Personal-Area-Cost", 3.0);
		bankAreaCost = prop.getDouble("Bank-Area-Cost", 5.0);
		transferFee = prop.getDouble("Transfer-Fee", 0.15);
		renameFee = prop.getDouble("Rename-Fee", 0.15);
		maxOffers = prop.getInteger("Max-Offers", 10);
		verticalCost = prop.getDouble("Vertical-Cost", 0.01);
		lotteryInterval = prop.getDouble("Lottery-Interval", 60.0);
		playersUntilHalf = prop.getInteger("Players-Until-Half", 5);
		maxAreas = prop.getInteger("Max-Areas", 20);
		salesTax = prop.getDouble("Sales-Tax", 0.01);
		minAuctionLength = prop.getDouble("Min-Auction-Length", 15.0);
		maxAuctionLength = prop.getDouble("Max-Auction-Length", 60.0);
		
	}
	
}