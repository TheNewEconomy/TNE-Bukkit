package net.tnemc.core.commands;

import net.tnemc.commands.core.CommandsHandler;
import net.tnemc.core.commands.account.pin.AccountPinResetCommand;
import net.tnemc.core.commands.account.pin.AccountPinSetCommand;
import net.tnemc.core.commands.currency.CurrencyListCommand;
import net.tnemc.core.commands.currency.CurrencyRenameCommand;
import net.tnemc.core.commands.currency.CurrencyTiersCommand;
import net.tnemc.core.commands.dev.DeveloperDebugCommand;
import net.tnemc.core.commands.module.ModuleAvailableCommand;
import net.tnemc.core.commands.module.ModuleDownloadCommand;
import net.tnemc.core.commands.module.ModuleInfoCommand;
import net.tnemc.core.commands.module.ModuleListCommand;
import net.tnemc.core.commands.module.ModuleLoadCommand;
import net.tnemc.core.commands.module.ModuleReloadCommand;
import net.tnemc.core.commands.module.ModuleUnloadCommand;
import net.tnemc.core.commands.transaction.TransactionAwayCommand;
import net.tnemc.core.commands.transaction.TransactionHistoryCommand;
import net.tnemc.core.commands.transaction.TransactionInfoCommand;
import net.tnemc.core.commands.transaction.TransactionVoidCommand;
import net.tnemc.core.commands.yeti.YetiIdiotCommand;

/**
 * The New Economy Minecraft Server Plugin
 * <p>
 * Created by creatorfromhell on 1/7/2020.
 * <p>
 * This work is licensed under the Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/ or send a letter to
 * Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * Created by creatorfromhell on 06/30/2017.
 */
public class ExecutorsRegistry {

  public static void register() {

    //Account pin
    CommandsHandler.instance().addExecutor("pin_reset_exe", new AccountPinResetCommand());
    CommandsHandler.instance().addExecutor("pin_set_exe", new AccountPinSetCommand());

    //Currency
    CommandsHandler.instance().addExecutor("currency_list_exe", new CurrencyListCommand());
    CommandsHandler.instance().addExecutor("currency_rename_exe", new CurrencyRenameCommand());
    CommandsHandler.instance().addExecutor("currency_tiers_exe", new CurrencyTiersCommand());

    //Module
    CommandsHandler.instance().addExecutor("module_available_exe", new ModuleAvailableCommand());
    CommandsHandler.instance().addExecutor("module_download_exe", new ModuleDownloadCommand());
    CommandsHandler.instance().addExecutor("module_info_exe", new ModuleInfoCommand());
    CommandsHandler.instance().addExecutor("module_list_exe", new ModuleListCommand());
    CommandsHandler.instance().addExecutor("module_load_exe", new ModuleLoadCommand());
    CommandsHandler.instance().addExecutor("module_reload_exe", new ModuleReloadCommand());
    CommandsHandler.instance().addExecutor("module_unload_exe", new ModuleUnloadCommand());

    //Developer
    CommandsHandler.instance().addExecutor("dev_debug_exe", new DeveloperDebugCommand());

    //Transaction
    CommandsHandler.instance().addExecutor("transaction_away_exe", new TransactionAwayCommand());
    CommandsHandler.instance().addExecutor("transaction_history_exe", new TransactionHistoryCommand());
    CommandsHandler.instance().addExecutor("transaction_info_exe", new TransactionInfoCommand());
    CommandsHandler.instance().addExecutor("transaction_void_exe", new TransactionVoidCommand());

    //Yediot
    CommandsHandler.instance().addExecutor("yediot_idiot_exe", new YetiIdiotCommand());

    //Language

  }
}