API
====================
   Used to get a player's UUID from their username or string version of UUID.       
 `  @param identifier The player's username of stringified version of their UUID.`       
 `  @return The UUID for the identifier, or null if it doesn't exist.`       
`public UUID getID(String identifier);`

   Used to determine if a player has an economy account.       
 `  @param identifier The player's username of stringified version of their UUID.`       
 `  @return True if the account exists, otherwise false.`       
`public Boolean accountExists(String identifier);`

   Create an economy account for the player using the specific identifier.`       
 `  @param identifier The player's username of stringified version of their UUID.`       
`public void createAccount(String identifier);`

   Used to get a player's economy account.       
 `  @param identifier The player's username of stringified version of their UUID.`       
 `  @return The player's account if it exists, otherwise null.`       
`public Account getAccount(String identifier);`

   Add funds to a player's economy account.       
 `  @param identifier The player's username of stringified version of their UUID.`       
 `  @param amount The amount of funds to add to the player's account.`       
`public void fundsAdd(String identifier, Double amount);`

   Add funds to a player's economy account.       
 `  @param identifier The player's username of stringified version of their UUID.`       
 `  @param world The world balance to perform this action on.`       
 `  @param amount The amount of funds to add to the player's account.`       
`public void fundsAdd(String identifier, String world, Double amount);`

   Add funds to a player's economy account.       
 `  @param identifier The player's username of stringified version of their UUID.`       
 `  @param world The world balance to perform this action on.`       
 `  @param amount The amount of funds to add to the player's account.`       
 `  @param currency The currency of the funds.`       
`public void fundsAdd(String identifier, String world, Double amount, Currency currency);`

   Determines if the specified player has the specified amount of funds.       
 `  @param identifier The player's username of stringified version of their UUID.`       
 `  @param amount The amount of funds to check for.`       
 `  @return Whether or not this player has the specified funds.`       
`public Boolean fundsHas(String identifier, Double amount);`

   Determines if the specified player has the specified amount of funds.       
 `  @param identifier The player's username of stringified version of their UUID.`       
 `  @param world The world balance to perform this action on.`       
 `  @param amount The amount of funds to check for.`       
 `  @return Whether or not this player has the specified funds.`       
`public Boolean fundsHas(String identifier, String world, Double amount);`

   Determines if the specified player has the specified amount of funds.       
 `  @param identifier The player's username of stringified version of their UUID.`       
 `  @param world The world balance to perform this action on.`       
 `  @param amount The amount of funds to check for.`       
 `  @param currency The currency of the funds.`       
 `  @return Whether or not this player has the specified funds.`       
`public Boolean fundsHas(String identifier, String world, Double amount, Currency currency);`

   Remove funds from a player's economy account.       
 `  @param identifier The player's username of stringified version of their UUID.`       
 `  @param amount The amount of funds to remove to the player's account.`       
`public void fundsRemove(String identifier, Double amount);`

   Remove funds from a player's economy account.       
 `  @param identifier The player's username of stringified version of their UUID.`       
 `  @param world The world balance to perform this action on.`       
 `  @param amount The amount of funds to remove to the player's account.`       
`public void fundsRemove(String identifier, String world, Double amount);`

   Remove funds from a player's economy account.       
 `  @param identifier The player's username of stringified version of their UUID.`       
 `  @param world The world balance to perform this action on.`       
 `  @param amount The amount of funds to remove to the player's account.`       
 `  @param currency The currency of the funds.`       
`public void fundsRemove(String identifier, String world, Double amount, Currency currency);`

   Get the specified player's balance.       
 `  @param identifier The player's username of stringified version of their UUID.`       
 `  @return The balance for the specified player.`       
`public Double getBalance(String identifier);`

   Get the specified player's balance.       
 `  @param identifier The player's username of stringified version of their UUID.`       
 `  @param world The world balance to perform this action on.`       
 `  @return The balance for the specified player.`       
`public Double getBalance(String identifier, String world);`

   Get the specified player's balance.       
 `  @param identifier The player's username of stringified version of their UUID.`       
 `  @param world The world balance to perform this action on.`       
 `  @param currency The currency of the funds.`       
 `  @return The balance for the specified player.`       
