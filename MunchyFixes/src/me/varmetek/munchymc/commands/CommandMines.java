package me.varmetek.munchymc.commands;

import com.google.common.collect.ImmutableList;
import com.sk89q.worldedit.bukkit.BukkitUtil;
import com.sk89q.worldedit.bukkit.selections.Selection;
import me.varmetek.core.commands.CmdCommand;
import me.varmetek.core.commands.CmdLogic;
import me.varmetek.core.commands.CmdSender;
import me.varmetek.core.commands.CmdTab;
import me.varmetek.core.service.Element;
import me.varmetek.core.util.Messenger;
import me.varmetek.munchymc.MunchyMax;
import me.varmetek.munchymc.mines.BlockData;
import me.varmetek.munchymc.mines.Mine;
import me.varmetek.munchymc.mines.MineManager;
import me.varmetek.munchymc.util.BlockLoc;
import me.varmetek.munchymc.util.Utils;
import org.apache.commons.lang3.Validate;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.material.MaterialData;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by XDMAN500 on 5/10/2017.
 */
public class CommandMines implements Element
{
  private static final String NEG = "&4&l > &c";
  private static final String POS = "&2&l > &a";


  private MineManager mines;
  public CommandMines(MineManager mines){
    this.mines = mines;

  }

  public static class MaterialParseException extends IllegalArgumentException{


    public MaterialParseException() {
      super();
    }

    public MaterialParseException(String message) {
      super(message);
    }


    public MaterialParseException(String message, Throwable cause) {
      super(message, cause);
    }

    public MaterialParseException(Throwable cause) {
      super(cause);
    }

}

  @Override
  public void clean (){

  }


