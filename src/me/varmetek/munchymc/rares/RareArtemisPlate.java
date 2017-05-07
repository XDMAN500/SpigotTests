package me.varmetek.munchymc.rares;

import me.varmetek.munchymc.Main;
import me.varmetek.munchymc.backend.RareItemListener;
import me.varmetek.munchymc.backend.test.CustomItemRare;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.EquipmentSlot;

public class RareArtemisPlate extends RareItemListener
{


	public RareArtemisPlate(CustomItemRare rare, Main plugin)
	{

		super(rare,plugin);

	}
	@Override
	public boolean check(Player pl)
	{
		return  isEquiped(pl, EquipmentSlot.CHEST);
		
	}


	@EventHandler
	public void onDamage(EntityDamageByEntityEvent ev){
		Entity damager = ev.getDamager();
		Entity damagee = ev.getEntity();
		if(!(damagee instanceof LivingEntity))return;
		if((damagee instanceof Player))return;
		if(!(damager instanceof Player))return;
		Player pl = (Player)damager;
		if(!check(pl))return;
		LivingEntity target = (LivingEntity)damagee;
		ev.setDamage(target.getMaxHealth()* (target.getMaxHealth()/Math.max(ev.getFinalDamage(),0.01d)));
		
		pl.setSaturation(20);
		pl.setFoodLevel(20);
		

	
		
				
	}

}
