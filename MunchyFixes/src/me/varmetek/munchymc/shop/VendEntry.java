package me.varmetek.munchymc.shop;

/**
 * Created by XDMAN500 on 6/11/2017.
 */
public  class VendEntry implements Comparable<VendEntry>{
  private final ShopVend key ;
  private final Long value;

  protected VendEntry(ShopVend key, Long value){
    this.key = key;
    this.value = value;
  }

  public ShopVend getShopVend(){
    return key;
  }

  public Long getLatestTime(){
    return value;
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
  public int compareTo (VendEntry o){
    return (int) (this.value - o.value);
  }
}
