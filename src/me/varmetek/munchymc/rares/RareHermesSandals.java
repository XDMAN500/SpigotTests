package me.varmetek.munchymc.rares;

import ca.thederpygolems.armorequip.ArmorEquipEvent;
import me.varmetek.munchymc.Main;
import me.varmetek.munchymc.backend.RareItemListener;
import me.varmetek.munchymc.backend.test.CustomItemRare;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerToggleSprintEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class RareHermesSandals extends RareItemListener{

	public  RareHermesSandals (CustomItemRare rare, Main plugin){

		super(rare,plugin);
	}


	@Override
	public boolean check(Player pl)
	{

		return isEquiped(pl, EquipmentSlot.CHEST);
	}



/*	@EventHandler
	public void onEquip(InventoryClickEvent ev){
		Player pl = (Player)ev.getWhoClicked();
		Inventory inv = ev.getClickedInventory();
		if(inv == null)return;
		if(!inv.equals(pl.getInventory()))return;
	
		if(!(ev.getSlotType() ==  InventoryType.SlotType.ARMOR))return;
		new BukkitRunnable(){
			public void run(){
		if(check(pl.getUniqueId())){
			
			pl.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,Integer.MAX_VALUE, 1, true,false));
		}else{
			pl.removePotionEffect(PotionEffectType.SPEED);
		}
			}
		}.runTask(Main.plugin);
	}
	@EventHandler
	public void onClick(PlayerInteractEvent ev){
		Player pl = (Player)ev.getPlayer();
		if(pl.getItemInHand()== null)return;
		if(!pl.getItemInHand().equals(getItem()))return;
		if(pl.getInventory().getBoots()!= null)return;
		pl.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,Integer.MAX_VALUE, 1, true,false));
	}*/
	@EventHandler
	public void onSprint(PlayerToggleSprintEvent ev){
		Player pl = (Player)ev.getPlayer();
		if(!check(pl))return;
		if(!ev.isSprinting()){
			ev.setCancelled(true);
			pl.setSprinting(true);
		}
	}
	
	@EventHandler
	public void onEquip(ArmorEquipEvent ev){
		Player pl = (Player)ev.getPlayer();

		plugin.getTaskHandler().run(() ->{
			if(check(pl)){
			
				pl.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,Integer.MAX_VALUE, 1, true,false));
			}else{
				pl.removePotionEffect(PotionEffectType.SPEED);
			}

		});
	}
}
