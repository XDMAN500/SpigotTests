package me.varmetek.munchymc.rares;

import me.varmetek.munchymc.Main;
import me.varmetek.munchymc.backend.RareItemListener;
import me.varmetek.munchymc.backend.test.CustomItemRare;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.inventory.EquipmentSlot;

/**
 * Created by XDMAN500 on 12/26/2016.
 */
public class RareHanSoloPants extends RareItemListener
{

	public RareHanSoloPants(CustomItemRare rare, Main plugin){

		super(rare,plugin);

	}

	@Override
	public boolean check (Player pl)
	{
		return isEquiped(pl, EquipmentSlot.LEGS);
	}

	@EventHandler
	public void exp(PlayerExpChangeEvent ev)
	{
		if(check(ev.getPlayer()))
		ev.setAmount(ev.getAmount()*20);
	}
}
