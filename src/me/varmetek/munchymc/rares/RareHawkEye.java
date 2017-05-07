package me.varmetek.munchymc.rares;

import me.varmetek.munchymc.Main;
import me.varmetek.munchymc.backend.RareItemListener;
import me.varmetek.munchymc.backend.test.CustomItemRare;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.util.List;

public class RareHawkEye extends RareItemListener{



	public RareHawkEye(CustomItemRare rare, Main plugin)
	{

		super(rare, plugin);
	}




	@Override

	public boolean check(Player pl)
	{
		return isEquiped(pl, EquipmentSlot.HAND);
	}


	@EventHandler
	public void onClick(PlayerInteractEvent ev){
		Player pl = (Player)ev.getPlayer();
		if(!check(pl))return;
		
		List<Entity> list = pl.getNearbyEntities(10, 10, 10);
		for(Entity e: list){
			if(e instanceof LivingEntity && pl.hasLineOfSight(e)){
				if(ev.getAction().equals(Action.LEFT_CLICK_AIR) ||ev.getAction().equals(Action.LEFT_CLICK_BLOCK)  ){
					LivingEntity le = (LivingEntity)e;
					le.damage(0, pl);
					le.setNoDamageTicks(0);
					
				}
			}
		}
	
	}
}
