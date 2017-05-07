package me.varmetek.munchymc.backend;

import me.varmetek.core.util.Messenger;
import me.varmetek.munchymc.Main;
import me.varmetek.munchymc.util.BlockLoc;
import me.varmetek.munchymc.util.UtilInventory;
import me.varmetek.munchymc.util.Utils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Furnace;
import org.bukkit.material.MaterialData;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * Created by XDMAN500 on 1/3/2017.
 */
public enum Shops implements Listener
{
	HANDLER;

	public Map<Integer, Shop> shops = new HashMap<>();

	@EventHandler
	public void signBreakEvent(BlockBreakEvent ev)
	{
		Player player = ev.getPlayer();
		if(player == null)return;
		Block block = ev.getBlock();

		if(block.getType() != Material.WALL_SIGN)return;
		BlockLoc location = new BlockLoc(block.getLocation());
		Shop shop = getShop(location);
		if(shop == null )return;

		if(shop.owner == ev.getPlayer().getUniqueId() && player.isSneaking())
		{
			Messenger.send(player, "Destroyed Shop");
			shops.remove(location.hashCode());
		}else{
			ev.setCancelled(true);
		}
	}
	@EventHandler
	public void signInteract(PlayerInteractEvent ev)
	{
		if(!ev.hasBlock())return;
		if(ev.getAction() != Action.RIGHT_CLICK_BLOCK)return;
		Player player = ev.getPlayer();
		Block block = ev.getClickedBlock();

		if(block.getType() != Material.WALL_SIGN)return;
		BlockLoc location = new BlockLoc(block.getLocation());

		//Messenger.send(player,"NEW: "+location.toString());
		//Messenger.send(player,"OLD: "+shops.keySet().iterator().next().toString());
		Shop shop =getShop(location);
		Messenger.send(player,"NEW: "+shop);
		if(shop == null )return;
		if(shop.owner == ev.getPlayer().getUniqueId() && player.isSneaking()){
			Messenger.send(player, "TODO - OPENING INVENTORIES");
		}else
		{
			if (!ev.hasItem())return;
			ItemStack item = ev.getItem();

			if (shop.itemType == Shop.ItemType.UNKNOWN)
			{

				if(shop.owner == ev.getPlayer().getUniqueId() )
				{
					if (item.hasItemMeta())
					{
						shop.itemType = Shop.ItemType.ALPHA;
						shop.item = UtilInventory.getAlphaCode(item);

					} else
					{
						shop.itemType = Shop.ItemType.MATERIAL;
						shop.item = item.getData();
					}
				}else{
					Messenger.send(player, "The shop owner has to fix this shop");
				}

			} else
			{
				Messenger.send(player,"Getting to the good stuff");
				boolean verified =
						UtilInventory.verifyItem(item, (shop.itemType ==Shop.ItemType.MATERIAL ? (MaterialData)shop.item : new MaterialData(Material.AIR))) || UtilInventory.verifyItem(item, (shop.itemType ==Shop.ItemType.ALPHA ? (String)shop.item : ""));
				if(!verified)return;
				int amountOwned = UtilInventory.numberOfItem(player.getInventory(),item);



						if(shop.type  == Shop.MarketType.SELL){

							double moneyEarned = shop.price*(((double)amountOwned)/ ((double)shop.amount));
							Messenger.send(player, "&aYou recieved $"+ moneyEarned);
							player.getInventory().remove(item);
						}else{
							double moneyPayed = shop.price;
							Messenger.send(player, "&aYou lost $"+ moneyPayed);
							ItemStack toadd = item.clone();
							toadd.setAmount(shop.amount);
							player.getInventory().addItem(toadd);
						}


			}
		}
	}
	@EventHandler
	public void createShop(SignChangeEvent ev)
	{

		////
		final Block block = ev.getBlock();
		final Player player = ev.getPlayer();
		//Messenger.send(player,"MAKiNG A SIGN "+ block.getType().name());

		if(block.getType() != Material.WALL_SIGN )return;
		Sign sign = (Sign)block.getState();
		Messenger.send(player,"MAKiNG A SIGN[2] " + sign.getLine(0));
		org.bukkit.material.Sign signData = (org.bukkit.material.Sign)sign.getData();
		Block behind = block.getRelative(signData.getAttachedFace());
		if(behind == null || Material.FURNACE == behind.getType() )return;
		Furnace furn = (Furnace)behind.getState().getData();
		furn.setFacingDirection(signData.getFacing());
		org.bukkit.block.Furnace furnaceState = 	(org.bukkit.block.Furnace)behind.getState();

		Main.get().getTaskHandler().run( () ->{
		Error error = validateSign( player.getUniqueId(),sign);
		switch (error){

			case UNKNOWN:
				Messenger.send(player, "&cSometing weird happened. Contact an Administrator.");
				ev.setLine(3,Utils.colorCode("&c"+ ev.getLine(0)));
				return;
			case FIRST:
				return;//Probably just a normal sign
			case SECOND:
				Messenger.send(player, "&cError in the second line. &7the format is <itemid>:<itemdata> or #<alphaCode>");
				ev.setLine(1,Utils.colorCode("&c"+ ev.getLine(1)));
				return;
			case THIRD:
				Messenger.send(player, "&cError in the third line. &7the format is $<price>");
				ev.setLine(2, Utils.colorCode("&c"+ ev.getLine(2)));
				return;

			case FOURTH:
				Messenger.send(player, "&cError in the third line. &7the format is <amount>");
				ev.setLine(3,Utils.colorCode("&c"+ ev.getLine(3)));
				return;

			case NONE:
				Messenger.send(player, "&aYou have successfully created a shop");
				ev.setLine(0,Utils.colorCode("&1"+ ev.getLine(0)));
				break;
		}

		});



	}
	public Shop getShop(BlockLoc loc){
		return shops.get(loc.hashCode());
	}
	private Error validateSign( UUID player ,Sign sign )
	{
		UUID owner;
		Shop.MarketType type;
		BlockLoc location;
		Shop.ItemType itemType;
		Object item;
		double price;
		int amount;


		String[] lines = sign.getLines();
		Location loc= sign.getLocation();
		BlockFace attached = ((org.bukkit.material.Sign)sign.getData()).getAttachedFace();


		if(lines == null || lines.length == 0)return Error.UNKNOWN;
		Pattern pat = Pattern.compile("[a-z0-9]");
		//if(!("[BUY]".equalsIgnoreCase(lines[0]) || "[SELL]".equalsIgnoreCase(lines[0])))return Error.FIRST;


		owner = player;
		type = "[BUY]".equalsIgnoreCase(lines[0]) ? Shop.MarketType.BUY  :
				       ( "[SELL]".equalsIgnoreCase(lines[0]) ? Shop.MarketType.SELL : Shop.MarketType.NONE);
		if(type == Shop.MarketType.NONE)return Error.FIRST;

		if(lines[1].startsWith("#") ){
			if(lines[1].length() == 1) return Error.SECOND;
			if(!pat.matcher(lines[1].substring(1,lines[1].length())).matches())return Error.SECOND;
			itemType  = Shop.ItemType.ALPHA;
			item = lines[1].substring(1,lines[1].length());
		}else{
			try{
				int id = 0;
				byte data = 0;
				if(lines[1].contains(":")){
					String[] side = lines[1].split(":");
					if(side.length > 2 ) return Error.SECOND;
					id =Integer.parseInt(side[0]);
					data =Byte.parseByte(side[1]);
				}else{
					id =Integer.parseInt(lines[1]);
				}
				item = new MaterialData(id,data);
				itemType = Shop.ItemType.MATERIAL;
			}catch(Exception e){
				if(!"<?>".equalsIgnoreCase(lines[1]))
					return Error.SECOND;

				itemType = Shop.ItemType.UNKNOWN;
				item = null;


			}
		}
		try{
			if(!lines[2].startsWith("$") || lines[2].length() == 1)return Error.THIRD;

			double d = Double.parseDouble(lines[2].substring(1,lines[2].length()));
			if(d< 0.01) return Error.THIRD;
			price = d;
		}catch(Exception e){
			return Error.THIRD;
		}
		try{
			int am =Integer.parseInt(lines[3]);
			if(am <1) return Error.FOURTH;
			amount = am;
		}catch(Exception e){
			return Error.FOURTH;
		}

		BlockLoc bv = new BlockLoc(loc);
		shops.put(bv.hashCode(), new Shop(owner,type,bv,itemType,item,price,amount,attached));
		return Error.NONE;

	}

	private enum Error
	{
		UNKNOWN,FIRST,SECOND,THIRD,FOURTH,NONE
	}
}
