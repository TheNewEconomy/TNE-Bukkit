Anything with {} are required arguments, () are optional.

Admin
-----------

| Node                | Description                                                                           | Command |
|---------------------|---------------------------------------------------------------------------------------|---------|
| tne.*               | Gives access to ALL The New Economy commands listed below | |
| tne.admin.*         | Gives access to all The New Economy Admin Commands and bypasses.                      | /tne    |
| tne.admin           | | |
| tne.admin.backup    | Saves & back ups the TNE Database file.(currently only FlatFile and SQLITE)           | /tne backup |
| tne.admin.balance   | Check the specified player's balance                                                  | /tne balance {player} (world) |
| tne.admin.bank      | View the specified player's bank                                                      | /tne bank {player} (world) |
| tne.admin.create    | Create an account with {player} as the username. Optional starting balance parameter. | /tne create {player} (balance) |
| tne.admin.config    | Gets or sets the specified configuration node.                                        | /tne config {set/get} {node} (value) |
| tne.admin.delete    | Delete {player}'s account.                                                            | /tne delete {player} |
| tne.admin.help      | General TNE help                                                                      | /tne help |
| tne.admin.history   | See a detailed break down of your transaction history.                                | /tne history {player} (page:#) (world:name/all) (type:type/all) |
|                     | (page) - The page number you wish to view                                             |
|                     | (world) - The world name you wish to filter, or all for every world. Defaults to current world |
|                     | (type) - The transaction type you wish to filter, or all for every transaction.       |
| tne.admin.id        | Get {player}'s id to be used for player configurations                                | /tne id {player} |
| tne.admin.pin       | Reset {username}'s pin.                                                               | /tne pin {username} {new pin} |
| tne.admin.purge     | Remove all accounts that have the default balance                                     | /tne purge |
| tne.admin.recreate  | Attempts to recreate database tables                                                  | /tne recreate |
| tne.admin.reload    | Reload the TNE configurations or reload the specified file                            | /tne reload {all/config/mobs/worlds} |
| tne.admin.save      | Force saves all TNE data                                                              | /tne save |
| tne.admin.status    | Set {username}'s account status. Valid options:Normal, Locked, BalanceLocked, and BankLocked. | /tne status {username} {status} |

Auction
-----------

| Node                | Description                                                                           | Command |
|---------------------|---------------------------------------------------------------------------------------|---------|
| tne.auction         |                                                                                       |         |
| tne.auction.bid     | Place a bid on an auction                                                             | /auction bid {bid} {lot} |
| tne.auction.cancel  | Cancel an auction                                                                     | /auction cancel {lot} |
| tne.auction.claim   | Claim an auction reward                                                               | /auction claim {lot} |
| tne.auction.end     | Force an end to any auctions                                                          | /auction end {winner} {lot} |
| tne.auction.info    | View auction information                                                              | /auction info {lot} |
| tne.auction.list    | View a list of auctions                                                               | /auction list {global/world} {page} |
| tne.auction.start   | Start a new auction,                                                                  | /tne start {configuration} |
|                     | {item:{data value}} - The name of the item to auction off, defaults to held item,     |         |
|                     | {amount:#} - The amount of {item} to auction off,                                     |         |
|                     | {start:#} - The starting bid for this item,                                           |         |
|                     | {increment:#} - The increment in which bids will be increased,                        |         |
|                     | {time:#} - The length(in seconds) this auction will go on for,                        |         |
|                     | {silent:true/false} - Whether or not this auction is a silent auction,                |         |
|                     | {global:true/false} - Whether or not this auction is global or world-based,           |         |
|                     | {permission:node} - The permission needed to partake in this auction.                 |         |

Bank
-----------

| Node                | Description                                                                           | Command |
|---------------------|---------------------------------------------------------------------------------------|---------|
| tne.bank.*          | Gives access to all Bank commands | |
| tne.bank            | | |
| tne.bank.help       | General bank help                                                                     | /bank help
| tne.bank.add        | Add {player} to your bank                                                             | /bank add
| tne.bank.balance    | Find out how much gold is in your bank                                                | /bank balance
| tne.bank.buy        | Buy yourself a bank                                                                   | /bank buy
| tne.bank.deposit    | Put the specified amount of money in your bank                                        | /bank deposit
| tne.bank.price      | See how much a bank cost                                                              | /bank price
| tne.bank.remove     | Remove {player} from your bank                                                        | /bank remove {player}
| tne.bank.use        | | |
| tne.bank.view       | View your bank                                                                        | /bank view
| tne.bank.withdraw   | Withdraw the specified amout of money from your bank                                  | /bank withdraw {amount}

Bypass
-----------

| Node                | Description                                                                           | Command |
|---------------------|---------------------------------------------------------------------------------------|---------|
| tne.bypass.*        | Bypasses all costs | |
| tne.bypass          | | |
| tne.bypass.bank     | Bypass all bank costs | |
| tne.bypass.inventory| Bypass all inventory costs | |
| tne.bypass.nametag  | Bypass all nametag costs | |
| tne.bypass.world    | Bypass all world costs | |

Credit
-----------

| Node                | Description                                                                           | Command |
|---------------------|---------------------------------------------------------------------------------------|---------|
| tne.credit.*        | Gives access to all Credit commands | |
| tne.credit          | | |
| tne.credit.commands | View all command credits you have accumulated                                         | /credit commands
| tne.credit.inventory| View time credits for {inventory} in every world                                      | /credit inventory {inventory}
| tne.lottery.*       | Soon™ | |
| tne.lottery.create  | Soon™ | |
| tne.lottery.view    | Soon™ | |

Eco
-----------

| Node                | Description                                                                           | Command |
|---------------------|---------------------------------------------------------------------------------------|---------|
| tne.eco.clean       | Fixes all items in the inventory that contain Crafting Cost in the lore.              | /eco clean |

Lottery(Upcoming)
-----------

| Node                | Description                                                                           | Command |
|---------------------|---------------------------------------------------------------------------------------|---------|
| tne.lottery.*       | Soon™ | |
| tne.lottery.create  | Soon™ | |
| tne.lottery.view    | Soon™ | |

Money
-----------

| Node                | Description                                                                           | Command |
|---------------------|---------------------------------------------------------------------------------------|---------|
| tne.money.*         | Gives access to all Money commands | |
| tne.money           | | |
| tne.money.history   | See a detailed break down of your transaction history.                                | /money history {page:#} {world:name/all} {type:type/all} |
| tne.money.help      | General money help                                                                    | /money help |
| tne.money.balance   | Find out how much money you have on you                                               | /money balance |
| tne.money.convert   | Convert some of your funds from one currency to another.                              | /money convert {amount} {to currency(:world)} (from currency(:world)) |
| tne.money.give      | Summon money from air and give it to a player                                         | /money give |
| tne.money.set       | Set {player}'s balance to {amount}                                                    | /money set {player} {amount} (world) |
| tne.money.take      | Make some of {player}'s money vanish into thin air                                    | /money take {player} {amount} |
| tne.money.pay       | Pay a player money from your balance                                                  | /money pay {player} {amount} |

Package
-----------

| Node                | Description                                                                           | Command |
|---------------------|---------------------------------------------------------------------------------------|---------|
| tne.package.*       | Give access to all Package commands | |
| tne.package         | | |
| tne.package.list    | List all packages for the specified inventory {type}                                  | /package list {type} |
| tne.package.buy     | Buy {package} for inventory {type}                                                    | /package buy {type} {package} |

Pin
-----------

| Node                | Description                                                                           | Command |
|---------------------|---------------------------------------------------------------------------------------|---------|
| tne.pin.*           | Gives access to all Pin commands                                                      |  |
| tne.pin             |                                                                                       |  |
| tne.pin.set         | Set your pin to {pin}'s value. Old pin is required if you have one set. Pins are case-sensitive   | /pin set {pin} {confirm pin} (old pin) |
| tne.pin.confirm     | Confirm your identity with your account pin. Pins are case-sensitive                               | /pin confirm {pin} |

Server(Coming in 5.5)
-----------
| Node                | Description                                                                           | Command |
|---------------------|---------------------------------------------------------------------------------------|---------|
| tne.server.*        | Gives access to all actions related to the server's economy account.                  |  |
| tne.server.bank     | Gives access to the server's bank accounts.                                           |  |
| tne.server.pay      | Gives access to paying the server account.                                            |  |
| tne.server.give     | Gives access to giving the server account funds.                                      |  |
| tne.server.set      | Gives access to setting the server account's balances.                                |  |
| tne.server.take     | Gives access to taking funds from the server account.                                 |  |
| tne.server.balance  | Gives access to checking the server account's balances.                               |  |

Shop
-----------

| Node                | Description                                                                           | Command |
|---------------------|---------------------------------------------------------------------------------------|---------|
| tne.shop.*          | Gives access to all Shop commands | |
| tne.shop            | | |
| tne.shop.admin      | | |
| tne.shop.add        | Add a new item to your shop for (cost) and/or (trade). Leave out item name to use currently held item.  | /shop add {shop} (amount:#) (item name) (stock:#) (gold:#) (trade:name:amount(default 1)) |
| tne.shop.blacklist  | Add/remove the specified player to the shop's blacklist                                                 | /shop blacklist {name} {player} |
| tne.shop.browse     | Browse the spcified shop's inventory                                                                    | /shop browse {name} |
| tne.shop.close      | Close the specified shop                                                                                | /shop close {name} |
| tne.shop.create     | Create a new shop. (admin) yes/no, (hidden) yes/no                                                      | /shop create {name} (admin) (hidden) |
| tne.shop.remove     | Remove a specific item from your shop. Cost is required if multiple entries exist                       | /shop remove {name} {amount} (item) (cost(gold:amount or trade:name:amount)) |
| tne.shop.share      | Allow/disallow profit sharing with another player                                                       | /shop share {name} {player} (percent or decimal) |
| tne.shop.stock      | Add/Remove stock of an item to your shop for (gold) and/or (trade). Leave out item name to use currently held item  | /shop stock {shop} {add/remove} (quantity:#) (amount:#) (item name(:damage)) (type:(sell/buy)) (gold:#) |
| tne.shop.toggle     | Toggle this shop's visibility. Only whitelisted players can buy from hidden shops                       | /shop toggle {name} |
| tne.shop.whitelist  | Add/remove the specified player to the shop's whitelist                                                 | /shop whitelist {name} {player} |

Placement and use of all signs
-----------

| Node                | Description                                                                           | Command |
|---------------------|---------------------------------------------------------------------------------------|---------|
| tne.place.*         | Gives access to creating all sign types | |
| tne.place           | | |
| tne.place.bank      | Can create Bank signs | |
| tne.place.buy       | Can create Buy signs | |
| tne.place.sell      | Can create Sell signs | |
| tne.place.shop      | Can create Shop signs | |
| tne.use.*           | Gives access to use all sign types | |
| tne.use             | | |
| tne.use.bank        | Can use Bank signs | |
| tne.use.buy         | Can use Buy signs | |
| tne.use.sell        | Can use Sell signs | |
| tne.use.shop        | Can use Shop signs | |



Parent permissions
-----------

This section is a complete list of all Parent and Child permissions. If you are unsure how this works, let me try to explain.

`parent.permission` = This permission will give you every permission listed below.
- `child.permission.one` = This permission does not need to be given to anyone with the parent permission
- `child.permission.two` = This permission does not need to be given to anyone with the parent permission

Use these in place of typing out each individual permission node.

| Parent | Children |
|-----|----|
| tne.*:| |
| | tne.admin
| | tne.admin.backup
| | tne.admin.balance
| | tne.admin.bank
| | tne.admin.create
| | tne.admin.config
| | tne.admin.delete
| | tne.admin.help
| | tne.admin.history
| | tne.admin.pin
| | tne.admin.purge
| | tne.admin.recreate
| | tne.admin.reload
| | tne.admin.save
| | tne.admin.status
| | tne.auction
| | tne.auction.bid
| | tne.auction.cancel
| | tne.auction.claim
| | tne.auction.end
| | tne.auction.info
| | tne.auction.list
| | tne.auction.start
| | tne.bank
| | tne.bank.help
| | tne.bank.add
| | tne.bank.balance
| | tne.bank.buy
| | tne.bank.deposit
| | tne.bank.price
| | tne.bank.remove
| | tne.bank.use
| | tne.bank.withdraw
| | tne.bypass
| | tne.bypass.bank
| | tne.bypass.inventory
| | tne.bypass.nametag
| | tne.bypass.world
| | tne.credit
| | tne.credit.commands
| | tne.credit.inventory
| | tne.eco.claim
| | tne.money
| | tne.money.history
| | tne.money.help
| | tne.money.balance
| | tne.money.convert
| | tne.money.give
| | tne.money.set
| | tne.money.take
| | tne.money.pay
| | tne.package
| | tne.package.list
| | tne.package.buy
| | tne.pin
| | tne.pin.set
| | tne.pin.confirm
| | tne.shop
| | tne.shop.admin
| | tne.shop.add
| | tne.shop.blacklist
| | tne.shop.browse
| | tne.shop.close
| | tne.shop.create
| | tne.shop.remove
| | tne.shop.share
| | tne.shop.stock
| | tne.shop.toggle
| | tne.shop.whitelist
| | tne.place
| | tne.place.bank
| | tne.place.buy
| | tne.place.sell
| | tne.place.shop
| | tne.use
| | tne.use.bank
| | tne.use.buy
| | tne.use.sell
| | tne.use.shop

| Parent | Children |
|-----|----|
| tne.admin.* | |
| | tne.admin
| | tne.admin.backup
| | tne.admin.balance
| | tne.admin.bank
| | tne.admin.create
| | tne.admin.config
| | tne.admin.delete
| | tne.admin.help
| | tne.admin.history
| | tne.admin.purge
| | tne.admin.reload
| | tne.admin.save
| | tne.bypass.bank
| | tne.bypass.inventory
| | tne.bypass.nametag
| | tne.bypass.world
| | tne.shop.admin

| Parent | Children |
|-----|----|
| tne.bank.* | |
| | tne.bank
| | tne.bank.help
| | tne.bank.add
| | tne.bank.balance
| | tne.bank.buy
| | tne.bank.deposit
| | tne.bank.price
| | tne.bank.remove
| | tne.bank.use
| | tne.bank.withdraw

| Parent | Children |
|-----|----|
| tne.bypass.* | |
| | tne.bypass
| | tne.bypass.bank
| | tne.bypass.inventory
| | tne.bypass.nametag
| | tne.bypass.world

| Parent | Children |
|-----|----|
| tne.credit.* | |
| | tne.credit
| | tne.credit.commands
| | tne.credit.inventory

| Parent | Children |
|-----|----|
| tne.lottery.* | |
| | tne.lottery.create
| | tne.lottery.view

| Parent | Children |
|-----|----|
| tne.money.* | |
| | tne.money
| | tne.money.help
| | tne.money.balance
| | tne.money.convert
| | tne.money.give
| | tne.money.set
| | tne.money.take
| | tne.money.pay

| Parent | Children |
|-----|----|
| tne.package.* | |
| | tne.package
| | tne.package.list
| | tne.package.buy

| Parent | Children |
|-----|----|
| tne.pin.* | |
| | tne.pin
| | tne.pin.set
| | tne.pin.confirm

| Parent | Children |
|-----|----|
| tne.shop.* | |
| | tne.shop
| | tne.shop.add
| | tne.shop.blacklist
| | tne.shop.browse
| | tne.shop.close
| | tne.shop.create
| | tne.shop.remove
| | tne.shop.share
| | tne.shop.stock
| | tne.shop.toggle
| | tne.shop.whitelist

| Parent | Children |
|-----|----|
| tne.place.* | |
| | tne.place
| | tne.place.bank
| | tne.place.buy
| | tne.place.sell
| | tne.place.shop

| Parent | Children |
|-----|----|
| tne.use.* | |
| | tne.use
| | tne.use.bank
| | tne.use.buy
| | tne.use.sell
| | tne.use.shop
