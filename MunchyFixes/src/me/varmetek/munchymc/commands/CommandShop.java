package me.varmetek.munchymc.commands;

import com.google.common.collect.ImmutableList;
import me.varmetek.core.commands.CmdCommand;
import me.varmetek.core.commands.CmdSender;
import me.varmetek.core.service.Element;
import me.varmetek.munchymc.MunchyMax;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

/**
 * Created by XDMAN500 on 2/26/2017.
 */
public class CommandShop implements Element
{

	private CmdCommand[] commands;

	public CommandShop (){


		commands = new CmdCommand[]{
		  new CmdCommand.Builder("shop").setLogic(
        (cmd)->{

          CmdSender sender = cmd.getSender();
          int len = cmd.getArguments().size();
          String alias = cmd.getAlias();
          ImmutableList<String> args = cmd.getArguments();

        Player pl;
        if (sender.isPlayer()){
          pl = sender.asPlayer();
        } else {
          return;
        }
        //Inventory inv = plugin.getServer().createInventory(null,18, " Shop");
        //Bukkit.getServer().getOnlinePlayers().forEach(  (u) -> {inv.addItem(getHead(u));});
        //inv.addItem(getHead(pl));
        //pl.openInventory(inv);
        MunchyMax.getShop().open(pl, ShopView.View.MAIN);
      }).build()
		};
	}

  private ItemStack getHead (Player player){
    ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);

    SkullMeta im = (SkullMeta) item.getItemMeta();
    im.setOwner(player.getName());
    item.setItemMeta(im);
    return item;

  }

  @Override
	public void clean (){

	}



	@Override
	public CmdCommand[] supplyCmd (){
		return new CmdCommand[0];
	}

	@Override
	public Listener supplyListener(){
		return null;
	}
}
