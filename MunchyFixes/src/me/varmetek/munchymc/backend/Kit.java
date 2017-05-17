package me.varmetek.munchymc.backend;

import me.varmetek.core.util.InventorySnapshot;
import me.varmetek.munchymc.backend.exceptions.DeserializeException;
import org.apache.commons.lang.Validate;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by XDMAN500 on 1/15/2017.
 */
public class Kit implements ConfigurationSerializable
{
  private InventorySnapshot inv;
	private  ItemStack filler;



	/*private Kit( Map<Integer,ItemStack>  inv , Collection<PotionEffect> effects){
		this.inv = ImmutableMap.copyOf(inv);
		filler = null;
		this.effects = ImmutableList.copyOf(effects);

	}

	private Kit(String id,Inventory  inv, Collection<PotionEffect> effects){
		this(id, UtilInventory.toMap(inv), effects);
	}*/

	private Kit(Builder b){
	  this.inv = b.inv;
  }


	public void apply(Player pl){
	  Validate.notNull(pl, "Cannot apply kit to null player");
    inv.apply(pl);
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

  public InventorySnapshot getInventorySnapshot(){
    return inv;
  }
	public Map<Integer,ItemStack> getInv(){
		return inv.getInventory();
	}

  public List<PotionEffect> getEffects(){
    return inv.getPotionEffects();
  }

  @Override
  public Map<String,Object> serialize (){
    Map<String,Object> output = new HashMap<>();
    output.put("inventory", inv);
    return output;
  }

  public static Kit deserialize(Map<String,Object>  map) throws DeserializeException{
    try {
      InventorySnapshot inv = (InventorySnapshot) map.get("inventory");
      return new Kit.Builder().setInventory(inv).build();
    }catch(Exception e){
      throw new DeserializeException(e);
    }
  }

  public static class Builder{
	  public InventorySnapshot inv;



    public Builder(){}



    public Builder setInventory(InventorySnapshot inv){
      Validate.notNull(inv);
      this.inv  = inv;
      return this;
    }

    public Builder fromPlayerInv(Player pl) {
      Validate.notNull(pl);
      inv = new InventorySnapshot.Builder().fromPlayer(pl).build();
      return this;
    }

    public Builder fromKit(Kit kit) {
      Validate.notNull(kit);
      inv = new InventorySnapshot.Builder().fromSnapshot(kit.inv).build();
      return this;
    }




	  public Kit build(){

	    return new Kit(this);
	  }
}
}
