package me.varmetek.munchymc.commands;

import com.google.common.collect.ImmutableList;
import me.varmetek.core.commands.CmdCommand;
import me.varmetek.core.commands.CmdSender;
import me.varmetek.core.service.Element;
import me.varmetek.core.util.Messenger;
import me.varmetek.munchymc.MunchyMax;
import me.varmetek.munchymc.backend.exceptions.ConfigException;
import org.bukkit.event.Listener;

/**
 * Created by XDMAN500 on 1/14/2017.
 */
public class CommandLoad implements Element
{
  private final CmdCommand[] commands;

  public CommandLoad (){


    commands = new CmdCommand[]{

      new CmdCommand.Builder("load").setLogic(
        (cmd)->{

          CmdSender sender = cmd.getSender();
          int len = cmd.getArguments().size();
          String alias = cmd.getAlias();
          ImmutableList<String> args = cmd.getArguments();

        if (!sender.isPlayer()) return;
        try {
          MunchyMax.getPlayerFileManager().readInventory(MunchyMax.getPlayerHandler().getSession(sender.asPlayer()));
        }catch(ConfigException e){
          Messenger.send(sender.asSender(), "&c Command failed due to an error");
        }

      }).build(),

      new CmdCommand.Builder("save").setLogic(
        (cmd)->{

        CmdSender sender = cmd.getSender();
        int len = cmd.getArguments().size();
        String alias = cmd.getAlias();
          ImmutableList<String> args = cmd.getArguments();

        if (!sender.isPlayer()) return;
        try {
          MunchyMax.getPlayerFileManager().writeInventory(MunchyMax.getPlayerHandler().getSession(sender.asPlayer()));
        } catch(ConfigException e){
          Messenger.send(sender.asSender(), "&c Command failed due to an error");

    }

      }).build(),

    };
  }


  @Override
  public void clean (){
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
