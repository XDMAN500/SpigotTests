package me.varmetek.munchymc.backend;

import me.varmetek.core.commands.CmdCommand;
import me.varmetek.core.service.Element;
import me.varmetek.munchymc.Main;
import me.varmetek.munchymc.backend.test.CustomItemRare;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public  abstract class RareItemListener implements Element{


	protected final CustomItemRare item;
	protected final Main plugin;
	protected RareItemListener (CustomItemRare ci, Main plugin)
	{
		item = ci;
		this.plugin = plugin;
	}



	public  ItemStack getItem(){ return item.getItem();}
	public String getID(){ return item.ID();}
	public boolean isEquiped(Player pl, EquipmentSlot slot)
	{
		return Rares.checkItem(getSlot(pl,slot), item , plugin.getUserHandler().getUser(pl).isTesting());
	}

	public ItemStack getSlot(Player pl, EquipmentSlot slot)
	{
		switch(slot)
		{
			case HAND: return pl.getInventory().getItemInMainHand();
			case OFF_HAND: return pl.getInventory().getItemInOffHand();
			case HEAD: return pl.getInventory().getHelmet();
			case CHEST: return pl.getInventory().getChestplate();
			case LEGS: return pl.getInventory().getLeggings();
			case FEET: return pl.getInventory().getBoots();
		}
		return null;
	}


	public  abstract boolean check(Player pl);

	@Override
  public CmdCommand[] supplyCmd(){
	  return null;
  }
}

