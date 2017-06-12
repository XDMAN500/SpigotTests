package me.varmetek.munchymc.commands;

import com.google.common.collect.ImmutableList;
import me.varmetek.core.commands.CmdCommand;
import me.varmetek.core.commands.CmdSender;
import me.varmetek.core.service.Element;
import me.varmetek.core.util.Messenger;
import me.varmetek.munchymc.MunchyMax;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by XDMAN500 on 5/7/2017.
 */
public class CommandCheckInv implements Element
{

  private Map<UUID,Inventory> targets = new HashMap<>();
  private static final ItemStack AIR = new ItemStack(Material.AIR);
  public CommandCheckInv(){
  }
  @Override
  public void clean (){

  }

  @Override
  public CmdCommand[] supplyCmd (){
    return new CmdCommand[]{
      new CmdCommand.Builder("checkinv")
        .setLogic(
          (cmd)->{

          CmdSender sender = cmd.getSender();
          int len = cmd.getArguments().size();
          String alias = cmd.getAlias();
          ImmutableList<String> args = cmd.getArguments();

          if(!sender.isPlayer()){
            Messenger.send(sender.asSender(), "&c This command is only executable by players.");
            return;
          }

          Player pl = sender.asPlayer();
          if(len == 0){
            Messenger.send(pl,"&c Usage: /checkinv <player>");
            return;
          }

        String name = args.get(0);
        Player target = Bukkit.getPlayer(name);
        if(target == null){
          Messenger.send(pl,"&c Player \""+ name +"\" is not online.");
          return;
        }

        name = target.getName();

          workInv(target);

        pl.openInventory(targets.get(target.getUniqueId()));
        Messenger.send(pl,"&a Viewing Inventory of \""+ name +"\".");


        })
        .build()
    };
  }

  public void clearInv(Player target){
    if(!targets.containsKey(target.getUniqueId())) return;

    Inventory inv = targets.get(target.getUniqueId());

    for(HumanEntity he: inv.getViewers()){
      he.closeInventory();
    }
    targets.remove(target.getUniqueId());
    inv.clear();
  }

  public void workInv(Player target){
    UUID id = target.getUniqueId();
    if(!targets.containsKey(id)){
      targets.put(id,Bukkit.createInventory(target,9*6));
    }
    Inventory inv =  targets.get(id) == null ? Bukkit.createInventory(null,9*6) : targets.get(id) ;
    ItemStack[][] rows = new ItemStack[4][9];
    for(int slot = 9; slot<18; slot++){
      rows[0][slot % 9] = target.getInventory().getItem(slot);

    }

    for(int slot = 18; slot<27; slot++){
      rows[1][slot % 9] = target.getInventory().getItem(slot);

    }

    for(int slot = 27; slot<36; slot++){
      rows[2][slot % 9] = target.getInventory().getItem(slot);

    }

    for(int slot = 0; slot<9; slot++){
      rows[3][slot % 9] = target.getInventory().getItem(slot);

    }

    ItemStack[] armor = target.getInventory().getArmorContents();
    ItemStack offHand = target.getInventory().getItemInOffHand();
    ItemStack selected = new ItemStack(Material.NETHER_STAR);

    for(int i = 0; i<9; i++){
      inv.setItem(i, rows[0][i % 9]);
    }

    for(int i = 9; i<18; i++){
      inv.setItem(i, rows[1][i % 9]);
    }

    for(int i = 18; i<27; i++){
      inv.setItem(i, rows[2][i % 9]);
    }

    for(int i = 27; i<36; i++){
      inv.setItem(i, rows[3][i % 9]);
    }
    for(int i = 36; i<45; i++){
      inv.setItem(i , i % 9 == target.getInventory().getHeldItemSlot() ? selected : null);
    }
   //inv.setItem(36 + target.getInventory().getHeldItemSlot(), selected);
    for(int i = 45; i < 45 + armor.length; i++){
      inv.setItem(i, armor[i-45]);
    }

    inv.setItem(50, offHand);
    MunchyMax.getTaskHandler().runTask(()->{inv.getViewers().forEach(( hi)-> ((Player)hi).updateInventory());},1);


  /*
    for(int i = 0; i<9 ; i++){
      inv.setItem(i+9, target.getInventory().getItem(i));
      inv.setItem(i,AIR);

    }
    inv.setItem(target.getInventory().getHeldItemSlot(), ;
    for(int i = 9; i<36 ; i++){
      inv.setItem(i+9, target.getInventory().getItem(i));

    }

    for(int i  = 0; i<4; i++){
      inv.setItem(i+37,target.getInventory().getArmorContents()[i]);
    }

    inv.setItem(37+6, target.getInventory().getItemInOffHand());
*/
  }

