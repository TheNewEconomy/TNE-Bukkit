About
####
This document is intended to outline various tests ran to determine how to best check to see if a slot has been changed for various click types.
This allows us to accurately determine if a player has placed/removed an item into/from an inventory

Definition
####
- Original Cursor: The itemstack in cursor before the delayed task has been run
- New Cursor: The itemstack in cursor after the delayed task has been run
- Original Slot/ItemStack:  The itemstack in the affected slot before the delayed task has been run
- New Slot/ItemStack:  The itemstack in the affected slot after the delayed task has been run


StartTest: Normal Click Top Inventory Filled Slot, GoldOre:4
==
#### Author: creatorfromhell:
#### Results have proven that you must check the new cursor, and compare it to the new slot
#### Trackable: Yes
[TheNewEconomy] [DEBUG MODE]Original Cursor: ItemStack{AIR x 0}  
[TheNewEconomy] [DEBUG MODE]New Cursor: ItemStack{GOLD_ORE x 4}  
[TheNewEconomy] [DEBUG MODE]Original ItemStack: ItemStack{AIR x 0}  
[TheNewEconomy] [DEBUG MODE]New ItemStack: empty

StartTest: Normal Click Top Inventory Empty Slot, GoldOre:4
==
#### Author: creatorfromhell:
#### Results have proven that merely comparing the original, and new slot will work for this item.
#### Trackable: Yes

[TheNewEconomy] [DEBUG MODE]Original Cursor: ItemStack{GOLD_ORE x 4}  
[TheNewEconomy] [DEBUG MODE]New Cursor: ItemStack{AIR x 0}  
[TheNewEconomy] [DEBUG MODE]Original ItemStack: empty  
[TheNewEconomy] [DEBUG MODE]New ItemStack: ItemStack{GOLD_ORE x 4}  

StartTest: Normal Click Top Inventory Filled Slot -> Filled Slot, GoldOre:4 Diamond:48
==
#### Author: creatorfromhell:
#### Results have proven that merely comparing the original, and new slot will work for this item.
#### Trackable: Yes

//Here we pick up the gold to the cursor  
[TheNewEconomy] [DEBUG MODE]Original Cursor: ItemStack{AIR x 0}  
[TheNewEconomy] [DEBUG MODE]New Cursor: ItemStack{GOLD_ORE x 4}  
[TheNewEconomy] [DEBUG MODE]Original ItemStack: ItemStack{AIR x 0}  
[TheNewEconomy] [DEBUG MODE]New ItemStack: empty  

//We then left click on the slot with the diamonds while the gold is in the cursor.  
[TheNewEconomy] [DEBUG MODE]Original Cursor: ItemStack{GOLD_ORE x 4}  
[TheNewEconomy] [DEBUG MODE]New Cursor: ItemStack{DIAMOND x 48}  
[TheNewEconomy] [DEBUG MODE]Original ItemStack: ItemStack{DIAMOND x 48}  
[TheNewEconomy] [DEBUG MODE]New ItemStack: ItemStack{GOLD_ORE x 4}  

StartTest: Right Click Filled Slot, GoldOre:4
==
#### Author: creatorfromhell:
#### Results have proven that this form of test requires a check to see if current cursor is same type as slot, then new slot may
#### be retrieved by either original, or new slot.
#### Trackable: Yes

[TheNewEconomy] [DEBUG MODE]Original Cursor: ItemStack{AIR x 0}  
[TheNewEconomy] [DEBUG MODE]New Cursor: ItemStack{GOLD_ORE x 2}  
[TheNewEconomy] [DEBUG MODE]Original ItemStack: ItemStack{GOLD_ORE x 2}  
[TheNewEconomy] [DEBUG MODE]New ItemStack: ItemStack{GOLD_ORE x 2}  

StartTest: Right Click Filled Slot with filled cursor same type, GoldOre:2 - GoldOre:2
==
#### Author: creatorfromhell:
#### Results have proven that it is required to check the original cursor, and new for differences and calculate according to slot amount.
#### Added amount = current stack size - (old cursor amount - new cursor amount)
#### Trackable: Yes

//Here we right click once.  
[TheNewEconomy] [DEBUG MODE]Original Cursor: ItemStack{GOLD_ORE x 2}  
[TheNewEconomy] [DEBUG MODE]New Cursor: ItemStack{GOLD_ORE x 1}  
[TheNewEconomy] [DEBUG MODE]Original ItemStack: ItemStack{GOLD_ORE x 3}  
[TheNewEconomy] [DEBUG MODE]New ItemStack: ItemStack{GOLD_ORE x 3}  

//Here is a second right click.  
[TheNewEconomy] [DEBUG MODE]Original Cursor: ItemStack{GOLD_ORE x 1}  
[TheNewEconomy] [DEBUG MODE]New Cursor: ItemStack{AIR x 0}  
[TheNewEconomy] [DEBUG MODE]Original ItemStack: ItemStack{GOLD_ORE x 4}  
[TheNewEconomy] [DEBUG MODE]New ItemStack: ItemStack{GOLD_ORE x 4}  

StartTest: Right Click Filled Slot with filled cursor dif type, GoldOre:4 - BeetRoot:3
==
#### Author: creatorfromhell:
#### Results have proven that merely comparing the original, and new slot will work for this item.
#### Trackable: Yes

[TheNewEconomy] [DEBUG MODE]Original Cursor: ItemStack{BEETROOT x 3}  
[TheNewEconomy] [DEBUG MODE]New Cursor: ItemStack{GOLD_ORE x 4}  
[TheNewEconomy] [DEBUG MODE]Original ItemStack: ItemStack{GOLD_ORE x 4}  
[TheNewEconomy] [DEBUG MODE]New ItemStack: ItemStack{BEETROOT x 3}  

StartTest: ShiftClick from top -> bottom inventory
==
#### Author: creatorfromhell:
#### Results have proven that any form of shift click will require extra effort
#### Trackable: To Be Determined

[TheNewEconomy] [DEBUG MODE]Original Cursor: ItemStack{AIR x 0}  
[TheNewEconomy] [DEBUG MODE]New Cursor: ItemStack{AIR x 0}  
[TheNewEconomy] [DEBUG MODE]Original ItemStack: ItemStack{AIR x 0}  
[TheNewEconomy] [DEBUG MODE]New ItemStack: empty  

StartTest: ShiftClick from bottom-> top inventory
==
#### Author: creatorfromhell:
#### Results have proven that any form of shift click will require extra effort
#### Trackable: To Be Determined
