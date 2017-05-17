package me.varmetek.core.util;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import me.varmetek.munchymc.util.UtilInventory;
import org.apache.commons.lang.Validate;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.*;

/**
 * Created by XDMAN500 on 5/13/2017.
 */
public class InventorySnapshot implements ConfigurationSerializable
{
  private ImmutableMap<Integer,ItemStack> inv;
  private ImmutableList<PotionEffect> effects;

  private InventorySnapshot(Builder builder){
    this.inv = ImmutableMap.copyOf(builder.inv);
    this.effects = ImmutableList.copyOf(builder.effects);
  }

  public ImmutableMap<Integer,ItemStack> getInventory(){
    return inv;
  }

  public ImmutableList<PotionEffect>  getPotionEffects(){
    return effects;
  }

  public void apply(Player pl){
    pl.getInventory().clear();
    pl.getActivePotionEffects().forEach( (eff)-> pl.removePotionEffect(eff.getType()));

    inv.forEach((slot,item) -> pl.getInventory().setItem(slot,item));

    if(effects != null){
      for(PotionEffect pe : effects){
        if(pe == null )continue;
        pl.addPotionEffect(pe);
      }
    }
  }

  @Override
  public Map<String,Object> serialize (){
    Map<String,Object> output = new HashMap<>();
    output.put("slots",inv);
    output.put("potions",effects);
    return output;
  }

  public static InventorySnapshot deserialize(Map<String,Object> input){
    Map<Integer,ItemStack> inv =  new HashMap<>();

    Map<Integer,ItemStack> first = (Map<Integer,ItemStack>)input.get("slots");
    inv = first;

   /* for(Map.Entry<String,ItemStack> i : first.entrySet()){
      try {
        inv.put(Integer.parseInt(i.getKey()), i.getValue());
      }catch(NumberFormatException expected){
        continue;
      }
    }*/

    return new Builder()
             .setInventory(inv)
             .setEffects( (List<PotionEffect>)input.get("potions"))
             .build();

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

    public Builder fromSnapshot(InventorySnapshot kit) {
      Validate.notNull(kit);
      setInventory(kit.inv);
      setEffects(kit.effects);
      return this;
    }




    public InventorySnapshot build(){

      return new InventorySnapshot(this);
    }
  }

}