`public Double getBalance(String identifier, String world, Currency currency);`

   Set the specified player's balance.       
 `  @param identifier The player's username of stringified version of their UUID.`       
 `  @param amount The new balance amount for this player.`       
`public void setBalance(String identifier, Double amount);`

   Set the specified player's balance.       
 `  @param identifier The player's username of stringified version of their UUID.`       
 `  @param amount The new balance amount for this player.`       
 `  @param world The world balance to perform this action on.`       
`public void setBalance(String identifier, Double amount, String world);`

   Set the specified player's balance.       
 `  @param identifier The player's username of stringified version of their UUID.`       
 `  @param amount The new balance amount for this player.`       
 `  @param world The world balance to perform this action on.`       
 `  @param currency The currency of the funds.`       
`public void setBalance(String identifier, Double amount, String world, Currency currency);`

   Create a bank for the specified owner in a specific world.       
 `  @param owner The identifier of the bank owner.`       
 `  @param world The name of the world to use.`       
`public void createBank(String owner, String world);`

   Determiner whether or not the specified owner has a bank.       
 `  @param owner The identifier of the bank owner.`       
 `  @return True if the owner has a bank, otherwise false.`       
`public Boolean hasBank(String owner);`

   Determine whether or not the specified owner has a bank in a world.       
 `  @param owner The identifier of the bank owner.`       
 `  @param world The name of the world to use.`       
 `  @return True if the owner has a bank, otherwise false.`       
`public Boolean hasBank(String owner, String world);`

   Add a member to the specific owner's bank.       
 `  @param owner The identifier of the bank owner.`       
 `  @param identifier The identifier of the player to add as a member.`       
 `  @param world The name of the world to use.`       
`public void addMember(String owner, String identifier, String world);`

   Remove a member from the specific owner's bank.       
 `  @param owner The identifier of the bank owner.`       
 `  @param identifier The identifier of the player to remove from the bank.`       
 `  @param world The name of the world to use.`       
`public void removeMember(String owner, String identifier, String world);`

   Determine whether or not a player is a member of a bank.       
 `  @param owner The identifier of the bank owner.`       
 `  @param identifier The identifier of the player.`       
 `  @param world The name of the world to use.`       
 `  @return True if the player is a member, otherwise false.`       
`public Boolean bankMember(String owner, String identifier, String world);`

   Get the balance of a bank.       
 `  @param owner The identifier of the bank owner.`       
 `  @return The balance of the bank.`       
`public Double getBankBalance(String owner);`

   Get the balance of a bank.       
 `  @param owner The identifier of the bank owner.`       
 `  @param world The name of the world to use.`       
 `  @return The balance of the bank.`       
`public Double getBankBalance(String owner, String world);`

   Set the balance of a bank to a new amount.       
 `  @param owner The identifier of the bank owner.`       
 `  @param amount The new amount for the bank balance.`       
`public void setBankBalance(String owner, Double amount);`

   Set the balance of a bank to a new amount.       
 `  @param owner The identifier of the bank owner.`       
 `  @param world The name of the world to use.`       
 `  @param amount The new amount for the bank balance.`       
`public void setBankBalance(String owner, String world, Double amount);`

   Determine whether or not a bank slot is occupied.       
 `  @param owner The identifier of the bank owner.`       
 `  @param slot The slot to check.`       
 `  @return True if the slot is occupied, otherwise false.`       
`public Boolean bankHasItem(String owner, Integer slot);`

   Determine whether or not a bank slot is occupied.       
 `  @param owner The identifier of the bank owner.`       
 `  @param world The name of the world to use.`       
 `  @param slot The slot to check.`       
 `  @return True if the slot is occupied, otherwise false.`       
`public Boolean bankHasItem(String owner, String world, Integer slot);`

   Get the itemstack in a slot of a bank.       
 `  @param owner The identifier of the bank owner.`       
 `  @param slot The slot to use.`       
 `  @return An ItemStack instance if the slot is occupied, otherwise null.`       
