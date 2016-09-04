Lottery
=======


Commands
--------
/lottery
/lottery create <name> [configurations]  - Create a lottery  
/lottery pot <name>   - View this lottery's pot  
/lottery cost <name>  - View the cost of a ticket for this lottery  
/lottery draw <name>  - Force draw from this lottery  
/lottery start <name>  - Push the lottery into the pool to be run after the current one is over  
/lottery stop <name> - Remove this lottery from the pool   
/lottery cancel <name> - Cancel the current run of this lottery  
/lottery enter <name> <# tickets> - Buy tickets for this lottery  
/lottery edit <name> <configuration> <value> - Edit the configuration values of this lottery  
/lottery reset <name> - Reset this lottery's pot to the default

Configurations
--------------
Permission - The permission needed to partake in this lottery.  
Cost: Items and/or money - The cost to buy a ticket  
Reward: Items and/or money - The starting reward for this lottery  
Reset Reward: false - Whether or not to reset the lottery with the starting reward  
Min Tickets: X - Minimum amount of tickets that must be sold in order for the lottery to draw  
Max Tickets: X - Maximum amount of tickets that can be sold for this lottery  
Win Chance: 25 - The chance that a player can win the jackpot  
Global: true - Whether or not this is a global or world-specific lottery  
World: Example World Name - The name of the world this lottery is for if world-specific  
Run Time: 6000(seconds) - The amount of time this lottery runs for  
Delay: The delay between lottery runs  
MultiWin: false - Whether or not multiple players can win