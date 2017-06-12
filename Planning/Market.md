Market
========
The market feature will give server owner's a simple, and easy-to-use global market place where player's may post items for sale.


Commands
--------------
- /market list <price> [market(defaults to global)] [period(defaults to 1 day)]
- /market create <name> [list:list node] [buy:buy node] [view:view node]
- /market [name]



Configurations
--------------
```yaml
Markets:
    global:
       #The maximum amount of time an item may be listed in this market
       MaxList: 1 day
       #The amount of money it costs to list an item in this market
       ListCost: 10
       #The permission node required to list an item here.
       List: tne.market.list
       #The permission node required to buy an item from here.
       Buy: tne.market.list
       #The permission node required to view this market.
       View: tne.market.list
       #If group offers is on, all offers by the same player will be grouped together, 
       #this may require more market pages
       GroupOffers: false
       #Want to blacklist items from your market? Do so here.
       Forbidden:
       #Want to only accept a couple items, and don't want to write a book in the forbidden list? Do so here
       Accepted:
```