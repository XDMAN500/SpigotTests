package me.varmetek.munchymc.fakeevents;

import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;

/**
 * Created by XDMAN500 on 5/6/2017.
 */
public interface EvLogic<T extends Event>
{
  @EventHandler
  void run (T ev);
}
