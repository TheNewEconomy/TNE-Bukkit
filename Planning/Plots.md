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
- /Plot - Main TNE Plots command.
  - Create \<Name> \<Type> \[Price] \[Owner] - Create a new plot.
  - Delete \<Name> - Deletes an existing plot.
  - Set - Set's values for a plot.
    - Owner \<Player> - Set's the plot's owner.
    - Tax \<True/False> \[Amount] - Set's the tax of a plot.
    - Open \<True/False> - Set's whether or not people can enter the plot.
    - Type \<Type> - Set's the plot type.
  - Admin - Toggles admin mode for plots.
  - Deny \<Player> - Denies a player from entering a plot.
  - Allow \<Player> - Allows a player to enter a plot.
  - Trust \<Player> - Gives a player permissions for a plot.
  - List - Displays certain plot info in lists.
    - Denied - Lists denied players.
    - Trusted - Lists trusted players.
  - Info [Name] - Gives information about a plot.

Configurations
--------
- plottype:
  - personal:
    - allow-purchase: true
    - default-price: 1000
    - allow-tax: true
    - maximum-tax: 7.0
    - default-admin-plot: false
  - bank:
    - allow-purchase: true
    - default-price: 5000
    - allow-tax: true
    - maximum-tax: 11.5
    - default-admin-plot: true
  - shop:
    - allow-purchase: true
    - default-price: 10000
    - allow-tax: true
    - maximum-tax: 17
    - default-admin-plot: false
