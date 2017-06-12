package me.varmetek.munchymc.shop;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Created by XDMAN500 on 6/11/2017.
 */
public class ShopVendManager
{
  public class Entry implements Comparable<Entry>{
    private final ShopVend key ;
    private final Long value;

    protected Entry(ShopVend key, Long value){
      this.key = key;
      this.value = value;
    }

    public ShopVend getShopVend(){
      return key;
    }

    public ShopVend getLatestTime(){
      return key;
    }

    @Override
    public int hashCode(){
      return key.hashCode();

    }

    @Override
    public boolean equals(Object c){
      if(c == null) return false;
      if(c.getClass() !=   this.getClass())return false;
      return c.hashCode() == this.hashCode();
    }

    @Override
    public int compareTo (Entry o){
      return (int) (this.value - o.value);
    }
  }
  public  final Map<String, Entry> venderIndex = new HashMap<>();


  public Optional<Entry> getByCode(String code){
    return Optional.ofNullable(venderIndex.get(code));
  }
}
