package me.varmetek.munchymc.backend.user;

import me.varmetek.core.commands.CmdCommand;
import me.varmetek.core.service.Element;
import me.varmetek.core.user.BasePlayerHandler;
import me.varmetek.core.util.PluginCore;
import me.varmetek.munchymc.MunchyMax;
import me.varmetek.munchymc.backend.exceptions.ConfigException;
import me.varmetek.munchymc.listeners.TickListener;
import me.varmetek.munchymc.util.Utils;
import org.apache.commons.lang.Validate;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Set;
import java.util.UUID;

/**
 * Created by XDMAN500 on 12/31/2016.
 */
public class PlayerHandler extends BasePlayerHandler<PlayerSession> implements TickListener, Element
{
	private static final long FLUSH_PERIOD = 20*60*10;//10 minutes
	private static final long FLUSH_GRACE = 60*2;//2 minutes

	private PlayerHandler handle = this;
	private final FlushTask flushTask;





	public PlayerHandler (PluginCore plugin)
	{
		super(plugin);
		MunchyMax.addTickListener(this);
		flushTask = new FlushTask();




	}

	public void runTask(){
    flushTask.runTaskTimer(plugin,FLUSH_PERIOD,FLUSH_PERIOD);
  }

	public void slimList(){
		Set<UUID> ids = this.registry.keySet();
		ids.forEach( (id)->{
			PlayerSession user = registry.get(id);
			if(!user.isOnline()){
				plugin.getLogger().info("Flushing user ("+ user.getName()+")");
				remove(user);

			}
		});

	}
	@Override
	protected PlayerSession _createUser(UUID pl)
	{
		Validate.notNull( pl, "Id cannot be null");
		return new PlayerSession(pl,this);
	}
	@Override
	protected PlayerSession _renewUser(PlayerSession pl)
	{
		Validate.notNull( pl, "Session cannot be null");
		return new PlayerSession(pl);
	}


	@Override
	public void tick ()
	{
		registry.values().forEach(PlayerSession::tick);
	}


	@Override
	public CmdCommand[] supplyCmd (){
		return null;
	}

	@Override
	public Listener supplyListener (){
		return new Listener(){
			@EventHandler(priority = EventPriority.HIGHEST)
			public void onLogin(PlayerJoinEvent ev){

				PlayerSession user = handle.renewSession(ev.getPlayer());
				ev.getPlayer().setInvulnerable(false);
				try
				{
					MunchyMax.getPlayerFileManager().loadUser(user);
				}catch(ConfigException e){
					MunchyMax.getInstance().getLogger().warning("Could not load player" + " \"" + user.getName() + "\".");
				}


			}

			@EventHandler(priority = EventPriority.HIGHEST)
			public void onLeave0(org.bukkit.event.player.PlayerQuitEvent ev){

				PlayerSession user = handle.renewSession(ev.getPlayer());
				ev.getPlayer().setInvulnerable(false);

				try
				{
					MunchyMax.getPlayerFileManager().saveUser(user);
				}catch(ConfigException e){
					MunchyMax.getInstance().getLogger().warning("Could not save player" + " \"" + user.getName() + "\".");
				}


			}

			@EventHandler(priority = EventPriority.HIGHEST)
			public void onLeave2(org.bukkit.event.player.PlayerKickEvent ev){

				PlayerSession user = handle.renewSession(ev.getPlayer());
				ev.getPlayer().setInvulnerable(false);

				try
				{
					MunchyMax.getPlayerFileManager().saveUser(user);
				}catch(ConfigException e){
					MunchyMax.getInstance().getLogger().warning("Could not save player" + " \"" + user.getName() + "\".");
				}


			}

		};
	}

	@Override
	public void clean(){
		super.clean();
		if(flushTask.getTaskId() != -1)
		  flushTask.cancel();
	}

	private class FlushTask extends BukkitRunnable{
		long lastrun;

		public void run(){
			lastrun = Utils.getSystemSeconds();

			Set<UUID> ids = registry.keySet();

			registry.values().forEach(
				(user)->{

					if(!user.isOnline() && (lastrun -user.timeCreated) > FLUSH_GRACE){

						plugin.getLogger().info("Flushing user ("+ user.getName()+")");
						remove(user);

					}
			});
		}

	}
}