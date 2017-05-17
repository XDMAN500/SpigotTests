package me.varmetek.core.user;

import me.varmetek.core.util.Cleanable;
import me.varmetek.core.util.PluginCore;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.*;

/**
 * Created by XDMAN500 on 12/31/2016.
 */
public abstract class BasePlayerHandler<T extends BasePlayerSession<? extends BasePlayerData>> implements Cleanable
{


	protected PluginCore plugin;
	protected  Map<UUID, T> registry = new HashMap<>();

	public BasePlayerHandler (PluginCore plugin ){
		this.plugin = plugin;

	}

	public void refreshList(){
		registry.clear();
		Bukkit.getOnlinePlayers().forEach( (p)-> getSession(p.getUniqueId()));
	}

	public T getSession(UUID pl)
	{
		Validate.notNull(pl,"The player profile cannot be null");
		T i =  registry.get(pl);
		if(i == null){
			i = add(_createUser(pl));
		}


		return  i;
	}

	public T getSession(OfflinePlayer pl)
	{
		return getSession( pl.getUniqueId());

	}

	public Optional<T> getIfExits(OfflinePlayer pl)
	{
		Validate.notNull(pl,"The player profile cannot be null");


		return exists(pl) ? Optional.ofNullable(registry.get(pl.getUniqueId())) : Optional.empty();


	}

	public T renewSession(UUID id){
		Validate.notNull(id,"The player profile cannot be null");;
		if(exists(id)){
			T e = registry.get(id);

			T newU = renew(e);
			e.clean();
			return newU;

		}else{
			return getSession(id);
		}

	}

	public T renewSession(OfflinePlayer id){
		Validate.notNull(id,"The player profile cannot be null");;
		return renewSession(id.getUniqueId());

	}

	protected T renew(T id){
		Validate.notNull(id);
		return add( _renewUser(id));

	}

	public boolean exists(UUID id){
		Validate.notNull(id,"The player profile cannot be null");;
		return registry.containsKey(id);
	}


	public boolean exists(OfflinePlayer id){
		Validate.notNull(id,"The player profile cannot be null");
		return registry.containsKey(id.getUniqueId());
	}


	protected abstract T _createUser(UUID pl);
	protected abstract T _renewUser(T pl);



	public Collection<T> getAllUsers(){
		return  registry.values();

	}

	public void remove(UUID id){
		T in;
		if(!registry.containsKey(id) || null == (in =registry.get(id)) )return;
		in.clean();
		registry.remove(id);
	}
	public void remove(OfflinePlayer id){
		remove(id.getUniqueId());
	}
	public void remove(BasePlayerSession id){
		remove(id.getUUID());
	}
	public T add(T bu){
		registry.put(bu.getUUID(),bu );
		return bu;
	}
	public void clean(){
		registry.values().forEach( (E)->E.clean());
		registry.clear();
		registry = null;
		plugin = null;


	}

	public PluginCore  getPlugin(){
	  return  plugin;
  }




}
