package me.varmetek.core.item;

import me.varmetek.core.util.PluginCore;
import me.varmetek.core.util.SimpleMap;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;

/**
 * Created by XDMAN500 on 1/14/2017.
 */
public class ItemMap extends SimpleMap<CustomItem>
{
	protected PluginCore plugin;
	public ItemMap(PluginCore plugin){
		super();
		this.plugin = plugin;

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
		plugin.getLogger().info("[CustomItems] Loading Item events");
		Collection<CustomItem> collect = map.values();
		for(CustomItem ci :collect )
		{
			ci.registerEvent();
		}
	}

	@Override
	public void clean(){
		super.clean();
		plugin = null;
	}

}
