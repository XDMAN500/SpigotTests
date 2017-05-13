package me.varmetek.munchymc.backend;

import me.varmetek.core.commands.CmdCommand;
import me.varmetek.core.service.Element;
import me.varmetek.core.user.BasePlayerHandler;
import me.varmetek.munchymc.MunchyMax;
import me.varmetek.munchymc.listeners.TickListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Set;
import java.util.UUID;

/**
 * Created by XDMAN500 on 12/31/2016.
 */
public class PlayerHandler extends BasePlayerHandler<PlayerSession> implements TickListener, Element
{

	private PlayerHandler handle = this;




	public PlayerHandler ()
	{


		MunchyMax.addTickListener(this);


	}


	public void slimList(){
		Set<UUID> ids = this.registry.keySet();
		ids.forEach( (id)->{
			PlayerSession user = registry.get(id);
			if(!user.isOnline()){
				remove(user);
				MunchyMax.getInstance().getLogger().info("Cleaning user ("+ user.getName()+")");
			}
		});

	}
	@Override
	protected PlayerSession _createUser(UUID pl)
	{
		return new PlayerSession(pl,this);
	}
	@Override
	protected PlayerSession _createUser(PlayerSession pl)
	{
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
			@EventHandler(priority = EventPriority.LOW)
			public void onLogin(PlayerJoinEvent ev){

				PlayerSession user = handle.renewSession(ev.getPlayer());
				ev.getPlayer().setInvulnerable(false);
				try
				{
					MunchyMax.getDataManager().asUserData().loadUser(user);
				}catch(Exception e){
					MunchyMax.getInstance().getLogger().warning("Could not load player" + " \"" + user.getName() + "\".");
				}


			}

			@EventHandler(priority = EventPriority.LOW)
			public void onLeave0(org.bukkit.event.player.PlayerQuitEvent ev){

				PlayerSession user = handle.renewSession(ev.getPlayer());
				ev.getPlayer().setInvulnerable(false);

				try
				{
					MunchyMax.getDataManager().asUserData().saveUser(user);
				}catch(Exception e){
					MunchyMax.getInstance().getLogger().warning("Could not save player" + " \"" + user.getName() + "\".");
				}


			}

			@EventHandler(priority = EventPriority.LOW)
			public void onLeave2(org.bukkit.event.player.PlayerKickEvent ev){

				PlayerSession user = handle.renewSession(ev.getPlayer());
				ev.getPlayer().setInvulnerable(false);

				try
				{
					MunchyMax.getDataManager().asUserData().saveUser(user);
				}catch(Exception e){
					MunchyMax.getInstance().getLogger().warning("Could not save player" + " \"" + user.getName() + "\".");
				}


			}

		};
	}
}