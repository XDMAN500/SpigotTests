package me.varmetek.munchymc.commands;

import me.varmetek.core.commands.CmdCommand;
import me.varmetek.core.service.Element;
import me.varmetek.core.util.Messenger;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by XDMAN500 on 3/1/2017.
 */
public class ShopView implements Element
{

	private Map<UUID,Entry> menu = new HashMap<>();

	private final Listener event;
	public ShopView(){


		event = new Listener()
		{
			@EventHandler
			public void clickHead (InventoryClickEvent ev){
				Player player = (Player) ev.getWhoClicked();
				if (ev.getClickedInventory() == null) return;
				if (!menu.containsKey(player.getUniqueId())) return;
				Bukkit.getServer().getLogger().info("On list");
				Entry v = menu.get(player.getUniqueId());

				if (v.view != View.MAIN) return;
				Bukkit.getServer().getLogger().info("On list : in Main");
				ItemStack clicked = ev.getCurrentItem();
				if (clicked == null) return;
				if (clicked.getType() != Material.SKULL_ITEM) return;
				if (clicked.getDurability() != 3) return;
				SkullMeta skull = (SkullMeta) clicked.getItemMeta();
				String ownerName = skull.getOwner();
				openShopPlayerMain(player, Bukkit.getServer().getPlayer(ownerName));
			}


			@EventHandler
			public void closeInventory (final InventoryCloseEvent ev){
				Player player = (Player) ev.getPlayer();

				if (!menu.containsKey(player.getUniqueId())) return;
				Entry v = menu.get(player.getUniqueId());
				Object[] data = v.data;
				if (v.view.parent == null){
					menu.remove(player.getUniqueId());
				} else {
					menu.put(player.getUniqueId(), new Entry(v.view, data));
					open(player, v.view.parent);
				}
			}
		};
	}

	@Override
	public void clean (){

		menu.clear();
		menu = null;
	}

	@Override
	public CmdCommand[] supplyCmd (){
		return null;
	}

	@Override
	public Listener supplyListener(){
		return event;
	}

	class Entry{
		final View view;
		final Object[] data;
		Entry(View w, Object... data){
			view =w;
			this.data = data;
		}



	}

	public void openShopMenu(Player player){
		Inventory inv = Bukkit.getServer().createInventory(null,54, "Shop");

		Bukkit.getServer().getOnlinePlayers().forEach(  (u) -> {inv.addItem(getHead(u));});
		//inv.addItem(getHead(pl));
		player.openInventory(inv);
		menu.put(player.getUniqueId(), new Entry(View.MAIN));
	}

	public void openShopPlayerMain(Player viewer, Player owner){
		Bukkit.getServer().getLogger().info("On list : in Player shop");
		menu.put(viewer.getUniqueId(), new Entry(View.PLAYERSHOP_MAIN, owner));

		Inventory inv = Bukkit.getServer().createInventory(null,9, "Shop > " + owner.getName());
		ItemStack item = new ItemStack(Material.EMERALD_BLOCK);
		ItemMeta im = item.getItemMeta();
		im.setDisplayName(Messenger.color("&ABuying"));
		item.setItemMeta(im);
		inv.setItem(2, item);

		item = new ItemStack(Material.REDSTONE_BLOCK);
		im = item.getItemMeta();
		im.setDisplayName(Messenger.color("&ASelling"));
		item.setItemMeta(im);
		inv.setItem(6, item);
		viewer.openInventory(inv);


	}











	public void open(Player viewer, View view, Object... data){
		switch (view){
			case MAIN: openShopMenu(viewer);break;
			case PLAYERSHOP_MAIN: openShopPlayerMain(viewer,(Player)data[0]);break;

		}
	}
	private ItemStack getHead(Player player){
		ItemStack item = new ItemStack(Material.SKULL_ITEM,1,(short)3);

		SkullMeta im = (SkullMeta) item.getItemMeta();
		im.setOwner(player.getName());
		item.setItemMeta(im);
		return item;


	}



	enum View {
		MAIN(null), PLAYERSHOP_MAIN(MAIN), PLAYERSHOP_BUY(PLAYERSHOP_MAIN),PLAYERSHOP_SELL(PLAYERSHOP_MAIN),PLAYERSHOP_BUY_ITEM(PLAYERSHOP_BUY),PLAYERSHOP_SELL_ITEM(PLAYERSHOP_SELL);

		public final View parent;
		View(View par){
			parent = par;
		}

	}
}
