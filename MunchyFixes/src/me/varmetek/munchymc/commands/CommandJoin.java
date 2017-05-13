package me.varmetek.munchymc.commands;

import me.varmetek.core.commands.CmdCommand;
import me.varmetek.core.service.Element;
import me.varmetek.core.util.Messenger;
import me.varmetek.munchymc.MunchyMax;
import me.varmetek.munchymc.backend.PlayerSession;
import org.apache.commons.lang.Validate;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.Arrays;

/**
 * Created by XDMAN500 on 3/4/2017.
 */
public class CommandJoin implements Element
{




  public CommandJoin (){



  }

  private String condense (String[] stuff, String separator){
    Validate.notNull(stuff);
    Validate.notEmpty(stuff);
    Validate.noNullElements(stuff);
    Validate.notNull(separator);

    StringBuilder b = new StringBuilder(stuff[0]);
    if (stuff.length > 1){
      for (int i = 1; i < stuff.length; i++) {
        b.append(separator).append(stuff[i]);
      }
    }
    return b.toString();
  }

  @Override
  public void clean (){

  }

  @Override
  public CmdCommand[] supplyCmd (){
    return new CmdCommand[]{

      new CmdCommand.Builder("join", (sender, label, args, length) ->{

        Player player;
        if (sender.isPlayer()){
          player = sender.asPlayer();
        } else {
          sender.asSender().sendMessage("This command is only for players");
          return;
        }
        int len = args.length;
        PlayerSession user = MunchyMax.getPlayerHandler().getSession(player);
        if (len == 0){

          Messenger.send(player,
            " ",
            "&b/" + label + " set <msg> &7- sets the join message",
            "&b/" + label + " getraw &7- sets the raw join message format",
            "&b/" + label + " get &7- sets the compiled join message format");
        } else {
          switch (args[0]) {
            case "get":
              Messenger.send(player, "&b Your join message: &r" + user.compileJoinMessage());
              ;
              break;
            case "getraw":
              player.sendMessage(Messenger.color("&b Your join message: &r") + user.getPlayerData().getJoinMessage());
              break;
            case "set":
              if (args.length > 1){
                user.getPlayerData().setJoinMessage(condense(Arrays.copyOfRange(args, 1, len), " "));
                Messenger.send(player, "&bJoin message set to:&r " + user.compileJoinMessage());
              } else {
                Messenger.send(player, "&b/" + label + " set <msg> &7- sets the join message");
              }
              break;
          }
        }

      }).addAlias("j").build(),


      new CmdCommand.Builder("leave", (sender, label, args, length) -> {

        Player player;
        if (sender.isPlayer()){
          player = sender.asPlayer();
        } else {
          sender.asSender().sendMessage("This command is only for players");
          return;
        }
        int len = args.length;
        PlayerSession user = MunchyMax.getPlayerHandler().getSession(player);
        if (len == 0){

          Messenger.send(player,
            "&b/" + label + " set <msg> &7- sets the leave message",
            "&b/" + label + " getraw &7- sets the raw leave message format",
            "&b/" + label + " get &7- sets the compiled leave message format");
        } else {
          switch (args[0]) {
            case "get":
              Messenger.send(player, "&b Your leave message: &r" + user.compileLeaveMessage());
              break;
            case "getraw":
              player.sendMessage(Messenger.color("&b Your leave message: &r") + user.getPlayerData().getLeaveMessage());
              break;
            case "set":
              if (args.length > 1){
                user.getPlayerData().setLeaveMessage(condense(Arrays.copyOfRange(args, 1, len), " "));
                Messenger.send(player, "&bLeave message set to :&r" + user.compileLeaveMessage());
              } else {
                Messenger.send(player, "&b/" + label + " set <msg> &7- sets the leave message");
              }
              break;
          }
        }

      }).build()
    };
  }

  @Override
  public Listener supplyListener (){
    return null;
  }
}
