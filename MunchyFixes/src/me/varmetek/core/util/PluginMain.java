package me.varmetek.core.util;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by XDMAN500 on 1/23/2017.
 */


public abstract class PluginMain extends JavaPlugin
{
	@Override
	public abstract void onLoad();

	@Override
	public abstract void onDisable();

	@Override
	public abstract void onEnable();


	protected abstract void registerElements ();




}
