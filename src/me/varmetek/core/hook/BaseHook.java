package me.varmetek.core.hook;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

/**
 * Created by XDMAN500 on 2/17/2017.
 */
public abstract class BaseHook<PLUGIN extends Plugin>
{
	protected PLUGIN plugin;
	protected boolean required;

	public BaseHook(PLUGIN plugin, boolean needed){
		Validate.notNull(plugin,"Plugin cannot be null");
		Validate.isTrue(plugin.isEnabled(),"Plugin must be enabled");
		this.plugin = plugin;
		required = needed;
	}
	public BaseHook(String name, boolean needed){
		this((PLUGIN) Bukkit.getPluginManager().getPlugin(name),needed);


	}

	public PLUGIN getPlugin(){
		return plugin;
	}



	public boolean isRequired(){
		return required;
	}
}
