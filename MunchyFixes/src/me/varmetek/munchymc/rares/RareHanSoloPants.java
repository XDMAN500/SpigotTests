package me.varmetek.munchymc.rares;

import me.varmetek.munchymc.backend.RareItemListener;
import me.varmetek.munchymc.backend.test.CustomItemRare;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.inventory.EquipmentSlot;

/**
 * Created by XDMAN500 on 12/26/2016.
 */
public class RareHanSoloPants extends RareItemListener
{

	public RareHanSoloPants(CustomItemRare rare){

		super(rare);

	}

	@Override
	public boolean check (Player pl)
	{
		return isEquiped(pl, EquipmentSlot.LEGS);
	}


	@Override
	public void clean (){

	}

	@Override
	public Listener supplyListener (){
		return new Listener()
		{
			@EventHandler
			public void exp(PlayerExpChangeEvent ev)
			{
				if(check(ev.getPlayer()))
					ev.setAmount(ev.getAmount()*20);
			}

		};
	}
}
