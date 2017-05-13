package me.varmetek.core.util;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by XDMAN500 on 5/13/2017.
 */
public abstract class PluginCore extends JavaPlugin implements Cleanable
{


  @Override
  public abstract void onLoad();
  @Override
  public abstract void onEnable();
  @Override
  public abstract void onDisable();

}
