package me.varmetek.core.util;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Created by XDMAN500 on 1/15/2017.
 */
public abstract class SimpleMap<T extends SimpleMapEntry> implements Cleanable
{
//	protected PluginBase plugin;
	protected final Map<String,T> map;

	public SimpleMap(){

		map = new LinkedHashMap<>();


	}
	public  SimpleMap<T>  register(T ci)
	{
		if(map.containsKey( ci.ID())){
			T cac = map.get(ci.ID());
			boolean invalid = cac != null;
			if(invalid){
				//Bukkit.getLogger().info("Failed Registering Item "+ci.ID());
				throw new IllegalStateException("That registry key is already full");
			}else{
				map.put(ci.ID(),ci);

			}
		}else{
			map.put(ci.ID(),ci);
		}
		return this;
		//Bukkit.getLogger().info("Registering Item "+ci.ID());
	}
	public  void registerAll(T... ci){
		for(T item: ci){
			try{
				register(item);
			}catch(Exception e){

				e.printStackTrace();

			}
		}
	}
	public Optional<T> get(String key)
	{
		return Optional.ofNullable(map.get(key));
	}
	public boolean isRegistered(String key){
		return map.containsKey(key);
	}
	public boolean isRegistered(T val){
		return map.containsValue(val);
	}
	public Collection<T> values(){
		return map.values();
	}

	public void unregister(String k){
		map.remove(k);
	}
	public void unregister(T k){
		map.remove(k.ID());
	}
	public void unregisterAll(){
		map.clear();
	}

	@Override
	public void clean ()
	{
		unregisterAll();

		//map = null;
	}
}