`public ItemStack getBankItem(String owner, Integer slot);`

   Get the itemstack in a slot of a bank.       
 `  @param owner The identifier of the bank owner.`       
 `  @param world The name of the world to use.`       
 `  @param slot The slot to use.`       
 `  @return An ItemStack instance if the slot is occupied, otherwise null.`       
`public ItemStack getBankItem(String owner, String world, Integer slot);`

   Get a list of items in a bank.       
 `  @param owner The identifier of the bank owner.`       
 `  @return A list of ItemStack instances.`       
`public List<ItemStack> getBankItems(String owner);`

   Get a list of items in a bank.       
 `  @param owner The identifier of the bank owner.`       
 `  @param world The name of the world to use.`       
 `  @return A list of ItemStack instances.`       
`public List<ItemStack> getBankItems(String owner, String world);`

   Add an item to a bank.       
 `  @param owner The identifier of the bank owner.`       
 `  @param slot The slot to use.`       
 `  @param stack The item to add.`       
`public void addBankItem(String owner, Integer slot, ItemStack stack);`

   Add an item to a bank.       
 `  @param owner The identifier of the bank owner.`       
 `  @param world The name of the world to use.`       
 `  @param slot The slot to use.`       
 `  @param stack The item to add.`       
`public void addBankItem(String owner, String world, Integer slot, ItemStack stack);`

   Set a bank's items.       
 `  @param owner The identifier of the bank owner.`       
 `  @param items The new list of items.`       
`public void setBankItems(String owner, Collection<ItemStack> items);`

   Set a bank's items.       
 `  @param owner The identifier of the bank owner.`       
 `  @param world The name of the world to use.`       
 `  @param items The new list of items.`       
`public void setBankItems(String owner, String world, Collection<ItemStack> items);`

   Get the inventory instance of a player's bank.       
 `  @param owner The identifier of the bank's owner.`       
 `  @return The inventory instance of the bank.`       
`public Inventory getBankInventory(String owner);`

   Get the inventory instance of a player's bank.       
 `  @param owner The identifier of the bank's owner.`       
 `  @param world The name of the world to use.`       
 `  @return The inventory instance of the bank.`       
`public Inventory getBankInventory(String owner, String world);`
   
   Format the specified amount based on this server's configurations.       
 `  @param amount The amount to format.`       
 `  @return The formatted balance.`       
`public String format(Double amount);`

   Format the specified amount based on this server's configurations.       
 `  @param world The name of the world, for world-specific configurations.`       
 `  @param amount The amount to format.`       
 `  @return The formatted balance.`       
`public String format(String world, Double amount);`

   Format the specified amount based on this server's configurations.       
 `  @param name The name of the currency for formatting purposes.`       
 `  @param world The name of the world, for world-specific configurations.`       
 `  @param amount The amount to format.`       
 `  @return The formatted balance.`       
`public String format(String name, String world, Double amount);`

   Retrieve the default currency's shortened format.       
 `  @return The shortened format of the default currency.`       
`public Boolean getShorten();`

   Retrieve the world default currency's shortened format.       
 `  @param world The name of the world to use.`       
 `  @return The shortened format of the default currency.`       
`public Boolean getShorten(String world);`

   Retrieve the currency's shortened format.       
 `  @param world The name of the world to use.`       
 `  @param currencyName The name of the currency to use.`       
 `  @return The shortened format of the default currency if the specified currency exists,
   otherwise null.`       
`public Boolean getShorten(String world, String currencyName);`

   Get the name of the default currency. This will return the major/minor singular/plural name based on the parameters given.       
 `  @param major Whether or not to retrieve the currency's major name.`       
 `  @param singular Whether or not to retrieve the plural name.`       
 `  @return The currency's name based on the given parameters.`       
`public String getCurrencyName(Boolean major, Boolean singular);`

   Get the name of the default currency for the specified world. This will return the major/minor singular/plural name based on the parameters given.       
 `  @param major Whether or not to retrieve the currency's major name.`       
 `  @param singular Whether or not to retrieve the plural name.`       
 `  @param world The name of the world to use.`       
 `  @return The currency's name based on the given parameters.`       
