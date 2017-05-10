package me.varmetek.core.util;

import me.varmetek.core.item.ItemMap;
import me.varmetek.core.service.ElementManager;
import me.varmetek.core.user.BasePlayerData;
import me.varmetek.core.user.BasePlayerHandler;
import me.varmetek.core.user.BasePlayerSession;
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

	public abstract <T extends BasePlayerSession<D>, D extends BasePlayerData> BasePlayerHandler<T,D> getPlayerHandler ();


	public abstract ElementManager getElementManager ();
	public abstract ItemMap getItemMap ();

	public abstract TaskHandler getTaskHandler ();

	protected abstract void registerElements ();




}
