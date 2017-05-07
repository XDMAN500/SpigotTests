package me.varmetek.munchymc.rares;

import me.varmetek.munchymc.Main;
import me.varmetek.munchymc.backend.RareItemListener;
import me.varmetek.munchymc.backend.test.CustomItemRare;
import me.varmetek.munchymc.util.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

public class RareSuperChest extends RareItemListener implements Listener
{

	public RareSuperChest(CustomItemRare rare, Main plugin){

		super(rare,plugin);
	}

	@Override
	public boolean check(Player pl)
	{

		return isEquiped(pl, EquipmentSlot.HAND) || isEquiped(pl, EquipmentSlot.OFF_HAND);
	}

	

	@Override
	public void clean (){

	}

	@Override
	public Listener supplyListener (){
		return new Listener()
		{
			@EventHandler
			public void OpenChestEvent(PlayerInteractEvent ev){
				Player pl = (Player)ev.getPlayer();
				if(!check(pl))return;
				if(Utils.isRightClicked(ev.getAction()))
					pl.openInventory(pl.getEnderChest());
				ev.setCancelled(true);
			}
			@EventHandler
			public void OpenChestEvent(BlockPlaceEvent ev){
				Player pl = (Player)ev.getPlayer();
				if(!check(pl))return;
				ev.setCancelled(true);
			}

		};
	}
}
