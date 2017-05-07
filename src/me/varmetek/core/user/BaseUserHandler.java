package me.varmetek.core.user;

import me.varmetek.core.util.Cleanable;
import me.varmetek.core.util.PluginMain;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * Created by XDMAN500 on 12/31/2016.
 */
public abstract class BaseUserHandler<T extends BaseUser> implements Cleanable
{


	protected  PluginMain plugin;
	protected  Map<UUID, T> registry = new HashMap<>();

	public BaseUserHandler (PluginMain plugin){
		this.plugin = plugin;
	}

	public void refreshList(){
		registry.clear();
		Bukkit.getOnlinePlayers().forEach( (p)-> getUser(p.getUniqueId()));
	}

	public T getUser(UUID pl)
	{
		Validate.notNull(pl,"The player profile cannot be null");
		T i;

		add( i = exists(pl) ?  registry.get(pl) : _createUser(pl) );

		return  i;
	}

	public T getUser(OfflinePlayer pl)
	{
		return getUser( pl.getUniqueId());

	}

	public Optional<T> getIfExits(OfflinePlayer pl)
	{
		Validate.notNull(pl,"The player profile cannot be null");


		return exists(pl) ? Optional.ofNullable(registry.get(pl.getUniqueId())) : Optional.empty();


	}

	public T renewUser(UUID id){
		if(exists(id)){
			T e = registry.get(id);

			T newU = renew(e);
			e.clean();
			return newU;

		}else{
			return getUser(id);
		}

	}

	public T renewUser(OfflinePlayer id){
		return renewUser(id.getUniqueId());

	}

	protected T renew(T id){
		return add( _createUser(id));

	}

	public boolean exists(UUID id){
		return registry.containsKey(id);
	}


	public boolean exists(OfflinePlayer id){
		return registry.containsKey(id.getUniqueId());
	}


	protected abstract T _createUser(UUID pl);
	protected abstract T _createUser(T pl);



	public T[] getAllUsers(){
		return  (T[])registry.values().toArray();

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
	public void remove(BaseUser id){
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

	public PluginMain getPlugin(){
		return plugin;
	}


}
