Commands
==========

Anything with {} are required arguments, () are optional.

##Admin Commands

| Command                                                                       | Permission          | Description                                                                           |
|-------------------------------------------------------------------------------|---------------------|---------------------------------------------------------------------------------------|
| /tne                                                                          | tne.admin.*         | Gives access to all The New Economy Admin Commands and bypasses.                      |
| /tne backup                                                                   | tne.admin.backup    | Saves & back ups the TNE Database file.(currently only FlatFile and SQLITE)           |
| /tne balance {player} (world)                                                 | tne.admin.balance   | Check the specified player's balance                                                  |
| /tne bank {player} (world)                                                    | tne.admin.bank      | View the specified player's bank                                                      |
| /tne create {player} (balance)                                                | tne.admin.create    | Create an account with {player} as the username. Optional starting balance parameter. |
| /tne delete {player}                                                          | tne.admin.delete    | Delete {player}'s account.                                                            |
| /tne help                                                                     | tne.admin.help      | General TNE help                                                                      |
| /tne id {player}                                                              | tne.admin.id        | Get {player}'s id to be used for player configurations                                |
| /tne purge                                                                    | tne.admin.purge     | Remove all accounts that have the default balance                                     |
| /tne reload {all/config/mobs/worlds}                                          | tne.admin.reload    | Reload the TNE configurations or reload the specified file                            |
| /tne save                                                                     | tne.admin.save      | Force saves all TNE data                                                              |

##Auction

