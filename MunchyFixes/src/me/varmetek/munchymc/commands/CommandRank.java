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
import ru.tehkode.permissions.PermissionGroup;
import ru.tehkode.permissions.PermissionManager;
import ru.tehkode.permissions.PermissionUser;

/**
 * Created by XDMAN500 on 4/22/2017.
 */
public class CommandRank implements Element
{





  public CommandRank (){



  }


  @Override
  public CmdCommand[] supplyCmd (){
    return new CmdCommand[]{
      new CmdCommand.Builder("rank").setLogic(
        (cmd)->{

          CmdSender sender = cmd.getSender();
          int length = cmd.getArguments().size();
          String alias = cmd.getAlias();
          ImmutableList<String> args = cmd.getArguments();
      /*msg.send("&7");
							msg.send("&7Player");
							msg.send( "& &a/" + alias +"  player  perm add "<player"> <permission>");
							msg.send( "&7 &a/" + alias +"  player perm check <"player"> <permission>");
							msg.send( "&7 &a/" + alias +"  player perm del <"player"> <permission>" );

							msg.send("&7");
							msg.send( "&7 &a/" + alias +"  player group join  <"player"> <group");
							msg.send( "&7 &a/" + alias +"  player group check <"player"> <group>");
							msg.send( "&7 &a/" + alias +"  player group leave <"player"> <group>" );


							msg.send("&7");
							msg.send("&7Group");
							msg.send( "&7 &a/" + alias +"  group perm add <group> <permission>");
							msg.send( "&7 &a/" + alias +"  group perm check <group> <permission>");
							msg.send( "&7 &a/" + alias +"  group perm del <group> <permission>" );


							msg.send("&7");
							msg.send( "&7Usage: &a/" + alias +"  group player add <group> <player>");
							msg.send( "&7Usage: &a/" + alias +"  group player check <group><player>");
							msg.send( "&7Usage: &a/" + alias +"  group player del <group> <player>" );
							msg.send( "&7Usage: &a/" + alias +"  group player list <group> <player>" );

									msg.send( "&7Usage: &a/" + alias +"  group create <group> );
									msg.send( "&7Usage: &a/" + alias +"  group remove <group> );

							*/
        boolean isPlayer = sender.isPlayer();
        Player player;
        PlayerSession user;

        if (isPlayer){
          player = sender.asPlayer();
          user = MunchyMax.getPlayerHandler().getSession(player);


        } else {
          sender.asSender().sendMessage("Running Command as non-player");
          return;
        }

        if (length == 0){
          if (isPlayer){
            Messenger.send(sender.asSender(),
              "Usage: &a/" + alias + " player",
              "Usage: &a/" + alias + " group");


          }
        } else {
          switch (args.get(0).toLowerCase()) {
            case "p":
            case "player"://////////////////////////////////////////////////////////

              if (length > 1){
                switch (args.get(1).toLowerCase()) {

                  case "p":
                  case "perm":////////////////////////////////////////////
                    handlePlayerPerm(args.toArray(new String[0]), sender);
                    break;

                  case "group":////////////////////////////////////////////////////////////////////

                    break;
                }
              } else {

                Messenger.send(sender.asSender(),
                  "&7",
                  "&cUsage: ",
                  "&8&l   > &7/" + alias + " player perm",
                  "&8&l   > &7/" + alias + " player group",
                  " ");

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

  private void handlePlayerGroup (String[] args, CmdSender sender, String alias){
    int len = args.length;
    if (len > 2){

      PermissionManager manager = MunchyMax.getHookManager().permHook.getManager();
      switch (args[2]) {
        case "join":///////////////////////////////////////////////////////
          if (len > 4){
            String playerName = args[3];
            String groupName = args[4];
            Player playr;
            if ((playr = Bukkit.getPlayer(playerName)) == null){

              Messenger.send(sender.asSender(), "&cPlayer \"" + playerName + "\" is not online");
              return;
            }

            PermissionGroup pg;

            if ((pg = manager.getGroup(groupName)) == null){
              Messenger.send(sender.asSender(), "&cGroup \"" + groupName + "\" doesn't exist.");
              return;
            }

            PermissionUser puser = manager.getUser(playr);

            for (PermissionGroup ppg : puser.getGroups()) {
              String val = puser.getOption("type");
              if (val != null && val == pg.getOption("type")){
                puser.removeGroup(ppg);
              }
            }

            puser.addGroup(pg);

            Messenger.send(sender.asSender(), "&a Player \"" + playr.getName() + "\" has joined group \"" + pg.getName() + "\".");

          } else {
            Messenger.send(sender.asSender(),
              "&cUsage: &7/" + alias + " " + args[0] + " " + args[1] + " " + args[2] + " <player> <permission>");
          }
          break;

        case "check":////////////////////////////////////////
          if (len > 4){
            String playerName = args[3];
            String groupName = args[4];
            Player playr;
            if ((playr = Bukkit.getPlayer(playerName)) == null){

              Messenger.send(sender.asSender(), "&cPlayer \"" + playerName + "\" is not online");
              return;
            }

            PermissionGroup pg;

            if ((pg = manager.getGroup(groupName)) == null){
              Messenger.send(sender.asSender(), "&cGroup \"" + groupName + "\" doesn't exist.");
              return;
            }

            PermissionUser puser = manager.getUser(playr);


            Messenger.send(sender.asSender(), puser.inGroup(pg) ?
                                                "&a Player \"" + playr.getName() + "\" is in group \"" + pg.getName() + "\"." :
                                                "&a Player \"" + playr.getName() + "\" is not in group \"" + pg.getName() + "\"."
            );


          } else {
            Messenger.send(sender.asSender(),
              "&cUsage: &7/" + alias + " " + args[0] + " " + args[1] + " " + args[2] + " <player> <permission>");
          }
          break;

        case "leave":////

          if (len > 4){
            String playerName = args[3];
            String groupName = args[4];
            Player playr;
            if ((playr = Bukkit.getPlayer(playerName)) == null){

              Messenger.send(sender.asSender(), "&cPlayer \"" + playerName + "\" is not online");
              return;
            }

            PermissionGroup pg;

            if ((pg = manager.getGroup(groupName)) == null){
              Messenger.send(sender.asSender(), "&cGroup \"" + groupName + "\" doesn't exist.");
              return;
            }

            PermissionUser puser = manager.getUser(playr);


            puser.removeGroup(pg);

            Messenger.send(sender.asSender(), "&a Player \"" + playr.getName() + "\" has left group \"" + pg.getName() + "\".");

          } else {
            Messenger.send(sender.asSender(),
              "&cUsage: &7/" + alias + " " + args[0] + " " + args[1] + " " + args[2] + " <player> <permission>");
          }
          break;


      }
    } else {
      ///why no no argument
    }
  }

  private void handlePlayerPerm (String[] args, CmdSender sender){
    int len = args.length;
    if (len > 2){

      PermissionManager manager = MunchyMax.getHookManager().permHook.getManager();
      switch (args[2]) {
        case "add":///////////////////////////////////////////////////////
          if (len > 4){
            String playerName = args[3];
            String perm = args[4];
            Player playr;
            if ((playr = Bukkit.getPlayer(playerName)) == null){

              Messenger.send(sender.asSender(), "&aPlayer \"" + playerName + "\" is not online");

            }


            manager.getUser(playr).addPermission(perm);

            Messenger.send(sender.asSender(), "&a Permission \"" + perm + "\" has been granted to player \"" + playr.getName() + "\".");


          } else {
            Messenger.send(sender.asSender(), "&cUsage: &7/rank player perm add <player> <permission>");
          }
          ;
          break;

        case "del":////////////////////////////////////////
          if (len > 4){
            String playerName = args[3];
            String perm = args[4];
            Player playr;
            if ((playr = Bukkit.getPlayer(playerName)) == null){

              Messenger.send(sender.asSender(), "&aPlayer \"&7" + playerName + "&a\" is not online");

            }


            manager.getUser(playr).removePermission(perm);
            Messenger.send(sender.asSender(), "&a Permission \"&7" + perm + "&a\" has been removed from player \"" + playr.getName() + "\".");


          } else {
            Messenger.send(sender.asSender(), "&cUsage: &7/rank player perm del <player> <permission>");
          }
          ;
          break;
        case "check":////

          if (len > 4){
            String playerName = args[3];
            String perm = args[4];
            Player playr;
            if ((playr = Bukkit.getPlayer(playerName)) == null){

              Messenger.send(sender.asSender(), "&aPlayer \"&7" + playerName + "&a\" is not online");

            }


            boolean has = manager.getUser(playr).has(perm);

            Messenger.send(sender.asSender(),
              has ? "&a Permission \"&7" + perm + "&a\" is owned by player \"&7" + playr.getName() + "&a\"." :
                "&c Permission \"&7" + perm + "&c\" is not owned by player \"&7" + playr.getName() + "&a\".");
          } else {
            Messenger.send(sender.asSender(), "&cUsage: &7/rank player perm check <player> <permission>");
          }
          break;


      }
    } else {
      ///why no no argument
    }
  }


  @Override
  public void clean (){

  }
}
