package me.varmetek.munchymc.listeners;

import me.varmetek.core.commands.CmdCommand;
import me.varmetek.core.service.Element;
import me.varmetek.munchymc.Main;
import me.varmetek.munchymc.util.UtilInventory;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.EnchantingInventory;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

/**
 * Created by XDMAN500 on 12/30/2016.
 */
public class EnchantmentListener implements Element
{

  private static final boolean replaceEnchants = true;

	private final Main plugin;
	private final Listener listen;

	public EnchantmentListener(Main plugin)
	{
		this.plugin = plugin;

		listen = new Listener()
    {
      /***
       * runs {@code undoSwap} when a click is registered in the {@code EnchantingInventory}.
       * <p>
       * Is overridden by
       * {@code PrepareEnchantEvent} if the inventory has the right conditions to run an {@code EnchantItemEvent}
       * */
      @EventHandler
      public void updateEnchant(InventoryClickEvent ev)
      {

        if(ev.getClickedInventory() == null)return;
        if(ev.getClickedInventory() .getType() != InventoryType.ENCHANTING)return;
        EnchantingInventory inv = (EnchantingInventory)ev.getClickedInventory() ;
        undoSwap(inv);
        //Messenger.send(ev.getWhoClicked(), ev.getEventName() +" Enchant Refreshed(1)");
      }

      /***
       *
       *  Gives the enchantments back to the item when the enchanting Inventory is closed
       */
      @EventHandler
      public void closeEnchant(InventoryCloseEvent ev)
      {
        if(ev.getInventory().getType() != InventoryType.ENCHANTING)return;
        EnchantingInventory inv = (EnchantingInventory)ev.getInventory();
        undoSwap(inv);
        //Messenger.send(ev.getPlayer(), ev.getEventName() +" Enchant Aborted(3)");
      }


      /***
       *
       *  Gives the Enchantments to the lapis and combines prospected enchantments with the current enchantments
       *  if replaceEnchants is false;
       */
      @EventHandler
      public void commitEnchant(EnchantItemEvent ev)
      {
        EnchantingInventory inv = (EnchantingInventory)ev.getInventory();
        ItemStack lapis = inv.getSecondary();
        boolean invalid = (lapis == null || lapis.getType()  != Material.INK_SACK || lapis.getEnchantments().isEmpty() );
        if(invalid)return;
        //	Messenger.send(ev.getEnchanter(), ev.getEventName() +" Enchant Commited(3)");
        Map<Enchantment,Integer> map = lapis.getEnchantments();


        if(replaceEnchants)
        {
          for(Enchantment e : map.keySet()){// Remove all Enchantments Before item is overriden
            lapis.removeEnchantment(e);
          }
        }

        plugin.getTaskHandler().runTask(() ->
        {
          doSwap(inv);
        },1l);

      }
      @EventHandler
      public void enchant(PrepareItemEnchantEvent ev){

        EnchantingInventory inv = (EnchantingInventory)ev.getInventory();
        ItemStack item =  inv.getItem();
        ItemStack  lapis =inv.getSecondary();

        boolean invalid = (lapis == null || item == null ||
                             lapis.getType()  != Material.INK_SACK || item.getType() == Material.AIR );
        if(invalid ||  item.getEnchantments().isEmpty())return;


        doSwap(inv);


        plugin.getTaskHandler().runTask(() ->
        {

          //inv.setItem(0,item);
          ev.getEnchanter().updateInventory();;
        },1l);

      }
    };
	}

	/***
	 *
	 *  Transfers the enchants from the lapis to the item;
	 *
	 */

	private  boolean undoSwap(EnchantingInventory inv)
	{
		ItemStack item =  inv.getItem();
		ItemStack  lapis =inv.getSecondary();

		boolean invalid = (lapis == null || item == null ||  lapis.getType()  != Material.INK_SACK || item.getType() == Material.AIR );
		if(invalid || lapis.getEnchantments().isEmpty())return false;

		item.addUnsafeEnchantments(lapis.getEnchantments());


		for(Enchantment e : item.getEnchantments().keySet())// Remove all Enchantments
			lapis.removeEnchantment(e);
		return true;




	}
	/**
	 *
	 *
	 * Transers enchantments from the item to the lapis
	 * @return returns the successfulness of the swap
	 * @param inv The inventory to do the swapping
	 *
	 */

	private  boolean doSwap(EnchantingInventory inv)
	{
		ItemStack item =  inv.getItem();
		ItemStack  lapis =inv.getSecondary();

		boolean invalid = (lapis == null || item == null ||  lapis.getType()  != Material.INK_SACK || item.getType() == Material.AIR );
		if(invalid || item.getEnchantments().isEmpty())return false;

		if(replaceEnchants)
		{
			lapis.addUnsafeEnchantments(item.getEnchantments());
		}else{
			UtilInventory.combineEnchantments(lapis,item.getEnchantments(),false);
		}



		for(Enchantment e : lapis.getEnchantments().keySet())// Remove all Enchantments
			item.removeEnchantment(e);
		return true;




	}

	@Override
	public void clean (){

	}

	@Override
	public CmdCommand[] supplyCmd (){
		return null;
	}

	@Override
	public Listener supplyListener (){
		return listen;
	}
}
