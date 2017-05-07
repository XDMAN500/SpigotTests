package me.varmetek.munchymc.backend.kit;

import me.varmetek.core.util.SimpleMap;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;

/**
 * Created by XDMAN500 on 1/15/2017.
 */
public class KitHandler extends SimpleMap<Kit>
{

	private static Kit  EMPTY = new Kit(null,new HashMap<>());
	public KitHandler(){


	}

	public void applyEmpty(Player pl){
		EMPTY.apply(pl);
	}
	public void unregister(String k){
		try
		{

			if (isRegistered(k))
			{
				//
				super.unregister(k);

			}
		}catch(Exception e){
			Bukkit.getLogger().warning("Failed to remove kit "+ k +" "+e.getMessage());
		}

	}




}
