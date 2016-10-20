Anything with {} are required arguments, () are optional.

| Node                | Description                                                                           | Command |
|---------------------|---------------------------------------------------------------------------------------|---------|
| tne.*               | Gives access to ALL The New Economy commands listed below | |
| tne.admin.*         | Gives access to all The New Economy Admin Commands and bypasses.                      | /tne    |
| tne.admin           | | |
| tne.admin.backup    | Saves & back ups the TNE Database file.(currently only FlatFile and SQLITE)           | /tne backup
| tne.admin.balance   | Check the specified player's balance                                                  | /tne balance {player} (world)
| tne.admin.bank      | View the specified player's bank                                                      | /tne bank {player} (world)
| tne.admin.create    | Create an account with {player} as the username. Optional starting balance parameter. | /tne create {player} (balance)
| tne.admin.delete    | Delete {player}'s account.                                                            | /tne delete {player}
| tne.admin.help      | General TNE help                                                                      | /tne help
| tne.admin.id        | Get {player}'s id to be used for player configurations                                | /tne id {player}
| tne.admin.purge     | Remove all accounts that have the default balance                                     | /tne purge
| tne.admin.reload    | Reload the TNE configurations or reload the specified file                            | /tne reload {all/config/mobs/worlds}
| tne.admin.save      | Force saves all TNE data                                                              | /tne save
| tne.auction.start   | Start a new auction                                                                   | /tne start {configuration}
| tne.auction.bid     | Place a bid on an auction                                                             | /auction bid {bid} {lot}
| tne.auction.claim   | Claim an auction reward                                                               | /auction claim {lot}
| tne.auction.cancel  | Cancel an auction                                                                     | /auction cancel {lot}
| tne.auction.info    | View auction information                                                              | /auction info {lot}
| tne.auction.list    | View a list of auctions                                                               | /auction list {global/world} {page}
| tne.auction.end     | Force an end to any auctions                                                          | /auction end {winner} {lot}
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
| tne.bypass.*        | Bypasses all costs | |
| tne.bypass          | | |
| tne.bypass.bank     | Bypass all bank costs | |
| tne.bypass.inventory| Bypass all inventory costs | |
| tne.bypass.nametag  | Bypass all nametag costs | |
| tne.bypass.world    | Bypass all world costs | |
| tne.credit.*        | Gives access to all Credit commands | |
| tne.credit          | | |
| tne.credit.commands | View all command credits you have accumulated                                         | /credit commands
| tne.credit.inventory| View time credits for {inventory} in every world                                      | /credit inventory {inventory}
| tne.lottery.*       | Soon™ | |
| tne.lottery.create  | Soon™ | |
| tne.lottery.view    | Soon™ | |
| tne.money.*         | Gives access to all Money commands | |
| tne.money           | | |
| tne.money.help      | General money help                                                                    | /money help
| tne.money.balance   | Find out how much money you have on you                                               | /money balance
| tne.money.give      | Summon money from air and give it to a player                                         | /money give
| tne.money.set       | Set {player}'s balance to {amount}                                                    | /money set {player} {amount} (world)
| tne.money.take      | Make some of {player}'s money vanish into thin air                                    | /money take {player} {amount}
| tne.money.pay       | Pay a player money from your balance                                                  | /money pay {player} {amount}
| tne.package.*       | Give access to all Package commands | |
| tne.package         | | |
| tne.package.list    | List all packages for the specified inventory {type}                                  | /package list {type}
| tne.package.buy     | Buy {package} for inventory {type}                                                    | /package buy {type} {package}
| tne.pin.*           | Gives access to all Pin commands | |
| tne.pin             | | |
| tne.pin.set         | Set your pin to {pin}'s value. Old pin is required if you have one set. Pins are case-sensitive   | /pin set {pin} {confirm pin} (old pin)
| tne.pin.confirm     | Cofirm your identity with your account pin. Pins are case-sensitive                               | /pin confirm {pin}
| tne.shop.*          | Gives access to all Shop commands | |
| tne.shop            | | |
| tne.shop.admin      | | |
| tne.shop.add        | Add a new item to your shop for (cost) and/or (trade). Leave out item name to use currently held item.  | /shop add {shop} (amount:#) (item name) (stock:#) (gold:#) (trade:name:amount(default 1))
| tne.shop.blacklist  | Add/remove the specified player to the shop's blacklist                                                 | /shop blacklist {name} {player}
| tne.shop.browse     | Browse the spcified shop's inventory                                                                    | /shop browse {name}
| tne.shop.close      | Close the specified shop                                                                                | /shop close {name}
| tne.shop.create     | Create a new shop. (admin) yes/no, (hidden) yes/no                                                      | /shop create {name} (admin) (hidden)
| tne.shop.remove     | Remove a specific item from your shop. Cost is required if multiple entries exist                       | /shop remove {name} {amount} (item) (cost(gold:amount or trade:name:amount))
| tne.shop.share      | Allow/disallow profit sharing with another player                                                       | /shop share {name} {player} (percent or decimal)
| tne.shop.stock      | Add/Remove stock of an item to your shop for (gold) and/or (trade). Leave out item name to use currently held item  | /shop stock {shop} {add/remove} (quantity:#) (amount:#) (item name(:damage)) (type:(sell/buy)) (gold:#)
| tne.shop.toggle     | Toggle this shop's visibility. Only whitelisted players can buy from hidden shops                       | /shop toggle {name}
| tne.shop.whitelist  | Add/remove the specified player to the shop's whitelist                                                 | /shop whitelist {name} {player}
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
