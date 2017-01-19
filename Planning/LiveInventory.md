Live Inventory
==============
The purpose of this document is to outline the strategy for developing
TNE's new live cross-server capable inventory editing system.

Features
--------
The ability to update inventories across multiple servers without limiting
viewing to a sole player. This will mostly be developed for player trading.

Settings
--------
- Max-view limit
- cross-server inventories(depending on what data format you have chosen)
- item blacklist/whitelist
- slot blacklist/whitelist
- ability to ban players from using inventories(mostly internal-usage only)

Outline
---------------
When a player opens any inventory they are added to the inventory's viewer
list. This will now track all activities they make to the inventory's items.
From this point on when a player modifies an inventory the inventory type,
owner's UUID, world and operation will be sent to a mysql table,
which will then update any viewers' inventory view.