package me.varmetek.munchymc.listeners;

import me.varmetek.munchymc.mines.Mine;
import me.varmetek.munchymc.util.UtilInventory;
import org.apache.commons.lang.Validate;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;

/**
 * Created by XDMAN500 on 6/7/2017.
 */
public class MinePostAutoActionEvent extends Event
{
  private static final HandlerList handlers = new HandlerList();
  private final Mine mine;
  private Inventory drops;
  private final Block bl;
  private final Player pl;

  public MinePostAutoActionEvent (Block theBlock, Player player, Mine mine, Inventory drops){


    Validate.notNull(mine);
    Validate.notNull(drops);

    this.mine = mine;
    this.drops = drops;
    this.bl = theBlock;
    this.pl = player;
  }

  public MinePostAutoActionEvent (Block theBlock, Player player, Mine mine, Collection<ItemStack> drops){
    this(theBlock,player,mine, UtilInventory.convert2Inv(drops));
  }

  public Mine getMine(){
    return mine;
  }

  public Inventory getDrops(){
    return drops;
  }

  public Block getBlock(){
    return bl;
  }


  public Player getPlayer(){
    return pl;
  }
  public HandlerList getHandlers() {
    return handlers;
  }

  public static HandlerList getHandlerList() {
    return handlers;
  }
}