`public String getCurrencyName(Boolean major, Boolean singular, String world);`

   Determine whether or not a specific currency exists.       
 `  @param name The name of the currency to use.`       
 `  @return True if the currency exists, otherwise false.`       
`public Boolean currencyExists(String name);`

   Determine whether or not a specific currency exists.       
 `  @param world The name of the world to use.`       
 `  @param name The name of the currency to use.`       
 `  @return True if the currency exists in the specific world, otherwise false.`       
`public Boolean currencyExists(String world, String name);`

   Get the instance of a currency based on its world and name.       
 `  @param world The name of the world to use.`       
 `  @return The instance of the currency if it exists, otherwise null.`       
`public Currency getCurrency(String world);`

   Get the instance of a currency based on its world and name.       
 `  @param world The name of the world to use.`       
 `  @param name The name of the currency.`       
 `  @return The instance of the currency if it exists, otherwise null.`       
`public Currency getCurrency(String world, String name);`

   Get a map of all the currencies.       
 `  @return A map containing every currency with the world as the key, and instance as value.`       
`public Map<String, Currency> getCurrencies();`

   Get a list of all the currencies for a specific world.       
 `  @param world The name of the world to use.`       
 `  @return A list containing every currency for the specified world.`       
`public List<Currency> getCurrencies(String world);`
  
   Determine whether or not there is a shop with the specified name.       
 `  @param name The name of the shop.`       
 `  @return True if the shop exists, otherwise false.`       
`public Boolean shopExists(String name);`

   Determine whether or not there is a shop with the specified name in a specific world.       
 `  @param name The name of the shop.`       
 `  @param world The name of the world to check.`       
 `  @return True if the shop exists, otherwise false.`       
`public Boolean shopExists(String name, String world);`

   Get the instance of a shop based on its name.       
 `  @param name The name of the shop.`       
 `  @return The instance of the shop if it exists, otherwise null.`       
`public Shop getShop(String name);`

   Get the instance of a shop based on its name and world.       
 `  @param name The name of the shop.`       
 `  @param world The name of the world to check.`       
 `  @return The instance of the shop if it exists, otherwise null.`       
`public Shop getShop(String name, String world);`

   Determine whether or not there is a TNESign at the specified location.  
 `  @param location The location to check for a sign.`       
 `  @return True if there is a TNESign, otherwise false.`       
`public Boolean validSign(Location location);`

   Get the instance of a TNESign based on its location.       
 `  @param location The location of the sign.`       
 `  @return The instance of the sign at the location if it exists, otherwise null.`       
`public TNESign getSign(Location location);`

   Create an empty instance of a TNESign with the specified type, and owner.       
 `  @param type The sign type for this sign.`       
 `  @param owner The string identifier of the owner for this sign.`       
 `  @return The sign instance based on the type, and owner.`       
`public TNESign createInstance(SignType type, String owner);`

   Create an empty instance of a TNESign with the specified type, and owner.       
 `  @param type The sign type for this sign.`       
 `  @param owner The UUID of the owner for this sign.`       
 `  @return The sign instance based on the type, and owner.`       
`public TNESign createInstance(SignType type, UUID owner);`

   Remove the TNESign located at the specified location.       
 `  @param location The location of the sign.`       
 `  @return True if the sign was removed, otherwise false.`       
`public Boolean removeSign(Location location);`


   Get the value of a String configuration.       
 `  @param configuration The configuration node.`       
 `  @return The value of the configuration.`       
`public String getString(String configuration);`

   Get the value of a String configuration.       
 `  @param configuration The configuration node.`       
 `  @param world The name of the world to use.`       
 `  @return The value of the configuration.`       
`public String getString(String configuration, String world);`

   Get the value of a String configuration.       
 `  @param configuration The configuration node.`       
 `  @param world The name of the world to use.`       
 `  @param uuid The uuid of the player to use.`       
 `  @return The value of the configuration.`       
`public String getString(String configuration, String world, UUID uuid);`

   Get the value of a String configuration.       
 `  @param configuration The configuration node.`       
 `  @param world The name of the world to use.`       
 `  @param player The identifier of the player to use.`       
 `  @return The value of the configuration.`       
