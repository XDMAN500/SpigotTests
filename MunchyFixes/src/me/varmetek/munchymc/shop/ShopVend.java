package me.varmetek.munchymc.shop;


import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang.Validate;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Created by XDMAN500 on 6/11/2017.
 */
public class ShopVend
{


  public  static final long FLUSH_GRACE = 1000*60*60;
  public static long  recentEntry = 0;
  private static final Map<String,VendEntry> venderIndex = new HashMap<>();


  public static Optional<ShopVend> getVend(String code){
    Validate.notNull(code);
    VendEntry entry = venderIndex.get(code);
    return entry == null ? Optional.empty() : Optional.ofNullable(entry.getShopVend());

  }

  public static ImmutableMap<String,VendEntry> getVenderIndex(){
    return ImmutableMap.copyOf(venderIndex);
  }

  public static void flushIndex(){
    Map<String,VendEntry> map = getVenderIndex();
    long now = System.currentTimeMillis();
    map.forEach((id,entry) ->{
      if( now - entry.getLatestTime() >= FLUSH_GRACE ){
        venderIndex.remove(id);
      }
    });

  }


  public static void clearIndex(){
    venderIndex.clear();
  }

  public static void indexVend(ShopVend vend){
    Validate.notNull(vend);
    venderIndex.put(vend.alphaCode().toString(), new VendEntry(vend,System.currentTimeMillis()));

  }



  private final ItemStack item;
  private final AlphaCode code;

  public ShopVend(ItemStack i){
    item = i.clone();
    item.setAmount(1);
    code = new AlphaCode(item.hashCode());
  }

  public ItemStack getItem(){
    return item.clone();
  }

  public AlphaCode alphaCode(){

    return code;
  }

  @Override
  public int hashCode(){
    return item.hashCode();

  }

  @Override
  public boolean equals(Object c){
    if(c == null) return false;
    if(c.getClass() !=   this.getClass())return false;
    return c.hashCode() == this.hashCode();
  }
}
