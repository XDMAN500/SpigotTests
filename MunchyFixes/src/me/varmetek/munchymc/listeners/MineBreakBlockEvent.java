package me.varmetek.munchymc.listeners;

import me.varmetek.munchymc.mines.Mine;
import org.apache.commons.lang.Validate;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockEvent;

/**
 * Created by XDMAN500 on 6/7/2017.
 */
public class MineBreakBlockEvent extends BlockEvent implements Cancellable
{
  private static final HandlerList handlers = new HandlerList();

  private final Mine mine;
  private boolean canceled = false;
  private final Player pl;

  public MineBreakBlockEvent (Block theBlock, Player player, Mine mine){
    super(theBlock);

    Validate.notNull(mine);
    pl = player;
    this.mine = mine;

  }

  @Override
  public boolean isCancelled (){
    return canceled;
  }

  @Override
  public void setCancelled (boolean cancel){
    canceled = cancel;
  }

  public HandlerList getHandlers() {
    return handlers;
  }

  public static HandlerList getHandlerList() {
    return handlers;
  }


}