`public String getString(String configuration, String world, String player);`

   Get the value of a Boolean configuration.       
 `  @param configuration The configuration node.`       
 `  @return The value of the configuration.`       
`public Boolean getBoolean(String configuration);`

   Get the value of a Boolean configuration.       
 `  @param configuration The configuration node.`       
 `  @param world The name of the world to use.`       
 `  @return The value of the configuration.`       
`public Boolean getBoolean(String configuration, String world);`

   Get the value of a Boolean configuration.       
 `  @param configuration The configuration node.`       
 `  @param world The name of the world to use.`       
 `  @param uuid The uuid of the player to use.`       
 `  @return The value of the configuration.`       
`public Boolean getBoolean(String configuration, String world, UUID uuid);`

   Get the value of a Boolean configuration.       
 `  @param configuration The configuration node.`       
 `  @param world The name of the world to use.`       
 `  @param player The identifier of the player to use.`       
 `  @return The value of the configuration.`       
`public Boolean getBoolean(String configuration, String world, String player);`

   Get the value of a Double configuration.       
 `  @param configuration The configuration node.`       
 `  @return The value of the configuration.`       
`public Double getDouble(String configuration);`

   Get the value of a Double configuration.       
 `  @param configuration The configuration node.`       
 `  @param world The name of the world to use.`       
 `  @return The value of the configuration.`       
`public Double getDouble(String configuration, String world);`

   Get the value of a Double configuration.       
 `  @param configuration The configuration node.`       
 `  @param world The name of the world to use.`       
 `  @param uuid The uuid of the player to use.`       
 `  @return The value of the configuration.`       
`public Double getDouble(String configuration, String world, UUID uuid);`

   Get the value of a Double configuration.       
 `  @param configuration The configuration node.`       
 `  @param world The name of the world to use.`       
 `  @param player The identifier of the player to use.`       
 `  @return The value of the configuration.`       
`public Double getDouble(String configuration, String world, String player);`

   Get the value of a Integer configuration.      
 `  @param configuration The configuration node.`       
 `  @return The value of the configuration.`       
`public Integer getInteger(String configuration);`

   Get the value of a Integer configuration.       
 `  @param configuration The configuration node.`       
 `  @param world The name of the world to use.`       
 `  @return The value of the configuration.`       
`public Integer getInteger(String configuration, String world);`

   Get the value of a Integer configuration.       
 `  @param configuration The configuration node.`       
 `  @param world The name of the world to use.`       
 `  @param uuid The uuid of the player to use.`       
 `  @return The value of the configuration.`       
`public Integer getInteger(String configuration, String world, UUID uuid);`

   Get the value of a Integer configuration.  
 `  @param configuration The configuration node.`         
 `  @param world The name of the world to use.`       
 `  @param player The identifier of the player to use.`       
 `  @return The value of the configuration.`       
`public Integer getInteger(String configuration, String world, String player);`

   Determine if the specified configuration exists.  
 `  @param configuration The configuration node.`   
 `  @return True if it exists, otherwise false.`          
`public Boolean hasConfiguration(String configuration);`

   Get the value of a configuration.    
 `  @param configuration The configuration node.`       
 `  @return The value of the configuration.`       
`public Object getConfiguration(String configuration);`

   Get the value of a configuration.       
 `  @param configuration The configuration node.`       
 `  @param world The name of the world to use.`       
 `  @return The value of the configuration.`       
`public Object getConfiguration(String configuration, String world);`

   Get the value of a configuration.       
 `  @param configuration The configuration node.`       
 `  @param world The name of the world to use.`       
 `  @param uuid The uuid of the player to use.`       
 `  @return The value of the configuration.`       
`public Object getConfiguration(String configuration, String world, UUID uuid);`

   Get the value of a configuration.       
 `  @param configuration The configuration node.`       
 `  @param world The name of the world to use.`       
 `  @param player The identifier of the player to use.`       
 `  @return The value of the configuration.`       
`public Object getConfiguration(String configuration, String world, String player);`

   Set a configuration value.       
 `  @param configuration The configuration node.`       
 `  @param value The new value for the configuration.`       
`public void setConfiguration(String configuration, Object value);`
