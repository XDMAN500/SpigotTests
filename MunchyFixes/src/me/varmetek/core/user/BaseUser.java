package me.varmetek.core.user;

import me.varmetek.core.scoreboard.Scoreboardx;
import me.varmetek.core.util.Cleanable;
import me.varmetek.munchymc.listeners.TickListener;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.UUID;

/**
 * Created by XDMAN500 on 12/31/2016.
 */
public abstract class BaseUser implements Cleanable, TickListener
{
	protected OfflinePlayer profile;
	protected  BaseUserHandler handler;

	protected Optional<Player> player ;
	protected Optional<Scoreboardx> board ;


	protected BaseUser (UUID profile,  BaseUserHandler  handler){
		Validate.notNull(profile,"Player profile cannot be null");
		Validate.notNull(handler,"Player profile cannot be null");

		this.handler = handler;

		player = Optional.ofNullable(Bukkit.getPlayer(profile));

		this.profile = player.isPresent() ?   player.get() :Bukkit.getOfflinePlayer(profile) ;


		board = Optional.empty();

		if(player.isPresent())
		{
			handler.getPlugin().getLogger().info( "Creating Profile ("+ this.profile.getName()+") "+  this.profile.getUniqueId() );
			board = Optional.of(new Scoreboardx());
			board.get().apply(player.get());

		}

		//updateProfile(profile.getUniqueId(),true);
		//updateProfile(profile);
	}
	/****
	 *
	 *
	 * This contructor is used for renewing or refreshing a user.
	 */

	protected BaseUser(BaseUser old, BaseUserHandler  handler){
		Validate.notNull(old,"Player profile cannot be null");
		(this.handler = handler).getPlugin().getLogger().info( "Updating Profile ("+ old.profile.getName()+") "+  old.profile.getUniqueId() );

		board = old.board;
		player = Optional.ofNullable(Bukkit.getPlayer(old.getUUID()));

		profile = player.isPresent() ?   player.get() :Bukkit.getOfflinePlayer(old.getUUID());

		if(player.isPresent())
		{
			board.get().apply(player.get());
		}
	}





	/*
	*  Instance
	* */
	public String getName()
	{
		return profile.getName();
	}

	public OfflinePlayer getProfile()
	{
		return profile;
	}

	public UUID getUUID()
	{
		return profile.getUniqueId();
	}



	public boolean isOnline(){

		return player.isPresent();
	}





	public Optional<Player> getPlayer(){

		return player;

	}





	public Optional<Scoreboardx> getScoreBoard(){
		return board;

	}





	/***
	 *
	 *
	 * Should be used with super.clean();
	 * */
	public void clean(){
		//handler.remove(this);
		handler = null;
		profile = null;
		player = null;
		board = null;
	}




}
