package me.varmetek.core.scoreboard;

import me.varmetek.core.util.Cleanable;
import me.varmetek.core.util.PluginCore;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

/**
 * Created by XDMAN500 on 2/11/2017.
 */
public class Scoreboardx implements Cleanable
{
	private Scoreboard scoreboard;
	private SidebarHandler sidebarHandler;

	protected PluginCore plugin;

	public Scoreboardx(PluginCore plugin){
		Validate.notNull(plugin);
		this.plugin = plugin;
		scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
		sidebarHandler = new SidebarHandler(this);

	}

	public SidebarHandler getSidebarHandler(){
		return sidebarHandler;
	}
	public Scoreboard getScoreboard(){
		return scoreboard;
	}

	public void apply(Player pl){
		pl.setScoreboard(scoreboard);
	}

	public PluginCore getPlugin(){
		return plugin;
	}
	@Override
	public void clean ()
	{
		sidebarHandler.clean();
		sidebarHandler = null;
		plugin = null;
	}
}
