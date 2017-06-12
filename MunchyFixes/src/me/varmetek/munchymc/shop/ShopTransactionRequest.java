package me.varmetek.munchymc.shop;

import org.apache.commons.lang.Validate;
import org.bukkit.entity.Player;

/**
 * Created by XDMAN500 on 6/11/2017.
 */
public class ShopTransactionRequest
{
  private final int transfered;
  private final boolean deposit;
  private final Player  agent;
  private final PlayerShop shop;


  public ShopTransactionRequest (PlayerShop shop, Player agent, boolean deposit, int transfered){
    Validate.notNull(shop,"Player shop cannot be null");
    Validate.notNull(agent, "Response cannot be null");
    Validate.notNull(deposit,"Action cannot be null");
    Validate.isTrue(transfered >=0,"Items transfered cannot be negative");

    this.shop = shop;
    this.agent = agent;
    this.deposit = deposit;
    this.transfered  = transfered;
  }


  public PlayerShop getPlayerShop(){
    return shop;
  }

  public boolean isDeposit(){
    return deposit;
  }

  public Player getPlayer(){
    return agent;
  }

  public int getTransfered(){
    return transfered;
  }


}
