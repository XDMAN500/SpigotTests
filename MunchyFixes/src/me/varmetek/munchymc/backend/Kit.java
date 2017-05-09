package me.varmetek.munchymc.backend;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import me.varmetek.munchymc.util.UtilInventory;
import org.apache.commons.lang.Validate;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.*;

/**
 * Created by XDMAN500 on 1/15/2017.
 */
public class Kit
{
	private ImmutableMap<Integer,ItemStack>  inv;
	private  ItemStack filler;
	private  ImmutableList<PotionEffect> effects;


	/*private Kit( Map<Integer,ItemStack>  inv , Collection<PotionEffect> effects){
		this.inv = ImmutableMap.copyOf(inv);
		filler = null;
		this.effects = ImmutableList.copyOf(effects);

	}

	private Kit(String id,Inventory  inv, Collection<PotionEffect> effects){
		this(id, UtilInventory.toMap(inv), effects);
	}*/

	private Kit(Map<Integer,ItemStack>  inv , List<PotionEffect> effects){
	  this.inv = ImmutableMap.copyOf(inv);
    this.effects = ImmutableList.copyOf(effects);
  }


	public void apply(Player pl){
		pl.getInventory().clear();
    pl.getActivePotionEffects().forEach( (eff)-> pl.removePotionEffect(eff.getType()));

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


	public Map<Integer,ItemStack> getInv(){
		return inv;
	}

  public List<PotionEffect> getEffects(){
    return effects;
  }

  public static class Builder{
	  public Map<Integer,ItemStack>  inv = Collections.EMPTY_MAP;
	  public ItemStack filler = null;
	  public List<PotionEffect> effects = Collections.EMPTY_LIST;


    public Builder(){}

    public Builder setInventory(Map<Integer,ItemStack> inv){
      Validate.notNull(inv);
        this.inv = inv;
        return this;
    }

    public Builder setInventory(Inventory inv){
      Validate.notNull(inv);
      return setInventory(UtilInventory.toMap(inv));
    }

    public Builder setEffects(List<PotionEffect> effects){
      Validate.notNull(effects);
      this.effects = effects;
      return this;
    }
    public Builder setEffects(Collection<PotionEffect> effects){
      Validate.notNull(effects);
     return setEffects(Arrays.asList(effects.toArray(new PotionEffect[0])));
    }

    public Builder fromPlayer(Player pl) {
      Validate.notNull(pl);
      setInventory(pl.getInventory());
      setEffects(pl.getActivePotionEffects());
      return this;
    }

    public Builder fromKit(Kit kit) {
      Validate.notNull(kit);
      setInventory(kit.inv);
      setEffects(kit.effects);
      return this;
    }




	  public Kit build(){

	    return new Kit(inv,effects);
	  }
}
}
