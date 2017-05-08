package me.varmetek.core.item;

import me.varmetek.core.util.SimpleMapEntry;
import org.bukkit.inventory.ItemStack;

/**
 * Created by XDMAN500 on 1/14/2017.
 */
public abstract class CustomItem implements SimpleMapEntry
{
	protected final String name;
	protected final ItemStack item ;






	public CustomItem (String name){

		this.name = name;
		item = build();

	}
	public ItemStack getItem ()
	{
		return item.clone();
	}
	public String ID()
	{
		return name;
	}
	public void registerEvent(){
		//if(itemEvents == null) return;

		//ElementService.get().registerListener(itemEvents.get());
		//Main.get().getLogger().info("[CustomItems] Loading events for "+ name);

	}

	protected abstract ItemStack build();


}
