package me.varmetek.munchymc.commands;

import me.varmetek.core.commands.CmdCommand;
import me.varmetek.core.service.Element;
import me.varmetek.core.util.Cleanable;
import me.varmetek.core.util.Messenger;
import me.varmetek.munchymc.Main;
import me.varmetek.munchymc.backend.User;
import me.varmetek.munchymc.backend.kit.Kit;
import me.varmetek.munchymc.backend.kit.KitHandler;
import me.varmetek.munchymc.util.Utils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.io.IOException;
import java.util.Arrays;

/**
 * Created by XDMAN500 on 1/16/2017.
 */
public class CommandKit implements Element, Cleanable
{
  private Main main;
  private KitHandler kitHandler;
  private static final String prefMal = "&4&l> &r";
  private static final String prefBen = "&2&l> &r";
  private final CmdCommand[] commands;
  public CommandKit (Main main){
    this.main = main;
    kitHandler = main.getKitHandler();

    commands = new CmdCommand[]{
      new CmdCommand.Builder("kit", (sender, alias, args, length) -> {


        Player pl;
        User user;

        if (sender.isPlayer()){
          pl = sender.asPlayer();
          user = main.getUserHandler().getUser(pl);
        } else {
          Messenger.send(sender.asSender(), prefBen + "&aCommand is only for players");
          return;
        }

        if (length == 0){
          pl.performCommand("kit list");
        } else {
          switch (args[0]) {
            case "save":
              if (length == 2){
                String name = args[1];
                if (kitHandler.isRegistered(name)){
                  Messenger.send(pl, prefBen + "&aOverwriting kit " + name);
                  kitHandler.unregister(name);
                }

                Kit k = null;

                if (kitHandler.isRegistered(name)){


                  Messenger.send(pl, prefMal + "&cKit is already registered");
                  return;
                } else {
                  kitHandler.register(k = new Kit.Builder(name, pl).build());
                }

                try {
                  main.getDataManager().asKitData().saveKit(k);
                } catch (IOException e) {
                  e.printStackTrace();
                  Messenger.send(pl, prefMal + "&cError saving kit");
                }
                Messenger.send(pl, prefBen + "&aKit " + name + " has been set");

              } else {
                Messenger.send(pl, prefMal + "&a/Kit save <name>");
              }
              break;

            case "load":
              if (length == 2){
                String name = args[1];
                if (kitHandler.isRegistered(name)){
                  Messenger.send(pl, prefBen + "&aLoading kit " + name);
                  kitHandler.get(name).get().apply(pl);
                } else {
                  Messenger.send(pl, prefMal + "&cKit " + name + " doesn't exist");

                }

              } else {
                Messenger.send(pl, prefMal + "&c/Kit load <name>");
              }
              break;
            case "list":
              if (length == 1){
                Messenger.send(pl,
                  "&bListing kits",
                  "&b============",
                  " ");
                for (Kit k : main.getKitHandler().values()) {

                  pl.spigot().sendMessage(forKit(k.ID()));
                }

                Messenger.send(pl,
                  " ",
                  "&b============");
              } else {
                Messenger.send(pl, "/Kit list");
              }
              break;
            case "remove":
              if (length == 2){
                String name = args[1];
                if (kitHandler.isRegistered(name)){
                  Messenger.send(pl, "Removing kit " + name);
                  kitHandler.unregister(name);
                  try {
                    main.getDataManager().asKitData().removeKit(name);
                  } catch (IOException e) {
                    e.printStackTrace();
                    Messenger.send(pl, "&cError removing kit");
                  }
                } else {
                  Messenger.send(pl, "Kit " + name + " doesn't exist");
                }

              } else {
                Messenger.send(pl, "/Kit remove <name>");
              }
              break;
            case "help":
              Messenger.send(pl,
                " ",
                "&6Help Menu");

              pl.spigot().sendMessage(forHelpCommand("  /kit save <kit>", "/kit save"));
              pl.spigot().sendMessage(forHelpCommand("  /kit load <kit>", "/kit load"));
              pl.spigot().sendMessage(forHelpCommand("  /kit remove <kit>", "/kit remove"));
              pl.spigot().sendMessage(forHelpCommand("  /kit list", "/kit list"));
              pl.spigot().sendMessage(forHelpCommand("  /kit clear", "/kit clear"));
              pl.spigot().sendMessage(forHelpCommand("  /kit help", "/kit help"));

              Messenger.send(pl, " ");
              break;


            default:
              Messenger.send(pl, "&cUnknown command");
          }
        }

      }).setTabLogic((sender, alias, args, length) -> {
        switch (length) {
          case 1:
            return Arrays.asList("save", "load", "remove", "list", "clear", "help");
          case 2:
            switch (args[1]) {
              case "save":
              case "load":
              case "remove":
                return Utils.toStringList(main.getKitHandler().values(), (Kit k) ->
                {

                  return k.ID();
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
             .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/kit load " + id)).create();
  }

  private BaseComponent[] forHelpCommand (String shown, String cmd){
    return new ComponentBuilder(shown)
             .color(ChatColor.GREEN)
             .event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, cmd)).create();
  }


  @Override
  public void clean (){
    main = null;
  }

  @Override
  public CmdCommand[] supplyCmd (){
    return commands;
  }


  @Override
  public Listener supplyListener(){
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