package me.varmetek.munchymc.commands;

import com.google.common.collect.ImmutableList;
import me.varmetek.core.commands.CmdCommand;
import me.varmetek.core.commands.CmdSender;
import me.varmetek.core.service.Element;
import me.varmetek.core.util.Cleanable;
import me.varmetek.core.util.Messenger;
import me.varmetek.munchymc.MunchyMax;
import me.varmetek.munchymc.backend.Kit;
import me.varmetek.munchymc.backend.KitHandler;
import me.varmetek.munchymc.backend.exceptions.ConfigException;
import me.varmetek.munchymc.util.Utils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

/**
 * Created by XDMAN500 on 1/16/2017.
 */
public class CommandKit implements Element, Cleanable
{

  private KitHandler kitHandler;
  private static final String prefMal = "&4&l> &r";
  private static final String prefBen = "&2&l> &r";
  private final CmdCommand[] commands;

  public CommandKit (KitHandler handler){

    kitHandler = handler;

    commands = new CmdCommand[]{
      new CmdCommand.Builder("kit").setLogic(
        (cmd)->{

          CmdSender sender = cmd.getSender();
          int length = cmd.getArguments().size();
          String alias = cmd.getAlias();
          ImmutableList<String> args = cmd.getArguments();

        Player pl;


        if (sender.isPlayer()){
          pl = sender.asPlayer();

        } else {
          Messenger.send(sender.asSender(), prefBen + "&aCommand is only for players");
          return;
        }

        if (length == 0){
          Messenger.send(pl,
            "&bListing kits",
            "&b============",
            " ");
          for (String name : kitHandler.getKits().keySet()) {

            pl.spigot().sendMessage(forKit(name));
          }

          Messenger.send(pl,
            " ",
            "&b============");
        }else{
          String name  = args.get(0);
          if (kitHandler.isKit(name)){
            Messenger.send(pl, prefBen + "&aLoading kit " + name);
            kitHandler.getKit(name).get().apply(pl);
          } else {
            Messenger.send(pl, prefMal + "&cKit " + name + " doesn't exist");

          }
        }
      }).setTab(
        (cmd)->{

          CmdSender sender = cmd.getSender();
          int len = cmd.getArguments().size();
          String alias = cmd.getAlias();
          ImmutableList<String> args = cmd.getArguments();

          if(len == 1){
            return Utils.toStringList(kitHandler.getKits().keySet(), (String s) ->
            {

              return s;
            });
          }else{
            return Collections.EMPTY_LIST;
          }
      }).build(),

      new CmdCommand.Builder("editkit").setLogic(
        (cmd)->{

          CmdSender sender = cmd.getSender();
          int length = cmd.getArguments().size();
          String alias = cmd.getAlias();
          ImmutableList<String> args = cmd.getArguments();


        Player pl;


        if (sender.isPlayer()){
          pl = sender.asPlayer();

        } else {
          Messenger.send(sender.asSender(), prefBen + "&aCommand is only for players");
          return;
        }



        if (length == 0){
          pl.performCommand("editkit help");
        } else {
          switch (args.get(0).toLowerCase()) {
            case "save": {
              if (length == 2){

                String name = args.get(1);

                if (kitHandler.isKit(name)){
                  Messenger.send(pl, prefBen + "&aOverwriting kit " + name);

                } else {
                  Messenger.send(pl, prefBen + "&aKit " + name + " has been set");
                }

                try {
                  Kit k = new Kit.Builder().fromPlayerInv(pl).build();
                  kitHandler.setKit(name, k);
                  MunchyMax.getKitFileManager().saveKit(name);
                } catch (ConfigException e) {
                  e.printStackTrace();
                  Messenger.send(pl, prefMal + "&cError saving kit");
                }


              } else {
                Messenger.send(pl, prefMal + "&a/editKit save <name>");
              }
            }
            break;

            case "load": {
              if (length == 2){
                String name = args.get(1);
                if (kitHandler.isKit(name)){
                  Messenger.send(pl, prefBen + "&aLoading kit " + name);
                  kitHandler.getKit(name).get().apply(pl);
                } else {
                  Messenger.send(pl, prefMal + "&cKit " + name + " doesn't exist");

                }

              } else {
                Messenger.send(pl, prefMal + "&c/editKit load <name>");
              }
            }
            break;
            case "list": {
              if (length == 1){
                Messenger.send(pl,
                  "&bListing kits",
                  "&b============",
                  " ");
                for (String name : kitHandler.getKits().keySet()) {

                  pl.spigot().sendMessage(forKit(name));
                }

                Messenger.send(pl,
                  " ",
                  "&b============");
              } else {
                Messenger.send(pl, "/editKit list");
              }
            }
            break;
            case "remove": {
              if (length <= 1){
                Messenger.send(pl, "/editkit remove <name>");
              }
              String name = args.get(1);
              if (!kitHandler.isKit(name)){
                Messenger.send(pl, "Kit " + name + " doesn't exist");
                return;
              }

              Messenger.send(pl, "&aRemoving kit " + name);

              try {
                MunchyMax.getKitFileManager().removeKit(name);
                kitHandler.delKit(name);
              } catch (IOException e) {
                e.printStackTrace();
                Messenger.send(pl, "&cError removing kit");
              }


            }
            break;
            case "help": {
              Messenger.send(pl,
                " ",
                "&6Help Menu");

              pl.spigot().sendMessage(forHelpCommand("  /editkit save <kit>", "/editkit save"));
              pl.spigot().sendMessage(forHelpCommand("  /editkit load <kit>", "/editkit load"));
              pl.spigot().sendMessage(forHelpCommand("  /editkit remove <kit>", "/editkit remove"));
              pl.spigot().sendMessage(forHelpCommand("  /editkit list", "/editkit list"));
              pl.spigot().sendMessage(forHelpCommand("  /editkit clear", "/editkit clear"));
              pl.spigot().sendMessage(forHelpCommand("  /editkit help", "/editkit help"));
              pl.spigot().sendMessage(forHelpCommand("  /editkit server", "/editkit server"));

              Messenger.send(pl, " ");
            }
            break;
            case "server": {
              if (length <= 1){
                Messenger.send(pl,
                  " ",
                  "&4Usage:",
                  "&c&l  >&7 /editkit server save",
                  "&c&l  >&7 /editkit server load"
                );
              } else {
                switch (args.get(1).toLowerCase()) {
                  case "save": {
                    try {
                      MunchyMax.getKitFileManager().saveKits();
                      Messenger.send(pl, "&A&l> &7All kits have been saved to disk");
                    } catch (Exception e) {
                      Messenger.send(pl, "&c&l> &7An error occured while saving kits");
                      Messenger.send(pl, "&c \"" + e.getMessage() + "\"");
                      e.printStackTrace();
                    }
                  }
                  break;

                  case "load": {
                    try {
                      MunchyMax.getKitFileManager().loadKits();
                      Messenger.send(pl, "&A&l> &7All kits have been loaded from disk");
                    } catch (Exception e) {
                      Messenger.send(pl, "&c&l> &7An error occured while loading kits");
                      Messenger.send(pl, "&c \"" + e.getMessage() + "\"");
                      e.printStackTrace();
                    }
                  }
                  break;

                  default: {
                    pl.performCommand("editkit server");
                  }
                }
              }
            }break;


            default:
              Messenger.send(pl, "&cUnknown command");
          }
        }

      }).setTab(
        (cmd)->{

          CmdSender sender = cmd.getSender();
          int length = cmd.getArguments().size();
          String alias = cmd.getAlias();
          ImmutableList<String> args = cmd.getArguments();

        switch (length) {
          case 1:
            return Arrays.asList("save", "load", "remove", "list", "clear", "help");
          case 2:
            switch (args.get(1).toLowerCase()) {
              case "save":
              case "load":
              case "remove":
                return Utils.toStringList(kitHandler.getKits().keySet(), (String s) ->
                {

                  return s;
                });
            }
          default:
            return null;
        }

      }).build()
    };
  }


