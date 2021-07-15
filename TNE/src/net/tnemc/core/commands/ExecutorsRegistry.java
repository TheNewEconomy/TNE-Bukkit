package net.tnemc.core.commands;

import net.tnemc.commands.core.CommandsHandler;
import net.tnemc.core.commands.account.pin.AccountPinResetCommand;
import net.tnemc.core.commands.account.pin.AccountPinSetCommand;
import net.tnemc.core.commands.admin.AdminAccountCommand;
import net.tnemc.core.commands.admin.AdminBalanceCommand;
import net.tnemc.core.commands.admin.AdminBuildCommand;
import net.tnemc.core.commands.admin.AdminCaveatsCommand;
import net.tnemc.core.commands.admin.AdminCreateCommand;
import net.tnemc.core.commands.admin.AdminDebugCommand;
import net.tnemc.core.commands.admin.AdminDeleteCommand;
import net.tnemc.core.commands.admin.AdminExtractCommand;
import net.tnemc.core.commands.admin.AdminIDCommand;
import net.tnemc.core.commands.admin.AdminIDExportCommand;
import net.tnemc.core.commands.admin.AdminMaintenanceMode;
import net.tnemc.core.commands.admin.AdminMenuCommand;
import net.tnemc.core.commands.admin.AdminPlayerCommand;
import net.tnemc.core.commands.admin.AdminPurgeCommand;
import net.tnemc.core.commands.admin.AdminReloadCommand;
import net.tnemc.core.commands.admin.AdminReportCommand;
import net.tnemc.core.commands.admin.AdminResetCommand;
import net.tnemc.core.commands.admin.AdminRestoreCommand;
import net.tnemc.core.commands.admin.AdminSaveCommand;
import net.tnemc.core.commands.admin.AdminStatusCommand;
import net.tnemc.core.commands.admin.AdminTestCommand;
import net.tnemc.core.commands.admin.AdminUploadCommand;
import net.tnemc.core.commands.admin.AdminVersionCommand;
import net.tnemc.core.commands.admin.DeveloperIDCommand;
import net.tnemc.core.commands.config.ConfigGetCommand;
import net.tnemc.core.commands.config.ConfigSaveCommand;
import net.tnemc.core.commands.config.ConfigSetCommand;
import net.tnemc.core.commands.config.ConfigTNEGetCommand;
import net.tnemc.core.commands.currency.CurrencyEditorCommand;
import net.tnemc.core.commands.currency.CurrencyListCommand;
import net.tnemc.core.commands.currency.CurrencyRenameCommand;
import net.tnemc.core.commands.currency.CurrencyTiersCommand;
import net.tnemc.core.commands.dev.DeveloperDebugCommand;
import net.tnemc.core.commands.language.LanguageCurrentCommand;
import net.tnemc.core.commands.language.LanguageListCommand;
import net.tnemc.core.commands.language.LanguageReloadCommand;
import net.tnemc.core.commands.language.LanguageSetCommand;
import net.tnemc.core.commands.module.ModuleAvailableCommand;
import net.tnemc.core.commands.module.ModuleDownloadCommand;
import net.tnemc.core.commands.module.ModuleInfoCommand;
import net.tnemc.core.commands.module.ModuleListCommand;
import net.tnemc.core.commands.module.ModuleLoadCommand;
import net.tnemc.core.commands.module.ModuleReloadCommand;
import net.tnemc.core.commands.module.ModuleUnloadCommand;
import net.tnemc.core.commands.money.MoneyBalanceCommand;
import net.tnemc.core.commands.money.MoneyConsolidateCommand;
import net.tnemc.core.commands.money.MoneyConvertCommand;
import net.tnemc.core.commands.money.MoneyGiveCommand;
import net.tnemc.core.commands.money.MoneyNoteCommand;
import net.tnemc.core.commands.money.MoneyOtherCommand;
import net.tnemc.core.commands.money.MoneyPayCommand;
import net.tnemc.core.commands.money.MoneyPayFromCommand;
import net.tnemc.core.commands.money.MoneySetAllCommand;
import net.tnemc.core.commands.money.MoneySetCommand;
import net.tnemc.core.commands.money.MoneyTakeCommand;
import net.tnemc.core.commands.money.MoneyTopCommand;
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

    //Admin
    CommandsHandler.instance().addExecutor("admin_account_exe", new AdminAccountCommand());
    CommandsHandler.instance().addExecutor("admin_balance_exe", new AdminBalanceCommand());
    CommandsHandler.instance().addExecutor("admin_build_exe", new AdminBuildCommand());
    CommandsHandler.instance().addExecutor("admin_caveats_exe", new AdminCaveatsCommand());
    CommandsHandler.instance().addExecutor("admin_create_exe", new AdminCreateCommand());
    CommandsHandler.instance().addExecutor("admin_debug_exe", new AdminDebugCommand());
    CommandsHandler.instance().addExecutor("admin_delete_exe", new AdminDeleteCommand());
    CommandsHandler.instance().addExecutor("admin_extract_exe", new AdminExtractCommand());
    CommandsHandler.instance().addExecutor("admin_id_exe", new AdminIDCommand());
    CommandsHandler.instance().addExecutor("admin_idexport_exe", new AdminIDExportCommand());
    CommandsHandler.instance().addExecutor("admin_maintenance_exe", new AdminMaintenanceMode());
    CommandsHandler.instance().addExecutor("admin_menu_exe", new AdminMenuCommand());
    CommandsHandler.instance().addExecutor("admin_player_exe", new AdminPlayerCommand());
    CommandsHandler.instance().addExecutor("admin_purge_exe", new AdminPurgeCommand());
    CommandsHandler.instance().addExecutor("admin_reload_exe", new AdminReloadCommand());
    CommandsHandler.instance().addExecutor("admin_report_exe", new AdminReportCommand());
    CommandsHandler.instance().addExecutor("admin_reset_exe", new AdminResetCommand());
    CommandsHandler.instance().addExecutor("admin_restore_exe", new AdminRestoreCommand());
    CommandsHandler.instance().addExecutor("admin_save_exe", new AdminSaveCommand());
    CommandsHandler.instance().addExecutor("admin_status_exe", new AdminStatusCommand());
    CommandsHandler.instance().addExecutor("admin_test_exe", new AdminTestCommand());
    CommandsHandler.instance().addExecutor("admin_upload_exe", new AdminUploadCommand());
    CommandsHandler.instance().addExecutor("admin_version_exe", new AdminVersionCommand());
    CommandsHandler.instance().addExecutor("admin_devid_exe", new DeveloperIDCommand());

    //Config
    CommandsHandler.instance().addExecutor("config_get_exe", new ConfigGetCommand());
    CommandsHandler.instance().addExecutor("config_save_exe", new ConfigSaveCommand());
    CommandsHandler.instance().addExecutor("config_set_exe", new ConfigSetCommand());
    CommandsHandler.instance().addExecutor("config_tneget_exe", new ConfigTNEGetCommand());

    //Currency
    CommandsHandler.instance().addExecutor("currency_editor_exe", new CurrencyEditorCommand());
    CommandsHandler.instance().addExecutor("currency_list_exe", new CurrencyListCommand());
    CommandsHandler.instance().addExecutor("currency_rename_exe", new CurrencyRenameCommand());
    CommandsHandler.instance().addExecutor("currency_tiers_exe", new CurrencyTiersCommand());

    //Developer
    CommandsHandler.instance().addExecutor("dev_debug_exe", new DeveloperDebugCommand());

    //Language
    CommandsHandler.instance().addExecutor("language_current_exe", new LanguageCurrentCommand());
    CommandsHandler.instance().addExecutor("language_list_exe", new LanguageListCommand());
    CommandsHandler.instance().addExecutor("language_reload_exe", new LanguageReloadCommand());
    CommandsHandler.instance().addExecutor("language_set_exe", new LanguageSetCommand());

    //Module
    CommandsHandler.instance().addExecutor("module_available_exe", new ModuleAvailableCommand());
    CommandsHandler.instance().addExecutor("module_download_exe", new ModuleDownloadCommand());
    CommandsHandler.instance().addExecutor("module_info_exe", new ModuleInfoCommand());
    CommandsHandler.instance().addExecutor("module_list_exe", new ModuleListCommand());
    CommandsHandler.instance().addExecutor("module_load_exe", new ModuleLoadCommand());
    CommandsHandler.instance().addExecutor("module_reload_exe", new ModuleReloadCommand());
    CommandsHandler.instance().addExecutor("module_unload_exe", new ModuleUnloadCommand());

    //Money
    CommandsHandler.instance().addExecutor("money_balance_exe", new MoneyBalanceCommand());
    CommandsHandler.instance().addExecutor("money_consolidate_exe", new MoneyConsolidateCommand());
    CommandsHandler.instance().addExecutor("money_convert_exe", new MoneyConvertCommand());
    CommandsHandler.instance().addExecutor("money_give_exe", new MoneyGiveCommand());
    CommandsHandler.instance().addExecutor("money_note_exe", new MoneyNoteCommand());
    CommandsHandler.instance().addExecutor("money_other_exe", new MoneyOtherCommand());
    CommandsHandler.instance().addExecutor("money_pay_exe", new MoneyPayCommand());
    CommandsHandler.instance().addExecutor("money_payfrom_exe", new MoneyPayFromCommand());
    CommandsHandler.instance().addExecutor("money_setall_exe", new MoneySetAllCommand());
    CommandsHandler.instance().addExecutor("money_set_exe", new MoneySetCommand());
    CommandsHandler.instance().addExecutor("money_take_exe", new MoneyTakeCommand());
    CommandsHandler.instance().addExecutor("money_top_exe", new MoneyTopCommand());

    //Transaction
    CommandsHandler.instance().addExecutor("transaction_away_exe", new TransactionAwayCommand());
    CommandsHandler.instance().addExecutor("transaction_history_exe", new TransactionHistoryCommand());
    CommandsHandler.instance().addExecutor("transaction_info_exe", new TransactionInfoCommand());
    CommandsHandler.instance().addExecutor("transaction_void_exe", new TransactionVoidCommand());

    //Yediot
    CommandsHandler.instance().addExecutor("yediot_idiot_exe", new YetiIdiotCommand());
  }
}