package me.varmetek.munchymc.rares;

import me.varmetek.core.util.Messenger;
import me.varmetek.munchymc.Main;
import me.varmetek.munchymc.backend.RareItemListener;
import me.varmetek.munchymc.backend.test.CustomItemRare;
import me.varmetek.munchymc.util.UtilInventory;
import me.varmetek.munchymc.util.Utils;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.block.DoubleChest;
import org.bukkit.block.Furnace;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.Repairable;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

public class RareOptimizer extends RareItemListener
{

	private static final  String KEY  = "Optimized";
	private static final List<Material> optimizable = Arrays.asList(Material.HOPPER,Material.BURNING_FURNACE);
	public RareOptimizer(CustomItemRare rare, Main plugin){

		super(rare,plugin);
	}

	//public final Map<BlockVector, Long> enchantedFurnaces  = new HashMap<>();

	@Override
	public boolean check(Player pl)
	{

		return isEquiped(pl, EquipmentSlot.HAND) ||isEquiped(pl, EquipmentSlot.OFF_HAND) ;
	}


	@EventHandler
	public void optimizeTouch(PlayerInteractEvent ev)
	{
		if(!ev.hasBlock())return;

		Player pl = ev.getPlayer();
		BlockState ih = ev.getClickedBlock().getState();


		if(!check(pl))return;

		if(ih == null)return;

		if(ev.getPlayer().isSneaking())
		{
			if(isOptimized(ih))
			{
				ih.removeMetadata(KEY, plugin);
				Messenger.send(pl, " &7This container has been Unoptimized");
				ev.setCancelled(true);
				return;
			}
		}else{
			if (optimize(ih))
			{
				Messenger.send(pl, "&7 This container has been Optimized.");
				ev.setCancelled(true);
			}
		}

	}


	@EventHandler
	public void smelt(FurnaceSmeltEvent ev)
	{
		BlockState bs = ev.getBlock().getState();
		if(isOptimized(bs))
		{
			optimize(bs);
		}
	}
	@EventHandler
	public void burnFuel(FurnaceBurnEvent ev){

		Furnace furn = (Furnace)ev.getBlock().getState();
		if(furn.hasMetadata(KEY) && furn.getInventory().getViewers().isEmpty())
		{
			furn.removeMetadata(KEY, plugin);
		}

	}

	@EventHandler
	public void unOptimizeTouch(BlockDamageEvent ev)
	{
		Player pl = ev.getPlayer();
		if(!check(pl))return;
		ev.setCancelled(true);
		ev.setInstaBreak(false);


	}

	@EventHandler
	public void blockBreak(BlockBreakEvent ev)
	{
		BlockState bs = ev.getBlock().getState();


			bs.removeMetadata(KEY,plugin);
	}

	@EventHandler
	public void hopperTransfer(InventoryMoveItemEvent ev)
	{
		BlockState bs = getState(ev.getInitiator().getHolder());
		BlockState dest = getState(ev.getDestination().getHolder());
		if(dest instanceof  Furnace)
		{
			if(isOptimized(dest))
				optimize(dest);
		}
		if(dest.hasMetadata("HopTime"))
		{
			((BukkitRunnable) dest.getMetadata("HopTime").get(0).value()).cancel();
			dest.removeMetadata("HopTime", plugin);
		}
		//Messenger.sendAll(ev.getEventName());
		if(isOptimized(bs))
		{

			//Messenger.sendAll(ev.getEventName() +" - is Optimized");
			boolean isSource = ev.getSource().equals(ev.getInitiator());
			dest.setMetadata("HopTime", new FixedMetadataValue(plugin,
			new BukkitRunnable(){
				public void run(){
					if(!optimizeHopper(ev.getSource(),ev.getDestination(),bs))
						this.cancel();

				}
			}));
			(
					(BukkitRunnable)dest.getMetadata("HopTime").get(0).value()).runTaskTimer(plugin,0,1L);

		}
	}
	private static Material getContainerType(BlockState ih)
	{
		if(optimizable.contains(ih.getType()))
			return ih.getType();
		else
			return Material.AIR;


	}

