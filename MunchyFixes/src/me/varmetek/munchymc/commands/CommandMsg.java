package me.varmetek.munchymc.commands;

import me.varmetek.core.commands.CmdCommand;
import me.varmetek.core.service.Element;
import me.varmetek.core.util.Messenger;
import me.varmetek.munchymc.Main;
import me.varmetek.munchymc.backend.PlayerSession;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.Arrays;

/**
 * Created by XDMAN500 on 5/6/2017.
 */
public class CommandMsg implements Element
{
  private Main main;


  public CommandMsg (Main main){
    this.main = main;
  }

  @Override
  public void clean (){
    main = null;
  }

  @Override
  public CmdCommand[] supplyCmd (){
    return new CmdCommand[]{
      new CmdCommand.Builder("msg")
        .setLogic((sender, alias, args, len) -> {
          if (!sender.isPlayer()){
            Messenger.send(sender.asSender(), "&c This command is for players only");
            return;
          }
          Player pl = sender.asPlayer();
          if (len <= 1){
            Messenger.send(pl, "&cUsage: /msg <player> <text>");
          } else {
            Player target = Bukkit.getPlayer(args[0]);
            if (target == null){
              Messenger.send(pl, "&c Player \"" + args[0] + "\" is not online");
              return;
            }
            PlayerSession plUser = main.getPlayerHandler().getSession(pl);
            PlayerSession targUser = main.getPlayerHandler().getSession(target);


            plUser.setMsgReply(target.getName());
            plUser.setLastMsgReply(System.currentTimeMillis());
            if ((System.currentTimeMillis() - targUser.getLastMsgReply()) >= 1500){
              targUser.setMsgReply(pl.getName());
              targUser.setLastMsgReply(System.currentTimeMillis());
            }
            Messenger.send(pl, "&7&l To " + target.getName() + "&8=> &7" + msg(Arrays.copyOfRange(args, 1, len)));
            Messenger.send(target, "&7&l From " + pl.getName() + "&8=>&7 " + msg(Arrays.copyOfRange(args, 1, len )));
          }

        }).build(),
      new CmdCommand.Builder("r")
        .setLogic((sender, alias, args, len) -> {
          if (!sender.isPlayer()){
            Messenger.send(sender.asSender(), "&c This command is for players only");
            return;
          }
          Player pl = sender.asPlayer();
          if (len == 0){
            Messenger.send(pl, "&cUsage: /r <text>");
          } else {
            PlayerSession plUser = main.getPlayerHandler().getSession(pl);

            Player target = Bukkit.getPlayer(plUser.getMsgReply());
            if (target == null){
              Messenger.send(pl, "&c Player \"" + args[0] + "\" is not online");
              return;
            }
            pl.performCommand("msg " + target.getName() +" "+ msg(Arrays.copyOfRange(args, 0, len - 1)));

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
