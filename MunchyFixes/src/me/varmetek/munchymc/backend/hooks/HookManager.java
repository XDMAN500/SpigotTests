package me.varmetek.munchymc.backend.hooks;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import me.varmetek.munchymc.Main;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicesManager;
import ru.tehkode.permissions.bukkit.PermissionsEx;

/**
 * Created by XDMAN500 on 4/22/2017.
 */
public class HookManager
{
	public Main main;

	public Permission permission = null;
	public Economy economy = null;
	public Chat chat = null;

	public HookPerm  permHook = null;
	public HookWorldEdit weHook = null;


	public HookManager(Main main){
		this.main = main;
	}

	public void load(){

		ServicesManager sm = Bukkit.getServicesManager();

		weHook = new HookWorldEdit((WorldEditPlugin) Bukkit.getPluginManager().getPlugin("PermissionsEx"),true);
		permHook = new HookPerm((PermissionsEx)Bukkit.getPluginManager().getPlugin("PermissionsEx"),false);

			RegisteredServiceProvider<Permission> permissionProvider = sm.getRegistration(net.milkbowl.vault.permission.Permission.class);

			if (permissionProvider != null) {
			permission = permissionProvider.getProvider();
		}

		RegisteredServiceProvider<Chat> chatProvider = sm.getRegistration(net.milkbowl.vault.chat.Chat.class);
		if (chatProvider != null) {
			chat = chatProvider.getProvider();
		}

		RegisteredServiceProvider<Economy> economyProvider = sm.getRegistration(net.milkbowl.vault.economy.Economy.class);
		if (economyProvider != null) {
			economy = economyProvider.getProvider();
		}




	}



}
