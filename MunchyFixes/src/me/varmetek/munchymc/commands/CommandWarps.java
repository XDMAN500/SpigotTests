package me.varmetek.munchymc.commands;

import com.google.common.collect.ImmutableList;
import me.varmetek.core.commands.CmdCommand;
import me.varmetek.core.commands.CmdSender;
import me.varmetek.core.service.Element;
import me.varmetek.core.util.Messenger;
import me.varmetek.munchymc.MunchyMax;
import me.varmetek.munchymc.backend.Point;
import me.varmetek.munchymc.backend.PointManager;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import ru.tehkode.permissions.PermissionUser;

import java.util.*;

/**
 * Created by XDMAN500 on 5/6/2017.
 */
public class CommandWarps implements Element
{


  private Map<UUID,String> confirm = new HashMap<UUID,String>();


  public CommandWarps (){




  }

  public boolean hasPermission (Player pl, String warp){
    if(pl.isOp())return true;
    PermissionUser pu = MunchyMax.getHookManager().permHook.getManager().getUser(pl);
    return pu.has("munchymax.warps." + warp) || pu.has("munchymax.warps.*");
  }


  @Override
  public void clean (){

  }

  @Override
  public CmdCommand[] supplyCmd (){
    return new CmdCommand[]{
      new CmdCommand.Builder("spawn").setLogic(

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
        pl.performCommand("warp spawn");
      }).build(),

        new CmdCommand.Builder("warp").setLogic(
          (cmd)->{

            CmdSender sender = cmd.getSender();
            int length = cmd.getArguments().size();
            String alias = cmd.getAlias();
            ImmutableList<String> args = cmd.getArguments();

          if (!sender.isPlayer()){
            Messenger.send(sender.asSender(), "&c This command is for players only");
            return;
          }

          Player pl = sender.asPlayer();
          PointManager manager = MunchyMax.getPointManager();

          if (length == 0){
            List<String> warps = manager.getWarps();
            Messenger.send(pl,
              " ",
              "&6Warps");
            for (String name : warps) {
              if (hasPermission(pl, name)){
                Messenger.send(pl, "&a  " + name);
              }
            }
          } else {
            String name = args.get(0);
            Optional<Point> point = manager.getWarp(name);
            if (point.isPresent()){
              if (hasPermission(pl, name)){
                point.get().teleport(pl);
                Messenger.send(pl, "&2@ &aYou have been teleported to warp \"" + name + "\".");
              } else {
                Messenger.send(pl, "&4@ &cYou don't have permission to teleported to warp \"" + name + "\".");
              }
            } else {
              Messenger.send(pl, "&4@ &cWarp \"" + name + "\" does not exist.");
            }
          }


        }).build(),

        new CmdCommand.Builder("editpoint").setLogic(
          (cmd)->{

            CmdSender sender = cmd.getSender();
            int length = cmd.getArguments().size();
            String alias = cmd.getAlias();
            ImmutableList<String> args = cmd.getArguments();

          if (!sender.isPlayer()){
            Messenger.send(sender.asSender(), "&c This command is for players only");
            return;
          }
          Player pl = sender.asPlayer();
          PointManager manager = MunchyMax.getPointManager();
          PermissionUser pu = MunchyMax.getHookManager().permHook.getManager().getUser(pl);

          if (!pu.has("munchymax.editpoint")){
            Messenger.send(pl, "&4@&c You do not have permission for this command.");
            return;
          }


          if (length == 0){
            Messenger.send(pl,
              " ",
              "&6Help Menu");
            Messenger.send(pl,
              "&a /editpoint set <name>",
              "&a /editpoint remove <name>",
              "&a /editpoint list",
              "&a /editpoint config <name>",
              "&a /editpoint tp <name>",
              "&a /editpoint server"

            );
          } else {
            switch (args.get(0).toLowerCase()) {
              case "set":{
                if (length <= 1){
                  Messenger.send(pl, "&4Usage:&c  /editpoint set <name>");
                  return;

                } else {
                  String name = args.get(1);
                  Optional<Point> point = manager.getPoint(name);
                  if (point.isPresent()){
                    Optional<CommandAction> op = MunchyMax.getElementManager().getElement(CommandAction.class);
                    if (op.isPresent()){
                      Messenger.send(pl, "&cWarp \"" + name + "\" already exists. &bDo /confirm to overwrite it.");
                      CommandAction ca = op.get();
                      ca.setAction(pl.getUniqueId(),
                        (CommandAction.ActionCommand) () -> {
                          Point p = new Point.Builder(pl.getLocation()).build();
                          manager.setPoint(name, p);
                          Messenger.send(pl, "&a Point \"" + name + "\" has been overwritten.");
                        }
                      );
                    } else {
                      Point p = new Point.Builder(pl.getLocation()).build();
                      manager.setPoint(name, p);
                      Messenger.send(pl, "&a Point \"" + name + "\" has been overwritten.");
                    }
                  } else {
                    Point p = new Point.Builder(pl.getLocation()).build();
                    manager.setPoint(name, p);
                    Messenger.send(pl, "&a Point \"" + name + "\" has been created.");
                  }
                }
              }break;
              case "remove": {
                if (length <= 1){
                  Messenger.send(pl, "&4Usage:&c  /editpoint remove <name>");

                } else {
                  String name = args.get(1);
                  Optional<Point> point = manager.getPoint(name);
                  if (point.isPresent()){
                    manager.delPoint(name);
                    Messenger.send(pl, "&a Point \"" + name + "\" has been deleted.");
                  } else {
                    Messenger.send(pl, "&c Point \"" + name + "\" does not exist.");
                  }
                }
              }break;
              case "tp": {
                if (length <= 1){
                  Messenger.send(pl, "&4Usage:&c  /editpoint tp <name>");

                } else {
                  String name = args.get(1);
                  Optional<Point> point = manager.getPoint(name);
                  if (point.isPresent()){
                    point.get().teleport(pl);
                    Messenger.send(pl, "&a You have been teleported to point \"" + name + "\".");
                  } else {
                    Messenger.send(pl, "&c Point \"" + name + "\" does not exist.");
                  }
                }
              }break;
              case "list": {
                Messenger.send(pl,
                  " ",
                  "&6A list of all points");
                for (String name : manager.getPoints().keySet()) {
                  Messenger.send(pl, "&b- &a" + (manager.isWarp(name) ? "&l" : "") + name);
                }
              }break;
              case "config": {
                if (length <= 1){
                  Messenger.send(pl, "&4Usage:&c  /editpoint config <name>");

                } else {
                  String name = args.get(1);
                  if (!manager.pointExist(name)){
                    Messenger.send(pl, "&c Point \"" + name + "\" does not exist.");
                    return;
                  }

                  if (length <= 2){
                    Messenger.send(pl, "&4Usages:",
                      "&c  /editpoint config " + name + " setwarp",
                      "&c  /editpoint config " + name + " delwarp"
                    );
                  } else {
                    switch (args.get(2)) {
                      case "setwarp":
                        manager.setWarp(name);
                        Messenger.send(pl, "&a Point \"" + name + "\" can now be used in /warp.");

                        break;
                      case "unsetwarp":
                        manager.delWarp(name);
                        Messenger.send(pl, "&a Point \"" + name + "\" can no longer be used in /warp.");
                        break;
                    }
                  }
                }
              } break;
              case "server": {
                if (length <= 1){
                  Messenger.send(pl,
                    " ",
                    "&4Usage:",
                    "&c&l  >&7 /editpoint server save",
                    "&c&l  >&7 /editpoint server load"
                  );
                } else {
                  switch (args.get(1)) {
                    case "save": {
                      try {
                        MunchyMax.getPointFileManager().saveAllPoints();
                        Messenger.send(pl, "&A&l> &7All points have been saved to disk");
                      } catch (Exception e) {
                        Messenger.send(pl, "&c&l> &7An error occured while saving points");
                        Messenger.send(pl, "&c \"" + e.getMessage() + "\"");
                        e.printStackTrace();
                      }
                    }
                    break;

                    case "load": {
                      try {
                        MunchyMax.getPointFileManager().loadllPoints();
                        Messenger.send(pl, "&A&l> &7All points have been loaded from disk");
                      } catch (Exception e) {
                        Messenger.send(pl, "&c&l> &7An error occured while loading points");
                        Messenger.send(pl, "&c \"" + e.getMessage() + "\"");
                        e.printStackTrace();
                      }
                    }
                    break;

                    default: {
                      pl.performCommand("editpoint server");
                    }
                  }
                }
              }break;

              default: {
                Messenger.send(pl, "&cUnknown Command");
                pl.performCommand("editpoint");

              }break;
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
