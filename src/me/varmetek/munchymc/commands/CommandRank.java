package me.varmetek.munchymc.commands;

import me.varmetek.core.commands.CmdCommand;
import me.varmetek.core.commands.CmdSender;
import me.varmetek.core.service.Element;
import me.varmetek.core.util.Messenger;
import me.varmetek.munchymc.Main;
import me.varmetek.munchymc.backend.User;
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
  private Main main;
  private final CmdCommand[] commands;



  public CommandRank (Main main){
    this.main = main;

    commands = new CmdCommand[]{
      new CmdCommand.Builder("rank", (sender, alias, args, length) -> {
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
        User user;

        if (isPlayer){
          player = sender.asPlayer();
          user = main.getUserHandler().getUser(player);


        } else {
          sender.asSender().sendMessage("Running Command as non-player");
          return;
        }

        if (length == 0){
          if (isPlayer){
            Messenger.send(sender.asSender(),
              "Usage: &a/" + alias + "player",
              "Usage: &a/" + alias + "group");


          }
        } else {
          switch (args[0]) {
            case "p":
            case "player"://////////////////////////////////////////////////////////
              if (length > 1){
                switch (args[1]) {
                  case "p":
                  case "perm":////////////////////////////////////////////
                    handlePlayerPerm(args, sender);


                    break;
                  case "group":////////////////////////////////////////////////////////////////////

                    break;
                }
              } else {

                Messenger.send(sender.asSender(),
                  "&7",
                  "&7Command: &a/" + alias + "  player",
                  "&7 /... perm ",
                  "&7 /... group");

              }
              break;
          }
        }
      }).build()

    };
  }


  @Override
  public CmdCommand[] supplyCmd (){
    return commands;
  }

  @Override
  public Listener supplyListener (){
    return null;
  }

  private void handlePlayerGroup (String[] args, CmdSender sender, String alias){
    int len = args.length;
    if (len > 2){

      PermissionManager manager = main.getHookManager().permHook.getManager();
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

      PermissionManager manager = main.getHookManager().permHook.getManager();
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

              Messenger.send(sender.asSender(), "&aPlayer \"" + playerName + "\" is not online");

            }


            manager.getUser(playr).removePermission(perm);
            Messenger.send(sender.asSender(), "&a Permission \"" + perm + "\" has been removed from player \"" + playr.getName() + "\".");


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

              Messenger.send(sender.asSender(), "&aPlayer \"" + playerName + "\" is not online");

            }


            boolean has = manager.getUser(playr).has(perm);

            Messenger.send(sender.asSender(),
              has ? "&a Permission \"" + perm + "\" is owned by player \"" + playr.getName() + "\"." :
                "&c Permission \"" + perm + "\" is not owned by player \"" + playr.getName() + "\".");
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
    main = null;
  }
}
