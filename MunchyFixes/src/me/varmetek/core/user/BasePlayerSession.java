package me.varmetek.core.user;

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
public abstract class BasePlayerSession<D extends BasePlayerData> implements Cleanable, TickListener
{
	protected OfflinePlayer profile;
	protected  BasePlayerHandler handler;
	protected D playerData;

	protected Optional<Player> player ;



	protected BasePlayerSession (UUID profile, BasePlayerHandler  handler){
		Validate.notNull(profile,"Player profile cannot be null");
		Validate.notNull(handler,"Player handler cannot be null");

		this.handler = handler;
		playerData = _createData();
		player = Optional.ofNullable(Bukkit.getPlayer(profile));

		this.profile = player.isPresent() ?   player.get() :Bukkit.getOfflinePlayer(profile) ;




		//updateProfile(profile.getUniqueId(),true);
		//updateProfile(profile);
	}
	/****
	 *
	 *
	 * This contructor is used for renewing or refreshing a user.
	 */

	protected BasePlayerSession (BasePlayerSession<D> old){
		Validate.notNull(old.profile,"Player profile cannot be null");
		Validate.notNull(old.handler,"Player handler cannot be null");
		(this.handler = old.handler).getPlugin().getLogger().info( "Updating Profile ("+ old.profile.getName()+") "+ old.profile.getUniqueId() );

		this.playerData = old.playerData == null ? _createData() : (D)old.playerData.copy();
		player = Optional.ofNullable(Bukkit.getPlayer(old.getUUID()));

		profile = player.isPresent() ?   player.get() :Bukkit.getOfflinePlayer(old.getUUID());

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

	public D getPlayerData(){
		return playerData;
	}

	public void setPlayerData(D data){
		playerData = data;
	}




	public Optional<Player> getPlayer(){

		return player;

	}

	protected abstract D _createData();










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

	}




}
