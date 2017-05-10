package me.varmetek.munchymc.listeners;

import me.varmetek.munchymc.backend.PlayerSession;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class UserTestModeChangeEvent extends Event implements Cancellable{
 private static final HandlerList  handles = new HandlerList();
 
 private PlayerSession user;
 private boolean mode;
 private boolean cancelled = false;
 
 public UserTestModeChangeEvent(PlayerSession argUser, boolean argMode){
	 user = argUser;
	 mode = argMode;
 }
 
	@Override
	public HandlerList getHandlers()
	{
		// TODO Auto-generated method stub
		return handles;
	}
	
	public PlayerSession getSession(){return user;}
	public boolean nextMode(){return mode;}

	@Override
	public boolean isCancelled()
	{
		// TODO Auto-generated method stub
		return cancelled;
	}

	@Override
	public void setCancelled(boolean cancel)
	{
		cancelled = cancel;
		
	}

}
