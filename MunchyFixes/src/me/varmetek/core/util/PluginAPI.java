package me.varmetek.core.util;

/**
 * Created by XDMAN500 on 1/23/2017.
 */


public abstract class PluginAPI implements Cleanable
{
	protected PluginCore plugin;
	protected PluginAPI(PluginCore plugin){
		this.plugin = plugin;
	}
	public abstract void onLoad();


	public abstract void onEnable();

	public abstract void onDisable();





	protected abstract void registerElements ();




}
