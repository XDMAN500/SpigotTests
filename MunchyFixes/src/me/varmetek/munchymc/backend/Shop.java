package me.varmetek.munchymc.backend;

import me.varmetek.munchymc.util.BlockLoc;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Furnace;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import java.util.UUID;

/**
 * Created by XDMAN500 on 1/3/2017.
 */
public class Shop
{
	UUID owner;
	MarketType type;
	BlockLoc location;
	ItemType itemType;
	Object item;
	double price;
	int amount;
	BlockFace attachedFace;
	ItemStack itemCache;
	 Shop(UUID owner, MarketType type, BlockLoc location, ItemType itemType, Object item, double price, int amount,BlockFace attachedFace){
	 	this.owner = owner; this.type = type; this.location = location; this.itemType = itemType; this.item = item;
	 	this.price = price; this.amount = amount;this.attachedFace = attachedFace;
	 }

	public enum MarketType
	{
		SELL,BUY,NONE
	}

	public enum ItemType{
		ALPHA,MATERIAL,UNKNOWN
	}
	public ItemStack getItemCache(){
	 	switch (itemType){

		    case ALPHA:
			    return ((Furnace)location.getLocation().getBlock().getRelative(attachedFace)).getInventory().getSmelting();
		    case MATERIAL:
			    return ((MaterialData)item).toItemStack();
		    case UNKNOWN:
			    return null;
	    }
	    return null;

	}
}