| Command                                                                       | Permission          | Description                                                                           |
|-------------------------------------------------------------------------------|---------------------|---------------------------------------------------------------------------------------|
| /auction                                                                      |                     | Base auction command                                                                  |
| /auction start {configurations}                                               | tne.auction.start   | Start a new auction,                                                                  |
|                                                                               |                     | {item:{data value}} - The name of the item to auction off, defaults to held item,     |
|                                                                               |                     | {amount:#} - The amount of {item} to auction off,                                     |
|                                                                               |                     | {start:#} - The starting bid for this item,                                           |
|                                                                               |                     | {increment:#} - The increment in which bids will be increased,                        |
|                                                                               |                     | {time:#} - The length(in seconds) this auction will go on for,                        |
|                                                                               |                     | {silent:true/false} - Whether or not this auction is a silent auction,                |
|                                                                               |                     | {global:true/false} - Whether or not this auction is global or world-based,           |
|                                                                               |                     | {permission:node} - The permission needed to partake in this auction.                 |
| /auction bid {bid} (lot)                                                      | tne.auction.bid     | Place a bid on an auction.                                                            |
| /auction claim {lot}                                                          | tne.auction.claim   | Claim an auction reward                                                               |
| /auction cancel {lot}                                                         | tne.auction.cancel  | Cancel an auction. NOTE: Only administrators are allowed to cancel auctions with bids |
| /auction info {lot}                                                           | tne.auction.info    | View auction information                                                              |
| /auction list {global/world} {page}                                           | tne.auction.list    | View a list of auctions. {global/world} - The scope to list, leave blank to show your auctions |
| /auction end {winner} {lot}                                                   | tne.auction.end     | Force an end to any auction. {winner(true/false)} = Pick winner?                      |

bid = Amount to bid
lot = The auction's lot number.


##Bank

| Command                                                                       | Permission          | Description                                                                           |
|-------------------------------------------------------------------------------|---------------------|---------------------------------------------------------------------------------------|
|                                                                               | tne.bank.*          | Gives access to all Bank commands                                                     |
| /bank help                                                                    | tne.bank.help       | General bank help                                                                     |
| /bank add                                                                     | tne.bank.add        | Add {player} to your bank                                                             |
| /bank balance                                                                 | tne.bank.balance    | Find out how much gold is in your bank                                                |
| /bank buy                                                                     | tne.bank.buy        | Buy yourself a bank                                                                   |
| /bank deposit                                                                 | tne.bank.deposit    | Put the specified amount of money in your bank                                        |
| /bank price                                                                   | tne.bank.price      | See how much a bank cost                                                              |
| /bank remove {player}                                                         | tne.bank.remove     | Remove {player} from your bank                                                        |
|                                                                               | tne.bank.use        |                                                                                       |
| /bank view                                                                    | tne.bank.view       | View your bank                                                                        |
| /bank withdraw {amount}                                                       | tne.bank.withdraw   | Withdraw the specified amout of money from your bank                                  |


##Credit

| Command                                                                       | Permission          | Description                                                                           |
|-------------------------------------------------------------------------------|---------------------|---------------------------------------------------------------------------------------|
|                                                                               | tne.credit.*        | Gives access to all Credit commands                                                   |
|                                                                               | tne.credit          |                                                                                       |
| /credit commands                                                              | tne.credit.commands | View all command credits you have accumulated                                         |
| /credit inventory {inventory}                                                 | tne.credit.inventory| View time credits for {inventory} in every world                                      |


##Money

| Command                                                                       | Permission          | Description                                                                           |
|-------------------------------------------------------------------------------|---------------------|---------------------------------------------------------------------------------------|
|                                                                               | tne.money.*         | Gives access to all Money commands                                                    |
|                                                                               | tne.money           |                                                                                       |
| /money help                                                                   | tne.money.help      | General money help                                                                    |
| /money balance                                                                | tne.money.balance   | Find out how much money you have on you                                               |
| /money give                                                                   | tne.money.give      | Summon money from air and give it to a player                                         |
| /money set {player} {amount} (world)                                          | tne.money.set       | Set {player}'s balance to {amount}                                                    |
| /money take {player} {amount}                                                 | tne.money.take      | Make some of {player}'s money vanish into thin air                                    |
| /money pay {player} {amount}                                                  | tne.money.pay       | Pay a player money from your balance                                                  |


##Package

| Command                                                                       | Permission          | Description                                                                           |
|-------------------------------------------------------------------------------|---------------------|---------------------------------------------------------------------------------------|
|                                                                               | tne.package.*       | Give access to all Package commands                                                   |
| /package list {type}                                                          | tne.package.list    | List all packages for the specified inventory {type}                                  |
| /package buy {type} {package}                                                 | tne.package.buy     | Buy {package} for inventory {type}                                                    |


##Pin

| Command                                                                       | Permission          | Description                                                                           |
|-------------------------------------------------------------------------------|---------------------|---------------------------------------------------------------------------------------|
|                                                                               | tne.pin.*           | Gives access to all Pin commands                                                      |
| /pin set {pin} {confirm pin} (old pin)                                        | tne.pin.set         | Set your pin to {pin}'s value. Old pin is required if you have one set. Pins are case-sensitive   |
| /pin confirm {pin}                                                            | tne.pin.confirm     | Cofirm your identity with your account pin. Pins are case-sensitive                   |


##Shop

| Command                                                                       | Permission          | Description                                                                           |
|-------------------------------------------------------------------------------|---------------------|---------------------------------------------------------------------------------------|
|                                                                               | tne.shop.*          | Gives access to all Shop commands                                                     |
|                                                                               | tne.shop.admin      |                                                                                       |
| /shop add {shop} (amount:#) (item name) (stock:#) (gold:#) (trade:name:amount(default 1))   | tne.shop.add        | Add a new item to your shop for (cost) and/or (trade). Leave out item name to use currently held item.  |
| /shop blacklist {name} {player}                                               | tne.shop.blacklist  | Add/remove the specified player to the shop's blacklist                                                 |
| /shop browse {name}                                                           | tne.shop.browse     | Browse the spcified shop's inventory                                                                    |
| /shop close {name}                                                            | tne.shop.close      | Close the specified shop                                                                                |
| /shop create {name} (admin) (hidden)                                          | tne.shop.create     | Create a new shop. (admin) yes/no, (hidden) yes/no                                                      |
| /shop remove {name} {amount} (item) (cost(gold:amount or trade:name:amount))  | tne.shop.remove     | Remove a specific item from your shop. Cost is required if multiple entries exist                       |
| /shop share {name} {player} (percent or decimal)                              | tne.shop.share      | Allow/disallow profit sharing with another player                                                       |
| /shop stock {shop} {add/remove} (quantity:#) (amount:#) (item name(:damage)) (type:(sell/buy)) (gold:#)   | tne.shop.stock      | Add/Remove stock of an item to your shop for (gold) and/or (trade). Leave out item name to use currently held item  |
| /shop toggle {name}                                                           | tne.shop.toggle     | Toggle this shop's visibility. Only whitelisted players can buy from hidden shops                       |
| /shop whitelist {name} {player}                                               | tne.shop.whitelist  | Add/remove the specified player to the shop's whitelist                                                 |





#Planned


##Lottery

| Command                                                                       | Permission          | Description                                                                           |
|-------------------------------------------------------------------------------|---------------------|---------------------------------------------------------------------------------------|
|                                                                               | tne.lottery.*       |                                                                                       |
| /lottery create                                                               | tne.lottery.create  |                                                                                       |
|                                                                               | tne.lottery.view    |                                                                                       |
| /lottery pot                                                                  |                     |                                                                                       |
| /lottery cost                                                                 |                     |                                                                                       |
| /lottery draw                                                                 |                     |                                                                                       |
| /lottery start                                                                |                     |                                                                                       |
| /lottery stop                                                                 |                     |                                                                                       |
| /lottery cancel                                                               |                     |                                                                                       |
| /lottery enter {# tickets}                                                    |                     |                                                                                       |
| /lottery edit                                                                 |                     |                                                                                       |
| /lottery reset                                                                |                     |                                                                                       |
