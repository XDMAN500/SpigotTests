package me.varmetek.munchymc.commands;

import com.google.common.collect.ImmutableList;
import me.varmetek.core.commands.CmdCommand;
import me.varmetek.core.commands.CmdLogic;
import me.varmetek.core.commands.CmdSender;
import me.varmetek.core.commands.CmdTab;
import me.varmetek.core.service.Element;
import me.varmetek.core.util.Messenger;
import me.varmetek.core.util.PluginCore;
import org.bukkit.event.Listener;

import java.util.Collections;
import java.util.logging.Level;

/**
 * Created by XDMAN500 on 5/20/2017.
 */
public class CommandServer implements Element
{

  private PluginCore plugin;

  public CommandServer(PluginCore pl){
    plugin = pl;
  }


  @Override
  public void clean (){

  }

  @Override
  public CmdCommand[] supplyCmd (){
    return new CmdCommand[]{
      new CmdCommand.Builder("serset").setLogic(

        (cmd)->{

          CmdSender sender = cmd.getSender();
          int len = cmd.getArguments().size();
          String alias = cmd.getAlias();
          ImmutableList<String> args = cmd.getArguments();

        if(len == 0){
          Messenger.send(sender.asSender(),
            " ",
            "&Help Menu!",
            " &8&l > &7/sercon logs debug",
            " &8&l > &7/sercon logs normal"

          );
          return;
        }
   //////////////////////////
        switch (args.get(0).toLowerCase()){

          default:{
            Messenger.send(sender.asSender(),
              "&cUsage:",
              "&7  "+ CmdLogic.getUsage(alias,args,0,"logs")
            );
          }break;


          case "logs":{
            if(len <= 1){
              Messenger.send(sender.asSender(),
                "&cUsage:",
                "&7  "+ CmdLogic.getUsage(alias,args,1, "normal"),
                "&7  "+ CmdLogic.getUsage(alias,args,1, "debug")
                );
              return;
            }


            switch (args.get(1).toLowerCase()){
              default:{
                Messenger.send(sender.asSender(),
                  "&cUsage:",
                  "&7  "+ CmdLogic.getUsage(alias,args,1, "normal"),
                  "&7  "+ CmdLogic.getUsage(alias,args,1, "debug")
                );
              }break;


              case "normal":{
                plugin.getLogger().setLevel(Level.INFO);
                Messenger.send(sender.asSender(), "&7Logging has been set to normal");
              }break;

              case "debug":{
                plugin.getLogger().setLevel(Level.ALL);
                Messenger.send(sender.asSender(), "&7Logging has been set to debug");
              }break;

            }

          }break;

        }

      }).setTab(
        (cmd)->{

          CmdSender sender = cmd.getSender();
          int len = cmd.getArguments().size();
          String alias = cmd.getAlias();
          ImmutableList<String> args = cmd.getArguments();

        if(len == 0){
          return CmdTab.send("logs");
        }

        switch (args.get(0).toLowerCase()){
          case "logs":{
            if(len == 1){
              CmdTab.send("debug", "normal");
            }
          }break;
        }


        return Collections.EMPTY_LIST;
        }
      ).build()
    };
  }

  @Override
  public Listener supplyListener (){
    return null;
  }
}