  private static final DecimalFormat blockFormat = new DecimalFormat("##0.0##%");
  @Override
  public CmdCommand[] supplyCmd (){
    return new CmdCommand[]{
      new CmdCommand.Builder("mine").setLogic(
        (cmd)->{

          CmdSender sender = cmd.getSender();
          int len = cmd.getArguments().size();
          String alias = cmd.getAlias();
          ImmutableList<String> args = cmd.getArguments();
          if (!sender.isPlayer()){
            Messenger.send(sender.asSender(), "&cThis command is for players only");
            return;
          }

          Player pl = sender.asPlayer();
          if (len == 0){
            Messenger.send(pl,
              " ",
              "&6> Help Menu",
              "&8&l  > &7" + CmdLogic.getUsage(alias, args, 0, "list"),
              "&8&l  > &7" + CmdLogic.getUsage(alias, args, 0, "save"),
              "&8&l  > &7" + CmdLogic.getUsage(alias, args, 0, "load")
            );
            return;
          }

          switch (args.get(0).toLowerCase()) {
            default: {
              Messenger.send(pl,
                "&cUsage",
                "&8&l  > &7" + CmdLogic.getUsage(alias, args, 0, "list"),
                "&8&l  > &7" + CmdLogic.getUsage(alias, args, 0, "save"),
                "&8&l  > &7" + CmdLogic.getUsage(alias, args, 0, "load")
              );


            }
            break;

            case "list": {


              Messenger.send(pl,
                " ",
                "&a ==[Mine list]=="
              );
              Set<String> worldNames = mines.getMineNames();
              if (worldNames.isEmpty()){
                Messenger.send(pl, NEG + "Empty");
              } else {
                for (String name : worldNames) {
                  Messenger.send(pl, "&8&l  >&a " + name);
                }

              }

            }break;

            case "reload":{

              MunchyMax.getMineFileManager().loadAll();
              Messenger.send(pl, "&7 Reloaded mines");


            }break;

            case "save":{

              MunchyMax.getMineFileManager().saveAll();
              Messenger.send(pl, "&7 Saved mines");
            }break;

          }
        }
      ).setTab(
        (cmd)->{

          CmdSender sender = cmd.getSender();
          int len = cmd.getArguments().size();
          String alias = cmd.getAlias();
          ImmutableList<String> args = cmd.getArguments();
          if(len == 0){
            return CmdTab.send("list","save","load");
          }
          return CmdTab.send();
        }).build(),

///////////////////////////////////////////////////////////////////////////////////
      new CmdCommand.Builder("editmine")
        .setLogic(
          (cmd)->{

            CmdSender sender = cmd.getSender();
            int len = cmd.getArguments().size();
            String alias = cmd.getAlias();
            ImmutableList<String> args = cmd.getArguments();

            if (!sender.isPlayer()){
              Messenger.send(sender.asSender(), "&cThis command is for players only");
              return;
            }
            Player pl = sender.asPlayer();
            if (len == 0){
              String prefix = "&8&l  > &7";
              Messenger.send(pl,
                " ",
                "&6> Help Menu",
                prefix + CmdLogic.getUsage(alias,args,0,"<name> info"),
                prefix + CmdLogic.getUsage(alias,args,0,"<name> create"),
                prefix + CmdLogic.getUsage(alias,args,0,"<name> remove"),
                prefix + CmdLogic.getUsage(alias,args,0,"<name> reset"),
                prefix + CmdLogic.getUsage(alias,args,0,"<name> setregion"),
                prefix + CmdLogic.getUsage(alias,args,0,"<name> setdelay"),
                prefix + CmdLogic.getUsage(alias,args,0,"<name> setsafe"),
                prefix + CmdLogic.getUsage(alias,args,0,"<name> setdelay"),
                prefix + CmdLogic.getUsage(alias,args,0,"<name> blocks"),
                prefix + CmdLogic.getUsage(alias,args,0,"<name> copy <mine>"),
                prefix + CmdLogic.getUsage(alias,args,0,"<name> clean"),
                prefix + CmdLogic.getUsage(alias,args,0,"<name> resettype <type>"),
                " "

              );
              return;
            }

            Entry entry;
              try {
               entry  = new Entry(args.get(0));
              }catch(IllegalArgumentException ex){
                Messenger.send(pl,NEG+ "Invalid name");
                return;
              }
            //Optional<Mine> mine = mines.getMine(pl.getWorld(),args[0]);

            if(len == 1){
              String prefix = "&8&l  > &7";
              Messenger.send(pl,
                " ",
                "&6> Help Menu",
                prefix + CmdLogic.getUsage(alias,args,1,"info"),
                prefix + CmdLogic.getUsage(alias,args,1,"create"),
                prefix + CmdLogic.getUsage(alias,args,1,"remove"),
                prefix + CmdLogic.getUsage(alias,args,1,"reset"),
                prefix + CmdLogic.getUsage(alias,args,1,"setregion"),
                prefix + CmdLogic.getUsage(alias,args,1,"setdelay"),
                prefix + CmdLogic.getUsage(alias,args,1,"setsafe"),
                prefix + CmdLogic.getUsage(alias,args,1,"setdelay"),
                prefix + CmdLogic.getUsage(alias,args,1,"blocks"),
                prefix + CmdLogic.getUsage(alias,args,1,"copy <mine>"),
                prefix + CmdLogic.getUsage(alias,args,1,"clean"),
                prefix + CmdLogic.getUsage(alias,args,1,"resettype <type>"),
                " "

              );
              return;
            }

            switch (args.get(1).toLowerCase()){
              default:{
                String prefix = "&8&l  > &7";
                Messenger.send(pl,
                  " ",
                  "&6> Help Menu",
                  prefix + CmdLogic.getUsage(alias,args,1,"info"),
                  prefix + CmdLogic.getUsage(alias,args,1,"create"),
                  prefix + CmdLogic.getUsage(alias,args,1,"remove"),
                  prefix + CmdLogic.getUsage(alias,args,1,"reset"),
                  prefix + CmdLogic.getUsage(alias,args,1,"setregion"),
                  prefix + CmdLogic.getUsage(alias,args,1,"setdelay"),
                  prefix + CmdLogic.getUsage(alias,args,1,"setsafe"),
                  prefix + CmdLogic.getUsage(alias,args,1,"setdelay"),
                  prefix + CmdLogic.getUsage(alias,args,1,"blocks"),
                  prefix + CmdLogic.getUsage(alias,args,1,"copy <mine>"),
                  prefix + CmdLogic.getUsage(alias,args,1,"clean"),
                  prefix + CmdLogic.getUsage(alias,args,1,"resettype <type>"),
                  " "

                );
              }break;


              case "info":{
                DecimalFormat percentage = new DecimalFormat("0.0#%");
                DecimalFormat number = new DecimalFormat();
                number.setGroupingSize(3);
                number.setGroupingUsed(true);


                  if(!entry.mine.isPresent()){
                    Messenger.send(pl,NEG +"Mine \"&7"+ entry.mineName +"&c\" does not exist.");
                    return;
                  }

                  BlockLoc
                    max = entry.mine.get().getMaxPoint(),
                    min = entry.mine.get().getMinPoint();
                  List<BlockData> blocks = entry.mine.get().getBlockData();
                //  Map<Flag<?>,Object> flags  = mine.get().getRegion().getFlags();


                  Messenger.send(pl,
                    " ",
                    "&a ==[Mine info]==",
                    "&a  Name:&r " + entry.mineName,
                    "&a  World:&r "+ entry.mine.get().getWorld().getName(),
                    "&a % Filled:&r " + percentage.format(entry.mine.get().getComposition()),
                    "&a # Blocks Filled:&r "+ number.format(entry.mine.get().getFilledBlocks()),
                    "&a # Blocks Empty:&r "+ number.format(entry.mine.get().getEmptyBlocks()),
                    "&a Till Reset:&r "+ number.format(entry.mine.get().timeTillReset()) + " sec",
                    "&a Reset Delay:&r "+ number.format(entry.mine.get().getResetDelay()) + " sec",


                    //"&a  Generator:&r "+ mine.get().getGenOption(),
                    "&a  Region: &r"
                      + "{" + max.getX() + "," + max.getY() + "," + max.getZ() + "}"
                      + "&7&l -->&r "
                      + "{"+ min.getX() + "," + min.getY() + "," + min.getZ() + "}",
                    "&a  Blocks:&r " + (blocks.isEmpty() ? "None" :" ")
                  );


                  if(!blocks.isEmpty()){
                    blocks.forEach(bl -> Messenger.send(pl, "    " + bl.toString()));
                  }

                //Deprecated
                 /* Messenger.send(pl,"&a  Flags:&r " + (flags.isEmpty() ? "None" :" "));

                  if(!flags.isEmpty()){
                    flags.entrySet().forEach(
                      flag ->
                        Messenger.send(pl, "    " + "\""+flag.getKey().getName() +"\" =>" + flagToString(flag.getValue())));
                  }*/


                }break;


              case "create":{


                if(entry.mine.isPresent()){
                  Messenger.send(pl,NEG +"Mine \"&7"+ entry.mineName +"&c\" already exist.");
                  return;
                }

                Optional<Selection> sel = MunchyMax.getHookManager().weHook.getSelection(pl);
                if(sel.isPresent()){
                  Mine min = mines.create(entry.mineName,
                    BukkitUtil.toVector(sel.get().getMaximumPoint()).toBlockVector(),
                    BukkitUtil.toVector(sel.get().getMinimumPoint()).toBlockVector(),
                    pl.getWorld());
                  Messenger.send(pl,POS +" Mine "+ inMarks("&a",min.getName())+" has been created.");
                }else{
                  Messenger.send(pl,NEG + "A selection is needed to set the region! Use //wand");
                }


              }break;

              case "remove":{
                if (!entry.mine.isPresent()){
                  Messenger.send(pl, NEG + "Mine "+ inMarks("&c",entry.mineName)+" does not exist.");
                  return;
                }

                Messenger.send(pl, POS+ "Mine "+inMarks("&a",entry.mineName)+" has been removed.");
                mines.remove(entry.mineName);


              }break;

              case "reset":{

                if (!entry.mine.isPresent()){
                  Messenger.send(pl, NEG + "Mine " + inMarks("&c",entry.mineName) + " does not exist.");
                  return;
                }
                Messenger.send(pl, POS+ "Mine "+inMarks("&a",entry.mineName)+" has reset.");
                entry.mine.get().resetMine();

              }break;

              case "clean":{

                if (!entry.mine.isPresent()){
                  Messenger.send(pl, NEG + "Mine " + inMarks("&c",entry.mineName) + " does not exist.");
                  return;
                }
                Messenger.send(pl, POS+ "Mine "+inMarks("&a",entry.mineName)+" has been cleaned.");
                entry.mine.get().clearMine();

              }break;

              case "setregion":{

                if(!entry.mine.isPresent()){
                  Messenger.send(pl,NEG +"Mine "+inMarks("&c",entry.mineName)+" does not exist.");
                  return;
                }

                Optional<Selection> sel = MunchyMax.getHookManager().weHook.getSelection(pl);
                if(sel.isPresent()){
                  entry.mine.get().setRegion(
                    BukkitUtil.toVector(sel.get().getMaximumPoint()).toBlockVector(),
                    BukkitUtil.toVector(sel.get().getMinimumPoint()).toBlockVector()
                  );

                  Messenger.send(pl,POS + "Mine "+ inMarks("&a",entry.mineName)+"'s region has been set.");
                }else{
                  Messenger.send(pl,NEG + "A selection is needed to set the region! Use //wand");
                }

              }break;

              case "setdelay":{
                if (!entry.mine.isPresent()){
                  Messenger.send(pl, NEG + "Mine " + inMarks("&c",entry.mineName)+ " does not exist.");
                  return;
                }

                if(len <=2){
                  Messenger.send(pl,
                    "&c Usage: ","&c"+CmdLogic.getUsage(alias,args,2,"[number]"));
                  return;
                }

                Integer delay = Utils.getInt(args.get(2));
                if(delay == null){
                  Messenger.send(pl,
                    NEG+"Invalid Number Input "+ args.get(2));
                  return;
                }
                Messenger.send(pl,
                  POS+ "Mine "+inMarks("&a",entry.mineName)+" now has a reset time of \""+ delay.intValue()+"\" seconds.");
                entry.mine.get().setResetDelay(delay);





              }break;

              case "setsafe":{


                  if (!entry.mine.isPresent()){
                    Messenger.send(pl, NEG + "Mine " + inMarks("&c",entry.mineName)+ " does not exist.");
                    return;
                  }


                  Messenger.send(pl,
                    POS+ "Mine "+inMarks("&a",entry.mineName)+" now has a safety spot.");
                  entry.mine.get().setSafeSpot(pl.getLocation());


              }break;
              case "copy":{



                if (!entry.mine.isPresent()){
                  Messenger.send(pl, NEG + "Mine " + inMarks("&c",entry.mineName)+ " does not exist.");
                  return;
                }
                if(len <=2){
                  Messenger.send(pl, NEG + CmdLogic.getUsage(alias,args,2, "<mine>"));
                  return;
                }




                Entry other;
                try {
                  other = new Entry(args.get(2));
                }catch(IllegalArgumentException ex){
                  Messenger.send(pl,NEG+ "Invalid name");
                  return;
                }


                if (!other.mine.isPresent()){
                  Messenger.send(pl, NEG + "Mine " + inMarks("&c",other.mineName)+ " does not exist.");
                  return;
                }

                entry.mine.get().copy(other.mine.get());
                Messenger.send(pl, POS+ "Mine " + inMarks("&a",entry.mineName) + " has copied mine "+ inMarks("&a",other.mineName)+".");

              }break;
              case "resettype":{
                if (!entry.mine.isPresent()){
                  Messenger.send(pl, NEG + "Mine " + inMarks("&c",entry.mineName)+ " does not exist.");
                  return;
                }
                if(len <=2){
                  Messenger.send(pl, NEG + CmdLogic.getUsage(alias,args,2, "<type>"));
                  return;
                }

                Mine.ResetType type =  Mine.ResetType.valueOf(args.get(2).toUpperCase());
                if(type == null){
                  Messenger.send(pl, NEG + "Type" + inMarks("&c",args.get(2))+ " does not exist.");
                  return;
                }


                entry.mine.get().setResetType(type);
                Messenger.send(pl, POS + "ResetType has been set to " + inMarks("&a",type.name())+ " .");
              }break;
              case "blocks":{

                handleBlocksCmd(pl,entry,sender,alias,args,len);
              }break;
              }

          }

        ).setTab(
        (cmd)->{

          CmdSender sender = cmd.getSender();
          int len = cmd.getArguments().size();
          String alias = cmd.getAlias();
          ImmutableList<String> args = cmd.getArguments();

            if (!sender.isPlayer()){
              return CmdTab.send();
            }
            Player pl = sender.asPlayer();


            if(len == 1){
              return Utils.matchString(args.get(0),Utils.Set2List(mines.getMineNames()));

            }
            if(len == 2){
              return Utils.matchString(args.get(1),
                CmdTab.send("info", "create","remove", "reset","setregion","setdelay","blocks","copy","clean","resettype"));

            }
            switch (args.get(1).toLowerCase()){

              case "blocks":{

                if(len == 3){return CmdTab.send("set","remove","copy","swap");}
                if(len == 4 && args.get(2).equalsIgnoreCase("copy") ){
                  return Utils.matchString(args.get(3),Utils.Set2List(mines.getMineNames()));}
              }break;
              case "copy":{
                if(len == 3){return Utils.matchString(args.get(2),Utils.Set2List(mines.getMineNames()));}
              }break;

              case "resettype":{
                if (len == 3){
                  return Utils.matchString(args.get(2),
                    Utils.toStringList(Mine.ResetType.values(), (t) -> {
                      return t.name();
                    }));
                }
              }
            }
       return CmdTab.send();
      }).build()
    };
  }

