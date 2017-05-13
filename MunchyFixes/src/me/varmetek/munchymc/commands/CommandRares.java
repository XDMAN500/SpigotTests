package me.varmetek.munchymc.commands;

import me.varmetek.core.commands.CmdCommand;
import me.varmetek.core.item.CustomItem;
import me.varmetek.core.service.Element;
import me.varmetek.munchymc.MunchyMax;
import me.varmetek.munchymc.backend.PlayerSession;
import me.varmetek.munchymc.backend.Rares;
import me.varmetek.munchymc.backend.test.CustomItemRare;
import me.varmetek.munchymc.backend.test.EnumCustomItem;
import me.varmetek.munchymc.util.UtilInventory;
import me.varmetek.munchymc.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class CommandRares implements Element
{
  private Inventory gui = Bukkit.createInventory(null, 9 * 6, "Rares");

  private final CmdCommand[] commands;

  public CommandRares (){


    makeGUI();
    commands = new CmdCommand[]{
      new CmdCommand.Builder("rares", (sender, alias, args, length) -> {
        if (!(sender.isPlayer())) return;
        Player pl = sender.asPlayer();
        pl.openInventory(gui);


      }).build()
    };
  }


  public void makeGUI (){
    gui.clear();
    for (CustomItemRare e : Rares.getRares()) {
      gui.addItem(e.getItem());

    }
    while (gui.firstEmpty() != -1) {
      CustomItem slot = MunchyMax.getItemMap()
                          .get(EnumCustomItem.SLOT_PLACEHOLDER.name()).get();
      //Bukkit.getLogger().warning("Is slot null? "+ (slot == null));
      //Bukkit.getLogger().warning("Has an item? "+ (slot.getItem() != null));
      gui.setItem(gui.firstEmpty(), slot.getItem());
    }


    };



  public void clean (){

    gui = null;
  }



  @Override
  public CmdCommand[] supplyCmd (){
    return commands;
  }


  @Override
  public Listener supplyListener (){
    return new Listener(){
      @EventHandler
      public void openGui (InventoryClickEvent ev){
        //makeGUI();
        ItemStack clicked = ev.getCurrentItem();
        Inventory clickedInv = ev.getClickedInventory();
        if (clicked == null) return;
        if (clickedInv == null) return;

        CustomItem id = MunchyMax.getItemMap().getByItem(clicked);


        if (clicked == null) return;
        if (clicked.getType().equals(Material.AIR)) return;
        if (!clicked.hasItemMeta()) return;
        if (clickedInv == null) return;
        if (clickedInv.equals(gui))
          ev.setCancelled(true);
        else
          return;
        if (id == null || !(id instanceof CustomItemRare)){
          ev.setCancelled(true);
          return;
        }


        Player pl = (Player) ev.getWhoClicked();
        PlayerSession user = MunchyMax.getPlayerHandler().getSession(pl);
        if (!pl.getOpenInventory().getTopInventory().equals(gui)) return;


        //Get item
        ItemStack toGive = clicked.clone();


        if (user.isTesting()){
          ItemMeta im = toGive.getItemMeta();
          List<String> lore = im.getLore() == null ? new ArrayList<String>() : im.getLore();
          lore.add(Utils.colorCode("&6*TEST MODE*"));
          im.setLore(lore);
          toGive.setItemMeta(im);
        }
        UtilInventory.giveItem(toGive, ev.getClick(), pl, ev.getHotbarButton());


      }


    };
  }
}
