CREATE TABLE IF NOT EXISTS `info` (
  `id` INTEGER NOT NULL UNIQUE,
  `version` VARCHAR(10),
  `server_name` VARCHAR(250)
);

CREATE TABLE IF NOT EXISTS `ecoids` (
  `username` VARCHAR(56),
  `uuid` VARCHAR(36) UNIQUE
);

CREATE TABLE IF NOT EXISTS `users` (
  `uuid` VARCHAR(36) NOT NULL UNIQUE,
  `inventory_credits` LONGTEXT,
  `command_credits` LONGTEXT,
  `acc_pin` VARCHAR(30),
  `joinedDate` VARCHAR(60),
  `accountnumber` INTEGER,
  `accountstatus` VARCHAR(60),
  `account_special` BOOLEAN
);

CREATE TABLE IF NOT EXISTS `user_balances` (
  `uuid` VARCHAR(36) NOT NULL,
  `server_name` VARCHAR(250) NOT NULL,
  `world` VARCHAR(50) NOT NULL,
  `currency` VARCHAR(250) NOT NULL,
  `balance` DOUBLE,
  PRIMARY KEY(uuid, server_name, world, currency)
);

CREATE TABLE IF NOT EXISTS `shops` (
  `shop_owner` VARCHAR(36),
  `shop_world` VARCHAR(50) NOT NULL,
  `shop_name` VARCHAR(60) NOT NULL,
  `shop_hidden` TINYINT(1),
  `shop_admin` TINYINT(1),
  PRIMARY KEY(shop_name, shop_world)
);

CREATE TABLE IF NOT EXISTS `shop_shares` (
  `shop_world` VARCHAR(50) NOT NULL,
  `shop_name` VARCHAR(60) NOT NULL,
  `uuid` VARCHAR(36) NOT NULL,
  `percentage` DOUBLE,
  PRIMARY KEY(shop_world, shop_name, uuid)
);

CREATE TABLE IF NOT EXISTS `shop_permissions` (
  `shop_world` VARCHAR(50) NOT NULL,
  `shop_name` VARCHAR(60) NOT NULL,
  `uuid` VARCHAR(36) NOT NULL,
  `permissions` LONGTEXT,
  PRIMARY KEY(shop_world, shop_name, uuid)
);

CREATE TABLE IF NOT EXISTS `shop_items` (
  `shop_world` VARCHAR(50) NOT NULL,
  `shop_name` VARCHAR(60) NOT NULL,
  `shop_buy` DOUBLE,
  `shop_sell` DOUBLE,
  `shop_trade` LONGTEXT,
  `shop_stock` INT NOT NULL,
  `shop_max` INT NOT NULL,
  `shop_unlimited` BOOLEAN,
  `slot` INT(60) NOT NULL,
  `amount` INT(60) NOT NULL,
  `damage` INT(60) NOT NULL,
  `material` LONGTEXT,
  `custom_name` LONGTEXT,
  `enchantments` LONGTEXT,
  `lore` LONGTEXT,
  PRIMARY KEY(shop_name, shop_world, slot)
);

CREATE TABLE IF NOT EXISTS `auctions` (
  `auction_lot` INT(60) NOT NULL,
  `auction_added` BIGINT(60) NOT NULL,
  `auction_start` BIGINT(60) NOT NULL,
  `auction_owner` VARCHAR(36),
  `auction_world` VARCHAR(36),
  `auction_silent` TINYINT(1),
  `auction_item` LONGTEXT,
  `auction_cost` LONGTEXT,
  `auction_increment` DOUBLE,
  `auction_global` TINYINT(1),
  `auction_time` INT(20),
  `auction_node` LONGTEXT,
  PRIMARY KEY(auction_lot)
);

CREATE TABLE IF NOT EXISTS `claims` (
  `claim_player` VARCHAR(36),
  `claim_lot` INT(60) NOT NULL,
  `claim_item` LONGTEXT,
  `claim_paid` TINYINT(1),
  `claim_cost` LONGTEXT,
  PRIMARY KEY(claim_player, claim_lot)
);

CREATE TABLE IF NOT EXISTS `banks` (
  `uuid` VARCHAR(36) NOT NULL,
  `world` VARCHAR(50) NOT NULL,
  PRIMARY KEY(uuid, world)
);

CREATE TABLE IF NOT EXISTS `bank_balances` (
  `uuid` VARCHAR(36) NOT NULL,
  `world` VARCHAR(50) NOT NULL,
  `currency` VARCHAR(250) NOT NULL,
  `balance` DOUBLE,
  PRIMARY KEY(uuid, world, currency)
);

CREATE TABLE IF NOT EXISTS `bank_members` (
  `uuid` VARCHAR(36) NOT NULL,
  `world` VARCHAR(50) NOT NULL,
  `member` VARCHAR(36) NOT NULL,
  `permissions` LONGTEXT,
  PRIMARY KEY(uuid, world, member)
);

CREATE TABLE IF NOT EXISTS `vaults` (
  `uuid` VARCHAR(36) NOT NULL,
  `world` VARCHAR(50) NOT NULL,
  `size` INT(60) NOT NULL,
  PRIMARY KEY(uuid, world)
);

CREATE TABLE IF NOT EXISTS `vault_members` (
  `uuid` VARCHAR(36) NOT NULL,
  `world` VARCHAR(50) NOT NULL,
  `member` VARCHAR(36) NOT NULL,
  `permissions` LONGTEXT,
  PRIMARY KEY(uuid, world, member)
);

CREATE TABLE IF NOT EXISTS `vault_items` (
  `uuid` VARCHAR(36) NOT NULL,
  `world` VARCHAR(50) NOT NULL,
  `member` VARCHAR(36) NOT NULL,
  `slot` INT(60) NOT NULL,
  `amount` INT(60) NOT NULL,
  `damage` INT(60) NOT NULL,
  `material` LONGTEXT,
  `custom_name` LONGTEXT,
  `enchantments` LONGTEXT,
  `lore` LONGTEXT,
  PRIMARY KEY(uuid, world, slot)
);

CREATE TABLE IF NOT EXISTS `signs` (
  `sign_owner` VARCHAR(36),
  `sign_type` VARCHAR(30) NOT NULL,
  `sign_location` VARCHAR(230) NOT NULL,
  `sign_meta` LONGTEXT,
  PRIMARY KEY(sign_location)
);

CREATE TABLE IF NOT EXISTS `sign_offers` (
  `sign_location` VARCHAR(230) NOT NULL,
  `offer_order` INT(60) NOT NULL,
  `offer_buy` DOUBLE,
  `offer_sell` DOUBLE,
  `offer_trade` LONGTEXT,
  `offer_amount` INT(60) NOT NULL,
  `offer_damage` INT(60) NOT NULL,
  `offer_material` LONGTEXT,
  `offer_admin` BOOLEAN,
  PRIMARY KEY(sign_location, offer_order)
);

CREATE TABLE IF NOT EXISTS `transactions` (
  `trans_id` VARCHAR(36),
  `trans_initiator` VARCHAR(36),
  `trans_player` VARCHAR(36),
  `trans_world` VARCHAR(36),
  `trans_type` VARCHAR(36),
  `trans_cost` DOUBLE,
  `trans_oldBalance` DOUBLE,
  `trans_balance` DOUBLE,
  `trans_time` BIGINT(60),
  PRIMARY KEY(trans_id)
);