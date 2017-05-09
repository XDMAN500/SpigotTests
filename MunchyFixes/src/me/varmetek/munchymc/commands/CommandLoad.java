package me.varmetek.munchymc.commands;

import me.varmetek.core.commands.CmdCommand;
import me.varmetek.core.service.Element;
import me.varmetek.munchymc.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

/**
 * Created by XDMAN500 on 1/14/2017.
 */
public class CommandLoad implements Element
{
  private Main plugin;
  private final CmdCommand[] commands;

  public CommandLoad (Main plugin){
    this.plugin = plugin;

    commands = new CmdCommand[]{

      new CmdCommand.Builder("load", (sender, alias, ags, length) -> {
        if (!sender.isPlayer()) return;
        plugin.getDataManager().asUserData().readInventory(plugin.getUserHandler().getUser((Player) sender));

      }).build(),

      new CmdCommand.Builder("save", (sender, alias, ags, length) -> {
        if (!sender.isPlayer()) return;
        plugin.getDataManager().asUserData().writeInventory(plugin.getUserHandler().getUser(sender.asPlayer()));


      }).build(),

    };
  }


  @Override
  public void clean (){
    plugin = null;
  }


  @Override
  public CmdCommand[] supplyCmd (){
    return new CmdCommand[0];
  }

  @Override
  public Listener supplyListener (){
    return null;
  }
}
