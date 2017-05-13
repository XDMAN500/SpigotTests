package me.varmetek.munchymc.commands;

import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldguard.protection.flags.Flag;
import me.varmetek.core.commands.CmdCommand;
import me.varmetek.core.commands.CmdSender;
import me.varmetek.core.service.Element;
import me.varmetek.core.util.Messenger;
import me.varmetek.munchymc.Main;
import me.varmetek.munchymc.backend.mines.LocalBlock;
import me.varmetek.munchymc.backend.mines.Mine;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created by XDMAN500 on 5/10/2017.
 */
public class CommandMines implements Element
{
  private static final String NEG = "&4&l > &c";
  private static final String POS = "&2&l > &a";

  private Main main;

  public CommandMines(Main plugin){
    main = plugin;
  }

  @Override
  public void clean (){
    main = null;
  }

  @Override
  public CmdCommand[] supplyCmd (){
    return new CmdCommand[]{
      new CmdCommand.Builder("editmine").setLogic((CmdSender sender, String alias, String[] args, int len) ->{
          if(!sender.isPlayer()){
            Messenger.send(sender.asSender(), "&cThis command is for players only");
            return;
          }
          Player pl = sender.asPlayer();
          if(len == 0){
            Messenger.send(pl,
              " ",
              "&6> Help Menu",
              "&8&l  > &l/editmine info <name>",
              "&8&l  > &l/editmine create <name>",
              "&8&l  > &l/editmine remove <name>",
              "&8&l  > &l/editmine reset <name>",
              "&8&l  > &l/editmine setregion <name>",
              "&8&l  > &l/editmine setdelay <name>",
              "&8&l  > &l/editmine setsafty <name>",
              "&8&l  > &l/editmine blocks <name>"
              );
          }else{
            switch (args[0].toLowerCase()){
              case "info":{
                 if(len <= 1){
                   Messenger.send(pl,NEG + "/"+alias + " " + args[0] + " " + "<name>" );

                   }else{
                     Optional<Mine> mine =  main.getMineManager().getMine(args[1]);
                     if(!mine.isPresent()){
                       Messenger.send(pl,NEG +"Mine \"&7"+ args[1] +"&c\" does not exist.");
                       return;
                     }
                   BlockVector
                     max = mine.get().getRegion().getMaximumPoint(),
                     min = mine.get().getRegion().getMinimumPoint();
                   List<LocalBlock> blocks = mine.get().getBlockData();
                   Map<Flag<?>,Object> flags  = mine.get().getRegion().getFlags();


                     Messenger.send(pl,
                       " ",
                       "&a ==[Mine info]==",
                       "&a_________",
                       "&a  Name:&r " + mine.get().getName(),
                       "&a  World:&r "+ mine.get().getWorld(),
                       "&a  Region: &r"
                         + "{" + max.getBlockX() + "," + max.getBlockY() + "," + max.getBlockZ() + "}"
                         + "&7&l -->&r "
                         + "{"+ min.getBlockX() + "," + min.getBlockY() + "," + min.getBlockZ() + "}",
                       "&a Blocks:&r " + (blocks.isEmpty() ? "None" :" ")
                       );
                     if(!blocks.isEmpty()){
                       blocks.forEach(bl -> Messenger.send(pl, "    " + bl.toString()));
                     }
                    Messenger.send(pl,"&a Flags:&r " + (flags.isEmpty() ? "None" :" "));
                    if(!flags.isEmpty()){
                      flags.entrySet().forEach(flag -> Messenger.send(pl, "    " + "\""+flag.getKey().getName() +"\" =>" + flagToString(flag.getValue())));
                    }


                 }
              }break;

              case "create":{
                if(len <=1){
                  Messenger.send(pl, NEG + "/"+alias + " " + args[0] + " " + "<name>" );
                }else{
                  Optional<Mine> mine =  main.getMineManager().getMine(args[1]);
                  if(mine.isPresent()){
                    Messenger.send(pl,NEG +"Mine \"&7"+ args[1] +"&c\" already exists.");
                    return;
                  }

                 main.getHookManager();
                }

              }break;

              case "remove":{

              }break;

              case "reset":{

              }break;

              case "setregion":{

              }break;

              case "setdelay":{

              }break;

              case "setsafty":{

              }break;

              case "blocks":{

              }break;

              default:{
                Messenger.send(pl, "&cUnknown Command");
                pl.performCommand(alias);
              }break;
            }
          }
      }).build()
    };
  }

  private String flagToString(Object val){
    if(val == null){
      return "";
    }
    if((val instanceof Enum)){
      return ((Enum)val).name();
    }

    return val.toString();

  }

  @Override
  public Listener supplyListener (){
    return null;
  }
}
