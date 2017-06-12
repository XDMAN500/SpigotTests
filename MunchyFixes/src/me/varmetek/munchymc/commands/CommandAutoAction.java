package me.varmetek.munchymc.commands;

import com.google.common.collect.ImmutableList;
import me.varmetek.core.commands.CmdCommand;
import me.varmetek.core.commands.CmdSender;
import me.varmetek.core.commands.CmdTab;
import me.varmetek.core.service.Element;
import me.varmetek.core.util.Messenger;
import me.varmetek.munchymc.backend.user.AutoAction;
import me.varmetek.munchymc.backend.user.PlayerHandler;
import me.varmetek.munchymc.backend.user.PlayerSession;
import me.varmetek.munchymc.util.Utils;
import org.apache.commons.lang.Validate;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

/**
 * Created by XDMAN500 on 6/4/2017.
 */
public class CommandAutoAction implements Element
{


  private PlayerHandler phandle;

  public CommandAutoAction(PlayerHandler handler){
    Validate.notNull(handler);
    phandle = handler;
  }

  @Override
  public void clean (){

  }

  @Override
  public CmdCommand[] supplyCmd (){
    return new CmdCommand[]{
      new CmdCommand.Builder("autoaction").setLogic(
        (cmd)->{

          CmdSender sender = cmd.getSender();
          int len = cmd.getArguments().size();
          String alias = cmd.getAlias();
          ImmutableList<String> args = cmd.getArguments();

        if(!sender.isPlayer()){
             Messenger.send(sender.asSender(), "&c This command is for players only");
             return;
        }
        Player player = sender.asPlayer();

        PlayerSession user = phandle.getSession(player);;
            if(len ==0){
              for(AutoAction a: AutoAction.values()){
                Messenger.send(player, "&7/"+alias+" "+a.name().toLowerCase()+" <on,off>");
              }
              return;
            }


              AutoAction aa;
              try{
                aa= AutoAction.valueOf(args.get(0).toUpperCase());
              }catch(Exception e){
                Messenger.send(player, "&cInvalid Argument");
                return ;
              }

              /*if(!player.hasPermission("ariaprison.autoaction.*")){
                if(!player.hasPermission("ariaprison.autoaction."+aa.name().toLowerCase()) ){
                  Messenger.send(player, "&cYou don't have permission to change the auto action &e"+aa.name()+"&c.");
                  return false;

                }
              }*/
              if(len == 1){
                user.toggleAutoAction(aa);

              }else{
                if(len == 2){
                  user.setAutoAction(aa,args.get(0).equalsIgnoreCase("on"));
                }
              }
              Messenger.send(player,"&7AutoAction &a"+aa.name() +" &7has been turned "+ (user.hasAutoAction(aa)? "&a&lON":"&c&lOFF"));



      }).setTab(
        (cmd)->{

          CmdSender sender = cmd.getSender();
          int len = cmd.getArguments().size();
          String alias = cmd.getAlias();
          ImmutableList<String> args = cmd.getArguments();
          if (len == 1){
            return CmdTab.send(Utils.toStringList(AutoAction.values(), (action) ->{ return action.name();}).toArray(new String[0] ));
          }
          if(len == 2){
            return CmdTab.send("on","off");
          }

          return CmdTab.send();
        }
      ).addAlias("aa").build()
    };
  }

  @Override
  public Listener supplyListener (){
    return null;
  }
}
