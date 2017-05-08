package me.varmetek.munchymc.backend;

import me.varmetek.core.commands.CmdCommand;
import me.varmetek.core.service.Element;
import me.varmetek.core.user.BaseUserHandler;
import me.varmetek.core.util.PluginMain;
import me.varmetek.munchymc.Main;
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
public class UserHandler extends BaseUserHandler<User> implements TickListener, Element
{

	private Main main;
	private Listener listen = new Listener(){
		@EventHandler(priority = EventPriority.LOW)
		public void onLogin(PlayerJoinEvent ev){

			User user = main.getUserHandler().renewUser(ev.getPlayer());
			ev.getPlayer().setInvulnerable(false);
			try
			{
				main.getDataManager().asUserData().loadUser(user);
			}catch(Exception e){
				plugin.getLogger().warning("Could not load player "+ user.getName());
			}


		}

		@EventHandler(priority = EventPriority.LOW)
		public void onLeave0(org.bukkit.event.player.PlayerQuitEvent ev){

			User user = main.getUserHandler().renewUser(ev.getPlayer());
			ev.getPlayer().setInvulnerable(false);

			try
			{
				main.getDataManager().asUserData().saveUser(user);
			}catch(Exception e){
				plugin.getLogger().warning("Could not save player "+ user.getName());
			}


		}

		@EventHandler(priority = EventPriority.LOW)
		public void onLeave2(org.bukkit.event.player.PlayerKickEvent ev){

			User user = main.getUserHandler().renewUser(ev.getPlayer());
			ev.getPlayer().setInvulnerable(false);

			try
			{
				main.getDataManager().asUserData().saveUser(user);
			}catch(Exception e){
				plugin.getLogger().warning("Could not save player "+ user.getName());
			};


		}

	};



	public UserHandler (PluginMain plugin)
	{
		super(plugin);
		main = (Main)plugin;
		main.addTickListener(this);


	}


	public void slimList(){
		Set<UUID> ids = this.registry.keySet();
		ids.forEach( (id)->{
			User user = registry.get(id);
			if(!user.isOnline()){
				remove(user);
				main.getLogger().info("Cleaning user ("+ user.getName()+")");
			}
		});

	}
	@Override
	protected User _createUser(UUID pl)
	{
		return new User(pl,this);
	}
	@Override
	protected User _createUser(User pl)
	{
		return new User(pl,this);
	}


	@Override
	public void tick ()
	{
		registry.values().forEach(User::tick);
	}


	@Override
	public CmdCommand[] supplyCmd (){
		return null;
	}

	@Override
	public Listener supplyListener (){
		return listen;
	}
}