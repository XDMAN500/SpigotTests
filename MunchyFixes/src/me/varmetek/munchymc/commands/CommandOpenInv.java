package me.varmetek.munchymc.commands;

import me.varmetek.core.commands.CmdCommand;
import me.varmetek.core.service.Element;
import me.varmetek.core.util.Messenger;
import me.varmetek.munchymc.Main;
import me.varmetek.munchymc.backend.User;
import me.varmetek.munchymc.util.UtilInventory;
import me.varmetek.munchymc.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

/**
 * Created by XDMAN500 on 1/26/2017.
 */
public class CommandOpenInv implements Element
{

	private Main main;
	private Map<UUID,Inventory> invs = new HashMap<>();
  private final CmdCommand[] commands;

  public CommandOpenInv(Main main){
		this.main = main;

    commands = new CmdCommand[]{
      new CmdCommand.Builder("openinv" , (sender,alias,args,length) -> {
        Player player;
        User user;

        if(sender.isPlayer()){
          player = sender.asPlayer();
          user =  main.getUserHandler().getUser(player);
        }else{
          //send a message
          return;
        }

        if(length == 0){

          List<String> list = Utils.toStringList(user.getInventoryMap().getAllowedTypes(),
            (type)->{
              return  type.name();
            });
          StringBuilder builder = new StringBuilder(" ");

          list.forEach( (name) -> {
            builder.append(" "+name+",");
          });

          builder.deleteCharAt(builder.length()-1);
          Messenger.send(player,"&7 Inventories you can open: "+ builder.toString());
          Inventory inv = createInv(player);



        }else{
          Optional<InventoryType>  option = user.getInventoryMap().getType(args[0]);
          if(option.isPresent()){
            player.openInventory(user.getInventoryMap().get(option.get()).get());

          }else{
            Messenger.send(player,"&7 Unknown Inventory type");
          }
        }


      }).build()
    };
	}

	private Listener event =  new Listener()
  {
    @EventHandler
    public void  closeInventory(InventoryCloseEvent ev){
      invs.remove(ev.getPlayer().getUniqueId());
    }

    @EventHandler
    public void inventoryCLick(InventoryClickEvent ev){
      if(ev.getClickedInventory()== null)return;
      if(!ev.getClickedInventory().equals( invs.get(ev.getWhoClicked().getUniqueId())))return;
      ItemStack item = ev.getCurrentItem();
      if(UtilInventory.isItemEmpty(item))return;
      Player player = (Player)ev.getWhoClicked();
      User user =  main.getUserHandler().getUser(player);

      switch (item.getType()){
        case WORKBENCH: player.performCommand("openinv "+ InventoryType.WORKBENCH.name());break;
        case ANVIL: player.performCommand("openinv "+ InventoryType.ANVIL.name());break;
        case ENCHANTMENT_TABLE: player.performCommand("openinv "+ InventoryType.ENCHANTING.name());break;
        case BREWING_STAND_ITEM: player.performCommand("openinv "+ InventoryType.BREWING.name());break;
        case ENDER_CHEST: player.performCommand("openinv "+ InventoryType.ENDER_CHEST.name());break;
        case CHEST: player.performCommand("openinv "+ InventoryType.CHEST.name());break;
        default: return;
      }
      UtilInventory.cancelClickEvent(ev);

    }


  };



	private Inventory createInv(Player pl){
		Inventory inv = Bukkit.createInventory(null,9);
		inv.addItem(new ItemStack(Material.WORKBENCH));
		inv.addItem(new ItemStack(Material.ANVIL));
		inv.addItem(new ItemStack(Material.ENCHANTMENT_TABLE));
		inv.addItem(new ItemStack(Material.BREWING_STAND_ITEM));
		inv.addItem(new ItemStack(Material.ENDER_CHEST));
		inv.addItem(new ItemStack(Material.CHEST));
		pl.openInventory(inv);
		invs.put(pl.getUniqueId(),inv);
		return inv;


	}


	@Override
	public CmdCommand[] supplyCmd ()
	{
		return commands;
	}

	@Override
  public Listener supplyListener(){
	  return event;
  }
  @Override
  public void clean(){
    invs = null;
    main = null;
  }
}
