Plots
=====
TNE's Plot feature will tie in with WorldGuard's Region System.

Each Plot Will have/allow the following:
- Transaction taxes
- Plot Owner(Player/Admin Plot)
- Plot Type(Personal/Bank/Shop)
  - Each plot type will have some configurations:
    - cost, the default cost to purchase a plot of this type
    - enabled, whether or not a plot type is enabled
    - tax enabled, whether or not plot owners may collect taxes from transactions that occur in a particular plot
    - max tax, the maximum tax percentage that may be set in plots


Commands
-------
- /plot - Main TNE Plot Command
- /plot create \<name\> \<type\> \[price\] \[owner\] - Create a new plot.
- /plot delete \<name\> - Deletes an existing plot.
- /plot set \<owner/tax/open/type\> \<value\>
  - Values:
  - Owner \<player\> - Set's the plot's owner.
  - Tax \<true/false\> \[amount\] - Set's the tax of a plot.
  - Open \<true/false\> - Set's whether or not people can enter the plot.
  - Type \<type\> - Set's the plot type.
  - Admin - Toggles admin mode for plots.
- /plot buy \<name\> - Buy a plot for the asking price from the market.
- /plot offer \<name\> \<amount\> - Offer a price that is lower than the market price for a plot.
- /plot accept \<player\> - Accept a player's offer to buy your plot.
- /plot deny \<player\> \[counter\] - Deny a player's offer to buy your plot, with optional counter offer.
- /plot list \<price\> - List a plot as being for sale on the market.
- /plot unlist \<name\> - Remove a plot from the market. 
- /plot deny \<player\> - Denies a player from entering a plot.
- /plot allow \<player\> - Allows a player to enter a plot.
- /plot trust \<player\> - Gives a player permissions for a plot.
- /plot view \<denied/trusted\> - Allows you to view certain plot information.
- /plot info \[name\] - Gives information about a plot.

Configurations
--------

```yaml
#All configurations relating to the plots feature.
Plots:
   #Whether or not plots are enabled
   Enabled: true
   
   #All configurations relating to personal plots.
   Personal:
      
      #Whether or not players may purchase this plot type.
      Purchase: true
      
      #The maximum amount a player may own of this plot type.
      MaxOwnership: 5
      
      #The default price, unless otherwise set.
      Price: 1000
      
      #Whether or not plot owners are allowed to collect a tax for certain transactions in their plot.
      CanTax: true
      
      #The maximum amount allowed to be collected by plot owners as taxes.
      MaximumTax: 7.0
   
   #All configurations relating to personal plots.
   Bank:
      
      #Whether or not players may purchase this plot type.
      Purchase: true
      
      #The maximum amount a player may own of this plot type.
      MaxOwnership: 5
      
      #The default price, unless otherwise set.
      Price: 5000
      
      #Whether or not plot owners are allowed to collect a tax for certain transactions in their plot.
      CanTax: true
      
      #The maximum amount allowed to be collected by plot owners as taxes.
      MaximumTax: 11.5
   
   #All configurations relating to personal plots.
   Shop:
      
      #Whether or not players may purchase this plot type.
      Purchase: true
      
      #The maximum amount a player may own of this plot type.
      MaxOwnership: 5
      
      #The default price, unless otherwise set.
      Price: 10000
      
      #Whether or not plot owners are allowed to collect a tax for certain transactions in their plot.
      CanTax: true
      
      #The maximum amount allowed to be collected by plot owners as taxes.
      MaximumTax: 17

```
