Currency System
==============================
It's time to setup a currency system for your server. This is the time to decide what direction you want to take your 
server's economy. Do you need one currency for the whole server? Or maybe multiple currencies per world, either way TNE 
supports it all. Here I'll cover a few pointers for your new currency system.

### In-Depth Configurations

##### Format
<i>The format configuration allows you to control how money information is presented when sent to the player. Please note
for all examples that follow we will be using the United States Dollar(USD) as a reference.</i>

Example:   
`Format: <symbol><major.amount><decimal><minor.amount>`

Output(For a balanace of 10 dollars and 30 cents):    
`$10.30`

Here's a small list of the variables that are available for you to use in the format configuration option.
* \<symbol> - The currency's symbol
* \<decimal> - The currency's decimal.
* \<major> - A combination of the currency's major amount and name
* \<minor> - A combination of the currency's minor amount and name
* \<major.name> - The currency's major name.
* \<minor.name> - The currency's minor name.
* \<major.amount> - The currency's major amount.
* \<minor.amount> - The currency's minor amount.
* \<short.amount> - The currency's shortened amount.
* \<shorten> - Added to make the outputted value shortened    

Extra Notes:
* If shortened, it will ignore the value of Format.
* It's also possible to include all colour variables from messages.yml

##### Balance
<i>This is simply the initial balance for this currency.</i>

Example:    
`Balance: 200.0`

##### Default
<i>Controls whether or not this currency is the default currency for the world, or server depending on how you setup your
economy.</i>

Example:     
`Default: true`

##### Conversion
<i>This is the conversion power for this currency. This is based on a decimal system, where 1.0 is 100% and 0 is 0%.</i>

Example:     
`Conversion:1.0`

##### Symbol
<i>This is the character(s) you wish to use as a symbol for this currency.</i>

Example:      
`Symbol: $`

##### Decimal
<i>The character(s) to use as the decimal place holder for this currency.</i>

Example:     
`Decimal: .`

##### ItemCurrency
<i>Whether or not you'd like this currency to be item-based.</i>

Example:    
`ItemCurrency: false`

##### ItemMajor
<i><b>Item-based currencies only.</b> The name of the item to use as the major currency.</i>

Example:    
`ItemMajor: GOLD_INGOT`

##### ItemMinor
<i><b>Item-based currencies only.</b> The name of the item to use as the minor currency.</i>

Example:    
`ItemMinor: IRON_INGOT`

##### MajorName
<i>The name of the major currency.</i>

Example:    
`The singular name.`    
`Single: Dollar`    
`The plural name.`    
`Plural: Dollars`

##### MinorName
<i>The name of the minor currency.</i>

Example:    
`The singular name.`    
`Single: Cent`    
`The plural name.`    
`Plural: Cents`