Live Inventory
==============
The purpose of this document is to outline the strategy for developing
TNE's new live cross-server capable inventory editing system.

Features
--------
The ability to update inventories across multiple servers without limiting
viewing to a sole player.

Outline
---------------
When a player opens any inventory they are added to the inventory's viewer
list. This will now track all activities they make to the inventory's items.
From this point on when a player modifies an inventory the inventory type,
owner's UUID, world and operation will be sent to a mysql table for other,
which will then update any viewers' inventory view.