  private BaseComponent[] forKit (String id){
    return new ComponentBuilder("  " + id)
             .color(ChatColor.GREEN)
             .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/kit " + id)).create();
  }

  private BaseComponent[] forHelpCommand (String shown, String cmd){
    return new ComponentBuilder(shown)
             .color(ChatColor.GREEN)
             .event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, cmd)).create();
  }


  @Override
  public void clean (){

  }

  @Override
  public CmdCommand[] supplyCmd (){
    return commands;
  }


  @Override
  public Listener supplyListener (){
    return null;
  }
}


/*
  @Override
	public void onCommand(CmdSender sender, String[] args)
	{

		if(sender.isPlayer()){
			Player pl = sender.asPlayer();
			User user = main.getUserHandler().getUser(pl);
			int length =args.length;
			Optional<Messenger> msgOp;
			Messenger msg;

			if((msgOp = user.getMessenger()).isPresent())
			{
				msg = msgOp.get();
			}else
			{
				return;
			}



			if(length == 0){
				msg
						.send("/Kit save <name>")
						.send("/kit load <name>")
						.send("/kit list")
						.send("/kit remove <name>")
						.send("/kit clear")
				;

			}else{
				switch(args[0]){
					case "save":
						if(length == 2 ){
								String name =  args[1];
								if( kitHandler.isRegistered(name)){
									msg.send("Overwriting kit "+ name);
									kitHandler.unregister(name);
								}

							kitHandler.register(new Kit(name,pl.getInventory()));
							msg.send("Kit "+ name + " has been set");

						}else{
							msg
									.send("/Kit save <name>");
						}
						break;
					case "load":
						if(length == 2 ){
							String name =  args[1];
							if(kitHandler.isRegistered(name)){
								msg.send("Loading kit "+ name);
								kitHandler.get(name).get().apply(pl);
							}else{
								msg.send("Kit "+ name + " doesn't exist");
							}



						}else{
							msg
									.send("/Kit load <name>");
						}
						break;
					case "list":
						if(length == 1 ){
						    msg.send("&bListing kits").send("&b============").send(" ");
							for(Kit k : main.getKitHandler().values())
							{

										pl.spigot().sendMessage( forKit(k.ID()));
								//msg.send("&a  "+ k.ID());
							}

							msg.send(" ").send("&b============");
						}else{
							msg
									.send("/Kit load <name>");
						}
						break;
					case "remove":
						if(length == 2 ){
							String name =  args[1];
							if(kitHandler.isRegistered(name)){
								msg.send("Removing kit "+ name);
								kitHandler.unregister(name);
								try
								{
									main.getDataManager().asKitData().removeKit(name);
								}catch(IOException e){
									e.printStackTrace();
								}
							}else{
								msg.send("Kit "+ name + " doesn't exist");
							}



						}else{
							msg
									.send("/Kit remove <name>");
						}
						break;
					case "clear":
						if(length == 1 ){
							kitHandler.applyEmpty(pl);
							msg.send("&7Your kit has been cleared");



						}else{
							msg
									.send("/Kit clear");
						}break;
					default: msg.send("&4Unknown command");
				}
			}
		}else{
			sender.asSender().sendMessage("Command is only for players");
		}
	}
*/