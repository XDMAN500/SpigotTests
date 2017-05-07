package me.varmetek.munchymc.commands;

import me.varmetek.core.commands.CmdCommand;
import me.varmetek.core.service.Element;
import me.varmetek.core.util.Messenger;
import me.varmetek.munchymc.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by XDMAN500 on 5/7/2017.
 */
public class CommandCheckInv implements Element
{
  private Main main;
  private Map<UUID,Inventory> targets = new HashMap<>();

  public CommandCheckInv(Main plugin){
    main = plugin;
  }
  @Override
  public void clean (){

  }

  @Override
  public CmdCommand[] supplyCmd (){
    return new CmdCommand[]{
      new CmdCommand.Builder("checkInv")
        .setLogic((sender,alias,args,len) ->{
          if(!sender.isPlayer()){
            Messenger.send(sender.asSender(), "&c This command is executable by players.");
            return;
          }
          Player pl = sender.asPlayer();
          if(len == 0){
            Messenger.send(pl,"&c Usage: /checkinv <player>");
          }else{
            String name = args[0];
            Player target = Bukkit.getPlayer(name);
            if(target == null){
              Messenger.send(pl,"&c Player \""+ name +"\" is not online.");
              return;
            }

            name = target.getName();
            if(targets.containsKey(target.getUniqueId())){
              pl.openInventory(targets.get(target.getUniqueId()));
            }else{
              workInv(target);
            }


          }
        })
        .build()
    };
  }
  public void workInv(Player target){
    UUID id = target.getUniqueId();
    if(!targets.containsKey(id)){
      targets.put(id,Bukkit.createInventory(target,9*6));
    }
    Inventory inv =  targets.get(id) == null ? Bukkit.createInventory(null,9*6) : targets.get(id) ;
    inv.setItem(target.getInventory().getHeldItemSlot(), new ItemStack(Material.STAINED_GLASS));
    for(int i = 0; i<9 ; i++){
      inv.setItem(i+9, target.getInventory().getItem(i));

    }
    for(int i = 9; i<36 ; i++){
      inv.setItem(i+9, target.getInventory().getItem(i));

    }

    for(int i  = 0; i<4; i++){
      inv.setItem(i+37,target.getInventory().getArmorContents()[i]);
    }

    inv.setItem(37+6, target.getInventory().getItemInOffHand());

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
        if(!targets.containsKey(pl.getUniqueId()))return;
        workInv((Player)pl);
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
        Inventory inv = targets.get(target.getUniqueId());

        for(HumanEntity he: inv.getViewers()){
          he.closeInventory();
        }
        targets.remove(target.getUniqueId());
      }

      @EventHandler
      public void onLeave(PlayerQuitEvent ev){
        Player target = ev.getPlayer();
        if(!targets.containsKey(target))return;
        Inventory inv = targets.get(target.getUniqueId());

        for(HumanEntity he: inv.getViewers()){
          he.closeInventory();
        }
        targets.remove(target.getUniqueId());
      }

      @EventHandler
      public void onItemDrop(PlayerDropItemEvent ev){
        Player target = ev.getPlayer();
        if(!targets.containsKey(target.getUniqueId()))return;
        workInv(target);
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
      public void onHandSwap( PlayerGameModeChangeEvent ev){
        Player target = ev.getPlayer();
        if(!targets.containsKey(target.getUniqueId()))return;
        workInv(target);
      }

      @EventHandler
      public void onHandSwap( PlayerItemBreakEvent ev){
        Player target = ev.getPlayer();
        if(!targets.containsKey(target.getUniqueId()))return;
        workInv(target);
      }

      @EventHandler
      public void onHandSwap( PlayerItemConsumeEvent ev){
        Player target = ev.getPlayer();
        if(!targets.containsKey(target.getUniqueId()))return;
        workInv(target);
      }


      @EventHandler
      public void onHandSwap(PlayerInteractEvent ev){
        Player target = ev.getPlayer();
        if(!targets.containsKey(target.getUniqueId()))return;
        workInv(target);
      }
      @EventHandler
      public void onHandSwap(  EntityDamageEvent ev){
        if)))===¡¡¡'0'
        if(!targets.containsKey(target.getUniqueId()))return;
        workInv(target);
      }

        PlayerDeathEvent

    };
  }
}
