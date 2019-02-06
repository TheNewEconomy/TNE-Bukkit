CREATE TABLE IF NOT EXISTS `prefix_INFO` (
  `id` INTEGER NOT NULL UNIQUE,
	`version` VARCHAR(10),
	`server_name` VARCHAR(250)
);

CREATE TABLE IF NOT EXISTS `prefix_ECOIDS` (
  `username` VARCHAR(56),
	`uuid` VARCHAR(36) UNIQUE
);

CREATE TABLE IF NOT EXISTS `prefix_USERS` (
  `uuid` VARCHAR(36) NOT NULL UNIQUE,
	`joined_date` BIGINT(60),
	`last_online` BIGINT(60),
	`account_number` INTEGER,
	`account_status` VARCHAR(60),
	`account_special` BOOLEAN
);

CREATE TABLE IF NOT EXISTS `prefix_BALANCES` (
  `uuid` VARCHAR(36) NOT NULL,
	`server_name` VARCHAR(250) NOT NULL,
	`world` VARCHAR(50) NOT NULL,
	`currency` VARCHAR(250) NOT NULL,
	`balance` VARCHAR(41),
	PRIMARY KEY(uuid, server_name, world, currency)
);

CREATE TABLE IF NOT EXISTS `prefix_TRANSACTIONS` (
  `trans_id` VARCHAR(36) NOT NULL,
  `trans_initiator` VARCHAR(36),
  `trans_initiator_balance` VARCHAR(41),
  `trans_recipient` VARCHAR(36) NOT NULL,
  `trans_recipient_balance` VARCHAR(41),
  `trans_type` VARCHAR(36) NOT NULL,
  `trans_world` VARCHAR(36) NOT NULL,
  `trans_time` BIGINT(60) NOT NULL,
  `trans_voided` BOOLEAN NOT NULL,
  PRIMARY KEY(trans_id)
);

CREATE TABLE IF NOT EXISTS `prefix_CHARGES` (
  `charge_transaction` VARCHAR(36) NOT NULL,
  `charge_player` VARCHAR(36) NOT NULL,
  `charge_currency` VARCHAR(250) NOT NULL,
  `charge_world` VARCHAR(36) NOT NULL,
  `charge_amount` VARCHAR(41) NOT NULL,
  `charge_type` VARCHAR(20) NOT NULL,
  PRIMARY KEY(charge_transaction, charge_player)
);