	private static ItemStack getSourceItem(Inventory inv)
	{
			if(inv instanceof FurnaceInventory)
				return ((FurnaceInventory)inv).getResult();
		ListIterator<ItemStack> it =  inv.iterator();

		while(it.hasNext() )
		{
			ItemStack temp = it.next();
			if(temp == null)continue;
			if(temp.getType() == Material.AIR)continue;

			return temp;

		}
		return null;
	}
	private boolean getDestAvaliable(Inventory inv)
	{
		if(inv instanceof FurnaceInventory)
			return ((FurnaceInventory)inv).getSmelting()  == null;
		return inv.firstEmpty() == -1;
	}
	private boolean destAddItem(Inventory inv, ItemStack item)
	{
		if(inv instanceof FurnaceInventory)
		{
			if (getDestAvaliable(inv)){
				((FurnaceInventory)inv).setSmelting(item);
				return true;
			}
		}else{
			inv.addItem(item);
			return true;
		}
		return false;

	}
	private boolean optimizeHopper(Inventory source, Inventory destination, BlockState bs)
	{

		boolean isDestFull = getDestAvaliable(destination);
		ItemStack item = getSourceItem(source);
		boolean isSrcEmpty =  item == null;

		//if(item == null)return false;
		if (isDestFull)
		{
		//	Messenger.sendAll("Hopper" + " Destination is Full");
			//bs.removeMetadata(KEY, Main.plugin);
			return true;
		}

		if (isSrcEmpty)
		{
		//	Messenger.sendAll("Hopper" + " Source is Empty");
			bs.removeMetadata(KEY, plugin);
			return false;
		}
		//Messenger.sendAll("hopper" + " - " +source.getName());
		///Messenger.sendAll(ev.getEventName() + " - " + (ev.getSource().firstEmpty() + 1));
		//ItemStack item = ev.getSource().getItem(ev.getSource().firstEmpty() + 1);
		//Messenger.sendAll("Hopper" + " - " + item);
		if(destAddItem(destination,item))
		source.removeItem(item);

		return true;





	}
	public  boolean isOptimized(BlockState ih)
	{
		return ih != null && (getContainerType(ih) != Material.AIR && ih.hasMetadata(KEY));
	}

	public   boolean optimize(BlockState ih)
	{
		Material ct = getContainerType(ih);
		switch(ct)
		{

			case HOPPER:

				break;
			case BURNING_FURNACE:
				((Furnace)ih).setCookTime((short)1000);
				break;
			case ANVIL:
			//	((BrewingStand)ih).setBrewingTime(1);

				break;
			case AIR:

				return false;

		}
		ih.setMetadata(KEY, new FixedMetadataValue(plugin,true));
		return true;
	}
	private BlockState getState(InventoryHolder ih)
	{
		if(ih instanceof DoubleChest)
		{
			DoubleChest dc = (DoubleChest)ih;

			return (BlockState) (dc.getLeftSide().getInventory().firstEmpty() == -1?  dc.getRightSide(): dc.getLeftSide());

		}
		return (BlockState)ih;

	}

	private static final List<EnchantmentTarget> whiteList = Arrays.asList(
			new EnchantmentTarget[] { EnchantmentTarget.ALL, EnchantmentTarget.TOOL});

	@EventHandler
	public void openEnchant(InventoryOpenEvent ev)
	{
		Inventory inv = ev.getInventory();
		if(inv == null)return;
		if(inv.getType() != InventoryType.ANVIL)return;
		if(!check((Player) ev.getPlayer()))return;

		Messenger.send(ev.getPlayer(), "&7 Optimized Repairing Enabled.");
	}
	@EventHandler
	public void optmizedEnchant(PrepareAnvilEvent ev)
	{

		AnvilInventory anvil= ev.getInventory();
		if(anvil == null)return;


		Player pl = (Player)ev.getViewers().get(0);
		if(!(check(pl)) ) return;
		//Messenger.send(pl, "&7 Optimized Repairing Enabled.");
		//ItemStack item1 = anvil.getItem(0);
		//ItemStack item2 = anvil.getItem(1);
		/*if(item1  == null | item2 == null)return;
		if(item1.getType() == Material.AIR || item1.getType() == Material.AIR)return;
		if(item1.getType() != item2.getType())return;




		if(item1.getEnchantments().isEmpty())return;

		ItemStack result  = item1.clone();


		for(Enchantment ench :item2.getEnchantments().keySet() )
		{
			int lvl = 	item2.getEnchantmentLevel(ench);

			int maxLvl = whiteList.contains(ench.getItemTarget()) ? 256 : ench.getMaxLevel();

			boolean isSameLevel = result.getEnchantmentLevel(ench) == lvl ;


			result.addUnsafeEnchantment(ench,
					result.containsEnchantment(ench)?
							( isSameLevel ? Math.min(lvl+1,maxLvl) :
									  Math.max(result.getEnchantmentLevel(ench) ,lvl) )
							: Math.min(lvl,maxLvl));

		}*/


		ItemStack result = UtilInventory.combineEnchantments(anvil.getItem(0),anvil.getItem(1),true,false);
		if(result == null)return;
		String name = anvil.getRenameText();
		ItemMeta im = result.getItemMeta();
		ev.getInventory().setRepairCost(1);
		if(im instanceof Repairable) ((Repairable)im).setRepairCost(1);
		im.setDisplayName(Utils.colorCode(name));

		result.setItemMeta(im);

		result.setDurability((short)0);


		ev.setResult(result);

	}

}
