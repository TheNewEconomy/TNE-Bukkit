Alpha 3.0 Testing
=================

Improvements Needed
-------------------
Inventory systems(cost, blacklisted items, view controlling, and access lists should be merged into a new single system.)

Bugs Found
----------
- Version checker needs improved to take full version string into consideration
- Potion cost error: NPE - InteractionListener:481
- Command help takes up too much screen space
- /tne create not found
- /tne purge doesn't reflect on reload
- /tne delete shows no message after deleting an account
- players can still be charged for chest access without confirming their pin
- players still being charged for commands without confirming pin
- text shown for when players cannot afford the command cost
- enchanted books, potions, and new arrow types should be blacklisted
from banks as we cannot properly save them without building for specific
minecraft versions
- 