  private static String flagToString(Object val){
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

  private static Pattern validateRegex = Pattern.compile("\\d+(:\\d+)?");

  private static MaterialData parseBlock(String input) throws MaterialParseException{
    try {
    int id = 0;
    byte data = 0;
    Validate.isTrue(validateRegex.matcher(input).matches(), "Invalid Material format");
    String[] args = input.split(":");

      id = Integer.parseInt(args[0]);
      if(args.length> 1){
        data = Byte.parseByte(args[1]);
      }

    MaterialData md = new MaterialData(id,data);
    Validate.isTrue(isValid(md),"Material is unsupported");
    return md;
    }catch(NumberFormatException expected){
      throw new MaterialParseException( "Material format Error",expected);
    }

  }

  private static boolean isValid(MaterialData data){
    return data.getItemType() != null;
  }

  private static String  dataString(MaterialData mat){
    return mat.getItemType().name() + "("+mat.getItemTypeId()+":"+mat.getData()+")";
  }



  private void handleBlocksCmd(Player pl, Entry entry,CmdSender sender,String alias, List<String> args, int len){

    //editmine <mine> blocks
    //editmine <mine> blocks add item wi
    if(!entry.mine.isPresent()){
      Messenger.send(pl,NEG +"Mine "+inMarks("&c",entry.mineName)+" does not exist.");
      return;
    }


    if(len <= 2){
      String prefix = "&a   > ";
      Messenger.send(pl,
        " ",
        "&6Help Menu",
        prefix + CmdLogic.getUsage(alias,args,2,"set <item(:data)> <weight>"),
        prefix + CmdLogic.getUsage(alias,args,2,"remove <item(:data)>"),
        prefix + CmdLogic.getUsage(alias,args,2,"copy <mine>"),
        prefix + CmdLogic.getUsage(alias,args,2,"swap <item(:data)> <item(:data)>"),
        " "

      );
      return;
    }

    switch ( args.get(2).toLowerCase()){
      case "add":
      case "set":{
        if(len <= 4 ){
          Messenger.send(pl, "&a   > " + CmdLogic.getUsage(alias,args,3,"<item(:data)> <weight>"));
          return;
        }

        MaterialData mat = null;

        try{
          mat = parseBlock(args.get(3));

        }catch(MaterialParseException expected){
          Messenger.send(pl,NEG + "Error: "+ expected.getMessage());
          return;
        }


        int weight = 0;
        try{
          weight = Integer.parseInt(args.get(4));
          if(weight < 1){
            Messenger.send(pl,NEG + "Invalid weight. It must be over 0");
            return;
          }
        }catch(NumberFormatException expected){
          Messenger.send(pl,NEG + "Invalid weight. It must be a number");
          return;
        }
        entry.mine.get().getRawBlockData().put(mat,weight);
        double per =(((double)weight)/((double)entry.mine.get().totalWeight()));

        Messenger.send( pl,POS + "Block " + inMarks("&a",dataString(mat)) + " has been set to mine "+ inMarks("&a",entry.mineName) + " with a "+ blockFormat.format(per) +" chance of appearance.'") ;



      }break;

      case "remove":{
        if(len <= 3 ){
          Messenger.send(pl,"&a   > " + CmdLogic.getUsage(alias,args,3,"<item(:data)>"));
          return;
        }
        MaterialData mat = null;

        try{
          mat =parseBlock(args.get(3));

        }catch(MaterialParseException expected){
          //throw new RuntimeException(expected);
          Messenger.send(pl,NEG + "Error: "+ expected.getMessage());
          return;

        }
        if(!entry.mine.get().containsData(mat)){
          Messenger.send(pl,
            NEG + "Block " + inMarks("&c",dataString(mat)) + " does not exist in mine " + inMarks("&c",entry.mineName) + "." );
        }

        entry.mine.get().getRawBlockData().remove( mat);
        Messenger.send( pl,POS + "Block " + inMarks("&a",dataString(mat)) + " has been removed from mine "+ inMarks("&a",entry.mineName));
      }break;

      case "copy":{
        if(len <= 3 ){
          Messenger.send(pl,"&a   > " + CmdLogic.getUsage(alias,args,3,"<mine>"));
          return;
        }

        Entry other;
        try {
          other =   new Entry(args.get(3));
        }catch(IllegalArgumentException ex){
          Messenger.send(pl,NEG+ "Invalid");
          return;
        }
        if (!other.mine.isPresent()){
          Messenger.send(pl, NEG + "Mine \"&7" + other.mineName + "&c\" does not exists.");
          return;
        }

        entry.mine.get().copyBlocks(other.mine.get());
        Messenger.send(pl, POS, "Mine "+ inMarks("&a", entry.mineName) + " has copied its blocks from mine "+ inMarks("&a",other.mineName+"."));
      }break;

      case "swap":{
        if(len <= 3){
          Messenger.send(pl,"&a   > " + CmdLogic.getUsage(alias,args,3,"<item(:data)> <item(:data)>"));
          return;
        }

        MaterialData original, replace;
        try{
          original =parseBlock(args.get(3));

        }catch(MaterialParseException expected){
          //throw new RuntimeException(expected);
          Messenger.send(pl,NEG + "Error: "+ expected.getMessage());
          return;

        }

        if(!entry.mine.get().containsData(original)){
          Messenger.send(pl,
            NEG + "Block " + inMarks("&c",dataString(original)) + " does not exist in mine " + inMarks("&c",entry.mineName) + "." );
          return;
        }

        if(len <= 4){
          Messenger.send(pl,"&a   > " + CmdLogic.getUsage(alias,args,4, "<item(:data)>"));
          return;
        }

        try{
          replace =parseBlock(args.get(4));

        }catch(MaterialParseException expected){
          //throw new RuntimeException(expected);
          Messenger.send(pl,NEG + "Error: "+ expected.getMessage());
          return;

        }

        if(entry.mine.get().containsData(replace)){
          int weightO = entry.mine.get().getDataWeight(original);
          int weightR = entry.mine.get().getDataWeight(replace);
          entry.mine.get().getRawBlockData().put(original,weightR);
          entry.mine.get().getRawBlockData().put(replace,weightO);
        }else{
          int weightO = entry.mine.get().getDataWeight(original);
          entry.mine.get().removeData(original);
          entry.mine.get().setData(replace, weightO);
        }
        Messenger.send(pl, POS + "Block "+ inMarks("&a",dataString(original)) + " has been replaced with block "+ inMarks("&a",dataString(replace)) + " in mine "+ inMarks("&a", entry.mineName) );



      }break;


      default:{
        Messenger.send(pl,NEG + "Incorrect syntax");
        pl.performCommand(alias + " " + args.get(0) + " " + args.get(1));
      }break;


    }

  }

 // private static final Pattern _regex = Pattern.compile(	"^(?:((?:\\w|\\d|[_-])+)(?:[:]))?((?:\\w|\\d|[_-])+)$");
  private static final Pattern regex = Pattern.compile(	"^((?:\\w|-)+)$");

  private class Entry{
    //final World world;
    final Optional<Mine> mine;
    final String  mineName;

    public Entry(final String input){

      Validate.notNull(input);

      Matcher mat = regex.matcher(input);
      Validate.isTrue(mat.matches());


      String rawMine = input;


     /* String rawWorld = mat.group(1);
      world = rawWorld == null ? def :  Bukkit.getWorld(rawWorld);
      Validate.notNull(world,"INVALID WORLD");
      worldName = world.getName();*/

     mine = mines.getMine(rawMine);
      mineName = mine.isPresent() ? mine.get().getName() : rawMine;


    }

  }

  private static String inMarks(String color,String text){
    return  color + "\"&7" + text + color+"\"";
  }
/*
  private static CmdLogic oldEditMine;
  static{
    oldEditMine = (CmdSender sender, String alias, String[] args, int len) ->{
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
          "&8&l  > &l/editmine blocks <name>",
          "&8&l  > &l/editmine copy <mine> <oldmine>",
          "&8&l  > &l/editmine setgen <mine> <option>",
          "&8&l  > &l/editmine list"
        );
        return;
      }
      switch (args[0].toLowerCase()){
        case "list":{
          Messenger.send(pl,
            " ",
            "&a ==[Mine list]==");
          for(String name : MunchyMax.getMineManager().getMineNames()){
            Messenger.send(pl, "&8&l  > " + name);
          }
        }break;
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
              "&a  Name:&r " + mine.get().getName(),
              "&a  World:&r "+ mine.get().getWorld().getName(),
              "&a  Generator:&r "+ mine.get().getGenOption(),
              "&a  Region: &r"
                + "{" + max.getBlockX() + "," + max.getBlockY() + "," + max.getBlockZ() + "}"
                + "&7&l -->&r "
                + "{"+ min.getBlockX() + "," + min.getBlockY() + "," + min.getBlockZ() + "}",
              "&a  Blocks:&r " + (blocks.isEmpty() ? "None" :" ")
            );
            if(!blocks.isEmpty()){
              blocks.forEach(bl -> Messenger.send(pl, "    " + bl.toString()));
            }
            Messenger.send(pl,"&a  Flags:&r " + (flags.isEmpty() ? "None" :" "));
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
        case "copy":{
          if(len <=1){
            Messenger.send(pl, NEG + "/" + alias + " " + args[0] + " " + "<mine>  <oldmine>");
            return;
          }
          Optional<Mine> mineCur = MunchyMax.getMineManager().getMine(args[1]);

          if (!mineCur.isPresent()){
            Messenger.send(pl, NEG + "Mine \"&7" + args[1] + "&c\" does not exists.");
            return;
          }
          if(len <=2){
            Messenger.send(pl, NEG + "/" + alias + " " + args[0] + " " + args[1] + " " + "<oldmine>");
            return;
          }





          Optional<Mine> mineOld = MunchyMax.getMineManager().getMine(args[2]);
          if (!mineOld.isPresent()){
            Messenger.send(pl, NEG + "Mine \"&7" + args[1] + "&c\" does not exists.");
            return;
          }

          mineCur.get().copy(mineOld.get());
          Messenger.send(pl, POS+ "Mine \"&7" + mineCur.get().getName() + "&a\" has copied mine \"&7"+ mineOld.get().getName()+"\"&a.");

        }break;

        case "setgen":{
          if(len <=1){
            Messenger.send(pl, NEG + "/"+alias + " " + args[0] + " " + "<name> <number>" );
            return;
          }


          Optional<Mine> mine = MunchyMax.getMineManager().getMine(args[1]);

          if (!mine.isPresent()){
            Messenger.send(pl, NEG + "Mine \"&7" + args[1] + "&c\" does not exists.");
            return;
          }

          if(len <=2){
            Messenger.send(pl, NEG + "/"+alias + " " + args[0] + " " + mine.get().getName() + " " + "<number>" );
            return;
          }
          int id;
          try{
            id =Integer.parseInt(args[2]);
            mine.get().setGenOption(id);
            Messenger.send(pl, POS + "The generator for mine \"&7"+ mine.get().getName()+"\"&a has been set to option "+mine.get().getGenOption());

          }catch(NumberFormatException expeted){
            Messenger.send(pl, NEG + "\"&7"+args[2]+"&c\" is not recognized as a number." );
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
                "&a   > " + "/" +alias + " " + args[0] + " " + args[1]+ " " + "remove <item:(data)>",
                "&a   > " + "/" +alias + " " + args[0] + " " + args[1]+ " " + "copy <mine>",
                "&a   > " + "/" +alias + " " + args[0] + " " + args[1]+ " " + "swap <item(:data)> <item(:data)>"

              );
            }else{
              switch ( args[2].toLowerCase()){
                case "add":
                case "set":{
                  if(len <= 4 ){
                    Messenger.send(pl, "&a   > " + "/" +alias + " " + args[0] + " " + args[1]+ " " + "add <item:(data)> <weight>");
                    return;
                  }

                  MaterialData mat = null;

                  try{
                    mat = parseBlock(args[3]);

                  }catch(Exception expected){
                    Messenger.send(pl,NEG + "Error: "+ expected.getMessage());
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
                  mine.get().getRawBlockData().put(mat,weight);
                  Messenger.send( pl,POS + "Block " + inMarks("&a",dataString(mat)) + " has been set to mine "+ inMarks("&a",mine.get().getName()) + " with a "+ blockFormat.format((((double)weight)/((double)mine.get().totalWeight()))) +"% chance of appearance.'") ;



                }break;

                case "remove":{
                  if(len <= 3 ){
                    Messenger.send(pl,"&a   > " + "/" +alias + " " + args[0] + " " + args[1]+ " " + "remove <item:(data)>");
                    return;

                  }
                  MaterialData mat = null;

                  try{
                    mat =parseBlock(args[3]);

                  }catch(Exception expected){
                    //throw new RuntimeException(expected);
                    Messenger.send(pl,NEG + "Error: "+ expected.getMessage());
                    return;

                  }
                  if(!mine.get().containsData(mat)){
                    Messenger.send(pl,
                      NEG + "Block " + inMarks("&c",dataString(mat)) + " does not exist in mine " + inMarks("&c",mine.get().getName()) + "." );
                  }

                  mine.get().getRawBlockData().remove( mat);
                  Messenger.send( pl,POS + "Block " + inMarks("&a",dataString(mat)) + " has been removed from mine "+ inMarks("&a",mine.get().getName()));
                }break;

                case "copy":{
                  if(len <= 3 ){
                    Messenger.send(pl,"&a   > " + "/" +alias + " " + args[0] + " " + args[1]+ " " + args[2]+ " " +" <mine>");

                    return;

                  }

                  Optional<Mine> mineL = MunchyMax.getMineManager().getMine(args[3]);
                  if (!mine.isPresent()){
                    Messenger.send(pl, NEG + "Mine \"&7" + args[3] + "&c\" does not exists.");
                    return;
                  }

                  mine.get().copyBlocks(mineL.get());
                  Messenger.send(pl, POS, "Mine "+ inMarks("&a", mine.get().getName()) + " has copied its blocks from mine "+ inMarks("&a",mineL.get().getName()+"."));
                }break;

                case "swap":{
                  if(len <= 3){
                    Messenger.send(pl,"&a   > " + "/" +alias + " " + args[0] + " " + args[1]+ " " + "swap <item(:data) <item(:data)>");
                    return;
                  }

                  MaterialData original, replace;
                  try{
                    original =parseBlock(args[3]);

                  }catch(Exception expected){
                    //throw new RuntimeException(expected);
                    Messenger.send(pl,NEG + "Error: "+ expected.getMessage());
                    return;

                  }

                  if(!mine.get().containsData(original)){
                    Messenger.send(pl,
                      NEG + "Block " + inMarks("&c",dataString(original)) + " does not exist in mine " + inMarks("&c",mine.get().getName()) + "." );
                    return;
                  }

                  if(len <= 4){
                    Messenger.send(pl,"&a   > " + "/" +alias + " " + args[0] + " " + args[1]+ " " + "swap " + dataString(original) + " "+ "<item(:data)>");
                    return;
                  }

                  try{
                    replace =parseBlock(args[4]);

                  }catch(Exception expected){
                    //throw new RuntimeException(expected);
                    Messenger.send(pl,NEG + "Error: "+ expected.getMessage());
                    return;

                  }

                  if(mine.get().containsData(replace)){
                    int weightO = mine.get().getDataWeight(original);
                    int weightR = mine.get().getDataWeight(replace);
                    mine.get().getRawBlockData().put(original,weightR);
                    mine.get().getRawBlockData().put(replace,weightO);
                  }else{
                    int weightO = mine.get().getDataWeight(original);
                    mine.get().removeData(original);
                    mine.get().setData(replace, weightO);
                  }
                  Messenger.send(pl, POS + "Block "+ inMarks("&a",dataString(original)) + " has been replaced with block "+ inMarks("&a",dataString(replace)) + " in mine "+ inMarks("&a", mine.get().getName()) );



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

    };
  }
  */
}
