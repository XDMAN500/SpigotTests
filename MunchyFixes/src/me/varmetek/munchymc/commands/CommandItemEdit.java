package me.varmetek.munchymc.commands;

import me.varmetek.core.commands.CmdCommand;
import me.varmetek.core.commands.CmdLogic;
import me.varmetek.core.commands.CmdTab;
import me.varmetek.core.service.Element;
import me.varmetek.core.util.Messenger;
import me.varmetek.munchymc.util.UtilInventory;
import me.varmetek.munchymc.util.Utils;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Map;

/**
 * Created by XDMAN500 on 6/10/2017.
 */
public class CommandItemEdit implements Element
{
  @Override
  public void clean (){

  }

  @Override
  public CmdCommand[] supplyCmd (){
    return new CmdCommand[]{
      new CmdCommand.Builder("itemedit").setLogic(
        (cmd)->{
          if(!cmd.getSender().isPlayer()){
            Messenger.send(cmd.getSender().asSender(), "&C this command is only for players");
          }

          Player pl = cmd.getSender().asPlayer();
          ItemStack hand = pl.getInventory().getItemInMainHand();
          List<String> args = cmd.getArguments();
          int len = args.size();

          if(UtilInventory.isItemEmpty(hand)){
            Messenger.send(pl, "&c&l >&c An item needs to be in hand.");
            return;
          }

          if(len == 0){


            String prefix = "&a   > ";
            Messenger.send(pl,
              " ",
              "&6Help Menu",
              prefix + CmdLogic.getUsage(cmd.getAlias(),args,0,"enchant"),
              prefix + CmdLogic.getUsage(cmd.getAlias(),args,0,"rename [name]"),
              prefix + CmdLogic.getUsage(cmd.getAlias(),args,0,"damage [damage]"),
              prefix + CmdLogic.getUsage(cmd.getAlias(),args,0,"itemid [id]"),
              prefix + CmdLogic.getUsage(cmd.getAlias(),args,0,"amount [itemid]"),

              " "

            );
            return;
          }

          switch (args.get(0).toLowerCase()) {
            default: {
              String prefix = "&a   > ";
              Messenger.send(pl,
                " ",
                "&6Help Menu",
                prefix + CmdLogic.getUsage(cmd.getAlias(),args,0,"enchant "),
                prefix + CmdLogic.getUsage(cmd.getAlias(),args,0,"rename [name]"),
                prefix + CmdLogic.getUsage(cmd.getAlias(),args,0,"damage [damage]"),
                prefix + CmdLogic.getUsage(cmd.getAlias(),args,0,"itemid [itemid]"),
                prefix + CmdLogic.getUsage(cmd.getAlias(),args,0,"amount [amount]"),
                " "

              );

            }break;
            case "enchant": {

              if (len <= 1){
                String prefix = "&a   > ";
                Messenger.send(pl,
                  " ",
                  "&6Help Menu",
                  prefix + CmdLogic.getUsage(cmd.getAlias(), args, 1, "remove [enchantment]"),
                  prefix + CmdLogic.getUsage(cmd.getAlias(), args, 1, "set [enchantment] [lvl]"),
                  prefix + CmdLogic.getUsage(cmd.getAlias(), args, 1, "clear"),
                  prefix + CmdLogic.getUsage(cmd.getAlias(), args, 1, "list"),

                  " "

                );
                return;
              }

              switch (args.get(1).toLowerCase()) {
                default:{
                  String prefix = "&a   > ";
                  Messenger.send(pl,
                    " ",
                    "&6Help Menu",
                    prefix + CmdLogic.getUsage(cmd.getAlias(), args, 1, "remove [enchantment]"),
                    prefix + CmdLogic.getUsage(cmd.getAlias(), args, 1, "set [enchantment] [lvl]"),
                    prefix + CmdLogic.getUsage(cmd.getAlias(), args, 1, "clear"),
                    prefix + CmdLogic.getUsage(cmd.getAlias(), args, 1, "list"),

                    " "

                  );

                }
                break;
                case "remove": {
                  if (len <= 2){
                    Messenger.send(pl, "&c Usage:",
                      CmdLogic.getUsage(cmd.getAlias(), args, 2, "[enchantment]")
                    );
                    return;
                  }

                  Enchantment ench = Enchantment.getByName(args.get(2));

                  if (ench == null){
                    Messenger.send(pl, "&c Unknown enchantment");
                    return;
                  }

                  hand.removeEnchantment(ench);

                  Messenger.send(pl, "&a Enchantment &7" + ench.getName() + "&a has been removed");

                }
                break;
                case "set": {
                  if (len <= 2){
                    Messenger.send(pl, "&c Usage:",
                      CmdLogic.getUsage(cmd.getAlias(), args, 2, "[enchantment] [level]")
                    );
                    return;
                  }
                  Enchantment ench = Enchantment.getByName(args.get(2).toUpperCase());

                  if (ench == null){
                    Messenger.send(pl, "&c Unknown enchantment");
                    return;
                  }

                  if (len <= 3){
                    Messenger.send(pl, "&c Usage:",
                      CmdLogic.getUsage(cmd.getAlias(), args, 3, "[level]")
                    );
                    return;
                  }

                  Integer level = Utils.getInt(args.get(3));

                  if (level == null){
                    Messenger.send(pl, "&c Invalid number &7" + args.get(3));
                    return;
                  }

                  if (level.intValue() > 0){
                    hand.addUnsafeEnchantment(ench, level);
                    Messenger.send(pl, "&a Enchantment &7" + ench.getName() + "{" + level + "}" + "&a has been added");
                  } else {
                    hand.removeEnchantment(ench);
                    Messenger.send(pl, "&a Enchantment &7" + ench.getName() + "&a has been removed");
                  }


                }
                break;
                case "list": {

                  Map<Enchantment,Integer> enchants = hand.getEnchantments();
                  if (enchants.isEmpty()){
                    Messenger.send(pl, "&c Item has no enchantments");
                  } else {
                    Messenger.send(pl, "&6 Listing Enchantments");
                    enchants.forEach((ench, lvl) -> {
                      Messenger.send(pl, "   &7- " + ench.getName() + " " + lvl.intValue());
                    });
                  }

                }

                break;
                case "clear": {
                  Map<Enchantment,Integer> enchants = hand.getEnchantments();

                  enchants.keySet().forEach((ench) -> {
                      hand.removeEnchantment(ench);
                    }
                  );

                  break;
                }
              }
              break;
            }
            case "rename":{
              if(len <=1){

                  Messenger.send(pl, "&c Usage:",
                    CmdLogic.getUsage(cmd.getAlias(), args, 1, "[name]")
                  );
                  return;

              }

              ItemMeta meta = hand.hasItemMeta() ? hand.getItemMeta() : hand.getItemMeta().clone();

              meta.setDisplayName(Messenger.color(args.get(1)));
              hand.setItemMeta(meta);
              Messenger.send(pl,"&a Item has been renamed to \"&r"+ meta.getDisplayName()+"&a\"");
            }break;

            case "damage":{
              if(len <=1){

                Messenger.send(pl, "&c Usage:",
                  CmdLogic.getUsage(cmd.getAlias(), args, 1, "[damage]")
                );
                return;

              }

              Short dmg = Utils.getShort(args.get(3));

              if (dmg == null){
                Messenger.send(pl, "&c Invalid number &7" + args.get(3));
                return;
              }

              hand.setDurability(dmg.shortValue());
              Messenger.send(pl,"&a Item damage has been set to "+ dmg.shortValue());
            }break;
            case "itemid":{
              if(len <=1){

                Messenger.send(pl, "&c Usage:",
                  CmdLogic.getUsage(cmd.getAlias(), args, 1, "[id]")
                );
                return;

              }

              Material mat = Material.matchMaterial(args.get(1));

              if (mat == null){
                Messenger.send(pl, "&c Invalid input &7" + args.get(3));
                return;
              }

              hand.setType(mat);
              Messenger.send(pl,"&a Item id has been set to "+ mat.name());
            }break;
            case "amount":{
              if(len <=1){

                Messenger.send(pl, "&c Usage:",
                  CmdLogic.getUsage(cmd.getAlias(), args, 1, "[amount]")
                );
                return;

              }

             Integer amount = Utils.getInt(args.get(1));

              if (amount == null){
                Messenger.send(pl, "&c Invalid number &7" + args.get(3));
                return;
              }

              hand.setAmount(amount);
              Messenger.send(pl,"&a Item damage has been set to "+ amount.shortValue());
            }break;


          }
        }
      ) .setTab(
        (cmd)->{
          if(!cmd.getSender().isPlayer()){
            return CmdTab.send();
          }

          Player pl = cmd.getSender().asPlayer();
          ItemStack hand = pl.getInventory().getItemInMainHand();
          List<String> args = cmd.getArguments();
          int len = args.size();

          if(UtilInventory.isItemEmpty(hand)){
            return CmdTab.send();
          }

          if(len == 0){
            return CmdTab.send();
          }

          if (len == 1){
            return Utils.matchString(args.get(0),CmdTab.send("enchant", "amount", "itemid", "damage", "rename"));
          }


       switch (args.get(0).toLowerCase()){



         case "enchant":{
           if(len== 2){
             return Utils.matchString(args.get(1),CmdTab.send("set","remove","list","clear"));
           }

           switch (args.get(1)) {
             default: return CmdTab.send();

             case "remove":
             case "set": {
               if (len == 3){
                return  Utils.matchString(args.get(2),CmdTab.send(
                   Utils.toStringList(Enchantment.values(),
                     (ench) -> {
                       return ench.getName();
                     }
                   ).toArray(new String[0]))
                 );
               }
               break;
             }
           }
         }break;
         case "itemid" : {
           if (len == 2){
             return  Utils.matchString(args.get(1),CmdTab.send(
               Utils.toStringList(Material.values(),
                 (ench) -> {
                   return ench.name();
                 }
               ).toArray(new String[0]))
             );
           }
         }
       }

       return CmdTab.send();
        }
      ).build()
    };
  }

  @Override
  public Listener supplyListener (){
    return null;
  }
}
