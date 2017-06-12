package me.varmetek.munchymc.commands;

import com.google.common.collect.ImmutableList;
import me.varmetek.core.commands.CmdCommand;
import me.varmetek.core.commands.CmdSender;
import me.varmetek.core.service.Element;
import me.varmetek.core.util.Messenger;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;

/**
 * Created by XDMAN500 on 5/9/2017.
 */
public class CommandPanel implements Element
{

  public CommandPanel(){

  }

  @Override
  public void clean (){

  }

  @Override
  public CmdCommand[] supplyCmd (){
    return new CmdCommand[]{
      new CmdCommand.Builder("panel").setLogic(

        (cmd)->{

      CmdSender sender = cmd.getSender();
      int len = cmd.getArguments().size();
      String alias = cmd.getAlias();
      ImmutableList<String> args = cmd.getArguments();

        CommandSender send  = sender.asSender();

        if(len == 0){
          Messenger.send(send,
            " ",
            "&6&l Help Menu",
            "&7&l >&7  /panel",
            " ");
        }
      }).build()
    };
  }

  @Override
  public Listener supplyListener (){
    return null;
  }
}
