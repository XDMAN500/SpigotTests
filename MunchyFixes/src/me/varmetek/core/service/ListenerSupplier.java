package me.varmetek.core.service;

import org.bukkit.event.Listener;

/**
 * Created by XDMAN500 on 5/6/2017.
 */
public interface ListenerSupplier
{
  Listener supplyListener();
}
