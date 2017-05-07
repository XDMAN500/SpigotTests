package me.varmetek.munchymc.backend.kit;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import me.varmetek.core.util.SimpleMapEntry;
import me.varmetek.munchymc.util.UtilInventory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.*;

/**
 * Created by XDMAN500 on 1/15/2017.
 */
public class Kit implements SimpleMapEntry
{
	private final ImmutableMap<Integer,ItemStack>  inv;
	private final ItemStack filler;
	private final String name;;
	private final ImmutableList<PotionEffect> effects;


	private Kit(String id, Map<Integer,ItemStack>  inv , Collection<PotionEffect> effects){
		name = id;
		this.inv = ImmutableMap.copyOf(inv);
		filler = null;
		this.effects = ImmutableList.copyOf(effects);

	}

	private Kit(String id,Inventory  inv, Collection<PotionEffect> effects){
		this(id, UtilInventory.toMap(inv), effects);
	}



	public void apply(Player pl){
		pl.getInventory().clear();
		UtilInventory.fromMap(inv,pl.getInventory());
		if(effects != null){
			for(PotionEffect pe : effects){
				if(pe == null )continue;
				pl.addPotionEffect(pe);
			}
		}
	}

	/*
	public void setInventory(Map<Integer,ItemStack>  inv){
		this.inv = inv;
	}
	public void setInventory(Inventory inv){
		this.inv = UtilInventory.toMap(inv);
	}
	public void setEffects(List<PotionEffect> effect) {
		this.effects = effects;
	}
*/
	@Override
	public String ID ()
	{
		return name;
	}

	public Map<Integer,ItemStack> getInv(){
		return inv;
	}

  public List<PotionEffect> getEffects(){
    return effects;
  }

  public static class Builder{
	  public Map<Integer,ItemStack>  inv;
	  public ItemStack filler;
	  public String name;
	  public Collection<PotionEffect> effects;

	  public Builder(String id, Map<Integer,ItemStack>  inv , Collection<PotionEffect> effects){
		  name = id;
		  this.inv = (inv == null) ? Collections.EMPTY_MAP : ImmutableMap.copyOf(inv);
		  filler = null;
		  this.effects = (effects == null ) ? Collections.EMPTY_LIST : ImmutableList.copyOf(effects);

	  }

	  public Builder(String id,Inventory  inv, Collection<PotionEffect> effects){
		  this(id, UtilInventory.toMap(inv), effects);
	  }

	  public Builder(Kit kit) {
		  this(kit.ID(), new HashMap<>(kit.getInv()), new ArrayList<>());
	  }

	  public Builder(String id, Player pl){
	    this(id, pl.getInventory(), pl.getActivePotionEffects());
    }

	  public Kit build(){
	  	return new Kit(name,inv,effects);
	  }
}
}
