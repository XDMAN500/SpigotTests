package me.varmetek.core.item;

import me.varmetek.core.util.SimpleMap;
import me.varmetek.munchymc.Main;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;

/**
 * Created by XDMAN500 on 1/14/2017.
 */
public class ItemMap extends SimpleMap<CustomItem>
{

	public ItemMap(){
		super();

	}

	public CustomItem getByItem(ItemStack t)
	{

		boolean invalid = (t== null || t.getType() == Material.AIR || t.getAmount() < 1) ;

		if(invalid  ||  !t.getItemMeta().hasDisplayName() ) return null;
		for(CustomItem ci : map.values())
		{
			if (ci.getItem().isSimilar(t))
				return ci;
		}

		return  null;
	}
	public  void registerItemEvent()
	{
		Main.get().getLogger().info("[CustomItems] Loading Item events");
		Collection<CustomItem> collect = map.values();
		for(CustomItem ci :collect )
		{
			ci.registerEvent();
		}
	}


}
