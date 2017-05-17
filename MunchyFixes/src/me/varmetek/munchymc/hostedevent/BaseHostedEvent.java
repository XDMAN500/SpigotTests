package me.varmetek.munchymc.hostedevent;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by XDMAN500 on 5/16/2017.
 */
public abstract class BaseHostedEvent
{
  protected final int maxPlayers;
  protected boolean started = false;
  protected List<UUID> playerRoster;

  public BaseHostedEvent(int maxPlayers){
    this.maxPlayers = maxPlayers;
    playerRoster = new ArrayList<>(maxPlayers);
  }

  public int getMaxPlayers(){
    return maxPlayers;
  }

  protected abstract void load();
  protected abstract void start();
  public abstract void removePlayer(Player pl);
  protected abstract void update() ;
  protected abstract  void giveInventory(Player pl);
  public abstract void stop();
  public abstract void cleanup();
}
