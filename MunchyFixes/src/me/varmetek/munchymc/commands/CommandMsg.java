package me.varmetek.munchymc.commands;

import com.google.common.collect.ImmutableList;
import me.varmetek.core.commands.CmdCommand;
import me.varmetek.core.commands.CmdSender;
import me.varmetek.core.service.Element;
import me.varmetek.core.util.Messenger;
import me.varmetek.munchymc.MunchyMax;
import me.varmetek.munchymc.backend.user.PlayerSession;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.Arrays;

/**
 * Created by XDMAN500 on 5/6/2017.
 */
public class CommandMsg implements Element
{



  public CommandMsg (){

  }

  @Override
  public void clean (){

  }

  @Override
  public CmdCommand[] supplyCmd (){
    return new CmdCommand[]{
      new CmdCommand.Builder("msg")
        .setLogic(
          (cmd)->{

            CmdSender sender = cmd.getSender();
            int len = cmd.getArguments().size();
            String alias = cmd.getAlias();
            ImmutableList<String> args = cmd.getArguments();


          if (!sender.isPlayer()){
            Messenger.send(sender.asSender(), "&c This command is for players only");
            return;
          }
          Player pl = sender.asPlayer();
          if (len <= 1){
            Messenger.send(pl, "&cUsage: /msg <player> <text>");
          } else {
            Player target = Bukkit.getPlayer(args.get(0));
            if (target == null){
              Messenger.send(pl, "&c Player \"" + args.get(0) + "\" is not online");
              return;
            }
            PlayerSession plUser = MunchyMax.getPlayerHandler().getSession(pl);
            PlayerSession targUser = MunchyMax.getPlayerHandler().getSession(target);


            plUser.setMsgReply(target.getName());
            plUser.setLastMsgReply(System.currentTimeMillis());
            if ((System.currentTimeMillis() - targUser.getLastMsgReply()) >= 1500){
              targUser.setMsgReply(pl.getName());
              targUser.setLastMsgReply(System.currentTimeMillis());
            }
            Messenger.send(pl, "&7&l To " + target.getName() + "&8=> &7" + msg(Arrays.copyOfRange(args.toArray(new String[0]), 1, len)));
            Messenger.send(target, "&7&l From " + pl.getName() + "&8=>&7 " + msg(Arrays.copyOfRange(args.toArray(new String[0]), 1, len )));
          }

        }).build(),
      new CmdCommand.Builder("r")
        .setLogic(

          (cmd)->{

            CmdSender sender = cmd.getSender();
            int len = cmd.getArguments().size();
            String alias = cmd.getAlias();
            ImmutableList<String> args = cmd.getArguments();


          if (!sender.isPlayer()){
            Messenger.send(sender.asSender(), "&c This command is for players only");
            return;
          }
          Player pl = sender.asPlayer();
          if (len == 0){
            Messenger.send(pl, "&cUsage: /r <text>");
          } else {
            PlayerSession plUser = MunchyMax.getPlayerHandler().getSession(pl);

            Player target = Bukkit.getPlayer(plUser.getMsgReply());
            if (target == null){
              Messenger.send(pl, "&c Player \"" + args.get(0) + "\" is not online");
              return;
            }
            pl.performCommand("msg " + target.getName() +" "+ msg(Arrays.copyOfRange(args.toArray(new String[0]), 0, len - 1)));

          }

        }).build()};
  }

  @Override
  public Listener supplyListener (){
    return null;
  }

  private static String msg (String... stuff){
    StringBuilder output = new StringBuilder();
    for (String part : stuff) {
      output.append(part).append(" ");
    }
    return output.toString();
  }
}