  @Override
  public Listener supplyListener (){
    return new Listener(){
      @EventHandler
      public void invClose(InventoryCloseEvent ev){
        if(ev.getInventory()== null)return;
        if(ev.getInventory().getHolder() == null ||! (ev.getInventory().getHolder() instanceof Player))return;
        if(!targets.containsKey(((Player) ev.getInventory().getHolder()).getUniqueId()))return;
        Inventory inv = ev.getInventory();
        Player holder = (Player)ev.getInventory().getHolder();
        if(inv.getViewers().isEmpty()){
          targets.remove(holder.getUniqueId());
        }else{
          workInv(holder);
        }

      }

      @EventHandler
      public void onInvClick(InventoryClickEvent ev){
        HumanEntity pl = ev.getWhoClicked();
        if(targets.containsKey(pl.getUniqueId())){
          workInv((Player) pl);
        }else{
          InventoryView view =pl.getOpenInventory();
          if(view == null)return;
          if(view.getTopInventory() == null)return;
          if(view.getTopInventory().getHolder() == null)return;
          if(!(view.getTopInventory().getHolder() instanceof Player))return;
          Player targ =(Player)view.getTopInventory().getHolder() ;
          workInv(targ);



        }
      }

      @EventHandler
      public void onInvPickUp(InventoryPickupItemEvent ev){
        if(ev.getInventory()== null)return;
        if(ev.getInventory().getHolder() == null ||! (ev.getInventory().getHolder() instanceof Player))return;
        if(!targets.containsKey(((Player) ev.getInventory().getHolder()).getUniqueId()))return;
        Inventory inv = ev.getInventory();
        Player holder = (Player)ev.getInventory().getHolder();
        workInv(holder);
      }

      @EventHandler
      public void onHandSwitch(PlayerItemHeldEvent ev){
        Player target = ev.getPlayer();
        if(!targets.containsKey(target.getUniqueId()))return;
        workInv(target);
      }

      @EventHandler
      public void onKick(PlayerKickEvent ev){
        Player target = ev.getPlayer();
        if(!targets.containsKey(target))return;
        clearInv(target);
      }

      @EventHandler
      public void onLeave(PlayerQuitEvent ev){
        Player target = ev.getPlayer();
        clearInv(target);
      }

      @EventHandler
      public void onItemDrop(PlayerDropItemEvent ev){
        Player pl = ev.getPlayer();
        if(targets.containsKey(pl.getUniqueId())){
          workInv( pl);
        }else{
          InventoryView view =pl.getOpenInventory();
          if(view == null)return;
          if(view.getTopInventory() == null)return;
          if(view.getTopInventory().getHolder() == null)return;
          if(!(view.getTopInventory().getHolder() instanceof Player))return;
          Player targ =(Player)view.getTopInventory().getHolder() ;
          workInv(targ);



        }
      }

      @EventHandler
      public void onItemPick(PlayerPickupItemEvent ev){
        Player target = ev.getPlayer();
        if(!targets.containsKey(target.getUniqueId()))return;
        workInv(target);
      }

      @EventHandler
      public void onHandSwap(PlayerSwapHandItemsEvent ev){
        Player target = ev.getPlayer();
        if(!targets.containsKey(target.getUniqueId()))return;
        workInv(target);
      }

      @EventHandler
      public void onGamemode( PlayerGameModeChangeEvent ev){
        Player target = ev.getPlayer();
        if(!targets.containsKey(target.getUniqueId()))return;
        workInv(target);
      }

      @EventHandler
      public void onItembreak( PlayerItemBreakEvent ev){
        Player target = ev.getPlayer();
        if(!targets.containsKey(target.getUniqueId()))return;
        workInv(target);
      }

      @EventHandler
      public void onItemConsume( PlayerItemConsumeEvent ev){
        Player target = ev.getPlayer();
        if(!targets.containsKey(target.getUniqueId()))return;
        workInv(target);
      }


      @EventHandler
      public void onInteract(PlayerInteractEvent ev){
        Player target = ev.getPlayer();
        if(!targets.containsKey(target.getUniqueId()))return;
        workInv(target);
      }
      @EventHandler
      public void onDamage(  EntityDamageEvent ev){
        if(!(ev.getEntity() instanceof Player))return;
        Player target = (Player)ev.getEntity();
        if(!targets.containsKey(target.getUniqueId()))return;
        workInv(target);
      }

      @EventHandler
      public void onDeath(PlayerDeathEvent ev){
        Player target = ev.getEntity();
        if(!targets.containsKey(target.getUniqueId()))return;
        workInv(target);
      }



    };
  }
}
