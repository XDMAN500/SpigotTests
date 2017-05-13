package me.varmetek.munchymc.commands;

import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.bukkit.BukkitUtil;
import com.sk89q.worldedit.bukkit.selections.Selection;
import com.sk89q.worldguard.protection.flags.Flag;
import me.varmetek.core.commands.CmdCommand;
import me.varmetek.core.commands.CmdSender;
import me.varmetek.core.service.Element;
import me.varmetek.core.util.Messenger;
import me.varmetek.munchymc.MunchyMax;
import me.varmetek.munchymc.backend.mines.LocalBlock;
import me.varmetek.munchymc.backend.mines.Mine;
import org.apache.commons.lang3.Validate;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.material.MaterialData;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * Created by XDMAN500 on 5/10/2017.
 */
public class CommandMines implements Element
{
  private static final String NEG = "&4&l > &c";
  private static final String POS = "&2&l > &a";



  public CommandMines(){

  }

  @Override
  public void clean (){

  }

  private String inMarks(String color,String text){
    return  color + "\"&7" + text + "\""+color;
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
                     Optional<Mine> mine =  MunchyMax.getMineManager().getMine(args[1]);
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
                  Optional<Mine> mine = MunchyMax.getMineManager().getMine(args[1]);
                  if(mine.isPresent()){
                    Messenger.send(pl,NEG +"Mine \"&7"+ args[1] +"&c\" already exists.");
                    return;
                  }

                 Optional<Selection> sel = MunchyMax.getHookManager().weHook.getSelection(pl);
                 if(sel.isPresent()){
                   Mine min =MunchyMax.getMineManager().create(args[1],
                     BukkitUtil.toVector(sel.get().getMaximumPoint()).toBlockVector(),
                     BukkitUtil.toVector(sel.get().getMinimumPoint()).toBlockVector(),
                     pl.getWorld());
                   Messenger.send(pl,POS +" Mine "+ inMarks("&a",min.getName())+" has been created.");
                 }else{
                   Messenger.send(pl,NEG + "A selection is needed to set the region! Use //wand");
                 }
                }

              }break;

              case "remove":{
                if(len <=1){
                  Messenger.send(pl, NEG + "/"+alias + " " + args[0] + " " + "<name>" );
                }else {
                  Optional<Mine> mine = MunchyMax.getMineManager().getMine(args[1]);
                  if (!mine.isPresent()){
                    Messenger.send(pl, NEG + "Mine \"&7" + args[1] + "&c\" does not exists.");
                    return;
                  }
                  Messenger.send(pl, POS+ "Mine "+inMarks("&a",mine.get().getName())+" has been removed.");
                  MunchyMax.getMineManager().remove(mine.get().getName());
                }

              }break;

              case "reset":{
                if(len <=1){
                  Messenger.send(pl, NEG + "/"+alias + " " + args[0] + " " + "<name>" );
                }else {
                  Optional<Mine> mine = MunchyMax.getMineManager().getMine(args[1]);
                  if (!mine.isPresent()){
                    Messenger.send(pl, NEG + "Mine \"&7" + args[1] + "&c\" does not exists.");
                    return;
                  }
                  Messenger.send(pl, POS+ "Mine "+inMarks("&a",mine.get().getName())+" has reset.");
                  mine.get().resetMine();
                }
              }break;

              case "setregion":{
                if(len <=1){
                  Messenger.send(pl, NEG + "/"+alias + " " + args[0] + " " + "<name>" );
                }else{
                  Optional<Mine> mine = MunchyMax.getMineManager().getMine(args[1]);
                  if(!mine.isPresent()){
                    Messenger.send(pl,NEG +"Mine \"&7"+ args[1] +"&c\" does not exists.");
                    return;
                  }

                  Optional<Selection> sel = MunchyMax.getHookManager().weHook.getSelection(pl);
                  if(sel.isPresent()){
                    mine.get().setRegion(
                      BukkitUtil.toVector(sel.get().getMaximumPoint()).toBlockVector(),
                      BukkitUtil.toVector(sel.get().getMinimumPoint()).toBlockVector()
                    );

                    Messenger.send(pl,POS + "Mine "+ inMarks("&a",mine.get().getName())+"'s region has been set.");
                  }else{
                    Messenger.send(pl,NEG + "A selection is needed to set the region! Use //wand");
                  }
                }
              }break;

              case "setdelay":{
                if(len <=1){
                  Messenger.send(pl, NEG + "/"+alias + " " + args[0] + " " + "<amount>" );
                }else {
                  Optional<Mine> mine = MunchyMax.getMineManager().getMine(args[1]);
                  if (!mine.isPresent()){
                    Messenger.send(pl, NEG + "Mine \"&7" + args[1] + "&c\" does not exists.");
                    return;
                  }
                  int amount = 0;

                  try{
                    amount = Integer.parseInt(args[1]);
                    Messenger.send(pl,
                      POS+ "Mine "+inMarks("&a",mine.get().getName())+" now has a reset time of \""+ amount+"\" seconds.");
                    mine.get().setResetDelay(amount);
                  }catch(NumberFormatException expected){
                    Messenger.send(pl,
                      NEG,"Invalid Number Input");
                  }

                  }


              }break;

              case "setsafty":{
                if(len <=1){
                  Messenger.send(pl, NEG + "/"+alias + " " + args[0] + " " + "<name>" );
                }else {
                  Optional<Mine> mine = MunchyMax.getMineManager().getMine(args[1]);
                  if (!mine.isPresent()){
                    Messenger.send(pl, NEG + "Mine \"&7" + args[1] + "&c\" does not exists.");
                    return;
                  }
                  Messenger.send(pl,
                    POS+ "Mine "+inMarks("&a",mine.get().getName())+" now has a safety spot.");
                  mine.get().setSafeSpot(pl.getLocation());

                }
              }break;

              case "blocks":{
                if(len <=1){
                  Messenger.send(pl, NEG + "/"+alias + " " + args[0] + " " + "<name>" );
                }else {
                  Optional<Mine> mine = MunchyMax.getMineManager().getMine(args[1]);
                  if (!mine.isPresent()){
                    Messenger.send(pl, NEG + "Mine \"&7" + args[1] + "&c\" does not exists.");
                    return;
                  }
                  if(len <= 2){
                    Messenger.send(pl,
                       " ",
                      "&6Help Menu",
                      "&a   > " + "/" +alias + " " + args[0] + " " + args[1]+ " " + "add <item:(data)> <weight>",
                      "&a   > " + "/" +alias + " " + args[0] + " " + args[1]+ " " + "remove <item:(data)>"

                      );
                  }else{
                    switch ( args[2].toLowerCase()){
                      case "add":{
                        if(len <= 4 ){
                         Messenger.send(pl, "&a   > " + "/" +alias + " " + args[0] + " " + args[1]+ " " + "add <item:(data)> <weight>");
                         return;
                        }

                        MaterialData mat = null;
                        try{
                          mat = parseBlock(args[3]);
                          Messenger.send( pl,POS + "Block has been removed from mine "+ inMarks("&a",mine.get().getName()));
                        }catch(Exception expected){
                          Messenger.send(pl,NEG + "Incorrect syntax: "+ expected.getMessage());
                          return;
                        }

                        int weight = 0;
                        try{
                          weight = Integer.parseInt(args[4]);
                          if(weight < 1){
                            Messenger.send(pl,NEG + "Invalid weight. It must be over 0");
                            return;
                          }
                        }catch(NumberFormatException expected){
                          Messenger.send(pl,NEG + "Invalid weight. It must be a number");
                          return;
                        }

                        Messenger.send(pl, POS + "Block has been added to the mine");
                        mine.get().getRawBlockData().put(mat,weight);


                      }break;

                      case "remove":{
                          if(len <= 3 ){
                            Messenger.send(pl,"&a   > " + "/" +alias + " " + args[0] + " " + args[1]+ " " + "remove <item:(data)>");
                            return;

                          }
                          try{
                            mine.get().getRawBlockData().remove( parseBlock(args[3]));
                            Messenger.send( pl,POS + "Block has been removed from mine "+ inMarks("&a",mine.get().getName()));
                          }catch(Exception expected){
                            Messenger.send(pl,NEG + "Incorrect syntax: "+ expected.getMessage());

                          }
                      }break;

                      default:{
                        Messenger.send(pl,NEG + "Incorrect syntax");
                        pl.performCommand(alias + " " + args[0] + " " + args[1]);
                      }break;


                    }
                  }
                }
              }break;

              default:{
                Messenger.send(pl, "&cUnknown Sub-Command");
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

  private static Pattern validateRegex = Pattern.compile("^\\d+:(\\.\\d+)?");
  private static MaterialData parseBlock(String input){
    int id = 0;
    byte data = 0;
    Validate.isTrue(validateRegex.matcher(input).matches(), "Invalid Material format");
    String[] args = input.split(":?");
    try {
      id = Integer.parseInt(args[0]);
      if(args.length> 1){
        data = Byte.parseByte(args[1]);
      }
    }catch(NumberFormatException expected){
      throw new IllegalArgumentException( "Invalid Material format");
    }
    return new MaterialData(id,data);




  }

}
