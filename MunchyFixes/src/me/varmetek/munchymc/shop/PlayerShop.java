package me.varmetek.munchymc.shop;

import org.apache.commons.lang.Validate;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by XDMAN500 on 6/11/2017.
 */
public class PlayerShop
{


  private UUID owner;
  private  ShopVend vend;
  private boolean isBuyShop;
  private AtomicInteger stock = new AtomicInteger(0);

  public PlayerShop(UUID owner, boolean isBuyShop){
    this.isBuyShop = isBuyShop;
    this.owner = owner;
  }

  public int getStock(){
    return stock.intValue();
  }


  public void updateVend(){
    if(vend != null){
      ShopVend.indexVend(vend);
    }
  }

  public void setShopVend(ShopVend vend){
    Validate.notNull(vend);
    this.vend = vend;
    updateVend();

  }


  public void setShopVend(String vend){
    Validate.notNull(vend);
    Optional<ShopVend> ven = ShopVend.getVend(vend);
   if(!ven.isPresent()){
     throw new ShopException("Alpha code \""+ vend+ "\" has not been registered!");
   }
   this.vend = ven.get();
    updateVend();

  }

  public boolean isBuyShop(){
    return isBuyShop;
  }


  public void setBuyShop(boolean on){
    isBuyShop = on;
  }







}
