package me.varmetek.munchymc.rares;

import me.varmetek.munchymc.Main;
import me.varmetek.munchymc.backend.RareItemListener;
import me.varmetek.munchymc.backend.test.CustomItemRare;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.EquipmentSlot;

/**
 * Created by XDMAN500 on 12/26/2016.
 */
public class RareVaderCape extends RareItemListener
{

	public RareVaderCape (CustomItemRare rare, Main plugin){

		super(rare,plugin);
	}

	@Override
	public boolean check (Player pl)
	{

		return  isEquiped(pl, EquipmentSlot.LEGS);
	}

	@EventHandler
	public void  capVelocity(PlayerMoveEvent ev)
	{

		Player pl = ev.getPlayer();
		if(check(pl))
		{
			//Messenger.send(pl, "Velocity: " + pl.getVelocity().getY() );
			if(pl.getFallDistance() >= 2 && pl.getVelocity().getY() < 0.0 )
			{
				pl.setVelocity(pl.getLocation().getDirection().multiply(0.2).setY(Math.max(pl.getVelocity().getY(), 0.0)));

			}

		}
	}
	@EventHandler
	public void fallDamage(EntityDamageEvent ev)
	{
		Player pl = ev.getEntity() instanceof Player ? (Player)ev.getEntity() : null;
		if(pl == null)return;
		if(!check(pl)) return;
		if(!ev.getCause().equals(EntityDamageEvent.DamageCause.FALL));
		ev.setDamage(0);
		pl.setFallDistance(0);


	}
}
