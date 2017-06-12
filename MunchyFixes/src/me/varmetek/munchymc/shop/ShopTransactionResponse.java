package me.varmetek.munchymc.shop;

import org.apache.commons.lang.Validate;

/**
 * Created by XDMAN500 on 6/11/2017.
 */
public class ShopTransactionResponse
{
  private final int transfered;
  private final boolean deposit;
  private final boolean success;
  private final PlayerShop shop;


  public ShopTransactionResponse (PlayerShop shop, boolean success, boolean deposit, int transfered){
    Validate.notNull(shop,"Player shop cannot be null");
    Validate.isTrue(transfered >=0,"Items transfered cannot be negative");

    this.shop = shop;
    this.success = success;
    this.deposit = deposit;
    this.transfered  = transfered;
  }


  public PlayerShop getPlayerShop(){
    return shop;
  }

  public boolean isDeposit(){
    return deposit;
  }

  public boolean isSuccess(){
    return success;
  }

  public int getTransfered(){
    return transfered;
  }

}
