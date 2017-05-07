package me.varmetek.munchymc.util;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

public class TestStuff implements Listener{

		public static void dd(){
			List<String> e = Arrays.asList(new String[]{"John","Bob","Lol"});
		}
		
		Player plOne, plTwo;
		Location loc1,loc2, spawn;
		public TestStuff(Player arg1, Player arg2, Location loc1, Location loc2, Location spawnLoc){
			plOne = arg1;
			plTwo = arg2;
			this.loc1 = loc1;
			this.loc2 = loc2;
			spawn = spawnLoc;
		}
	
		public void start(){
			giveInv(plOne);
			giveInv(plTwo);
			plOne.teleport(loc1);
			plTwo.teleport(loc2);
			
		}
		
		public void giveInv(Player pl){
			pl.setItemInHand(new ItemStack(Material.DIAMOND));
			pl.getInventory().setHelmet(new ItemStack(Material.IRON_HELMET));
			pl.getInventory().setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
			pl.getInventory().setLeggings(new ItemStack(Material.IRON_LEGGINGS));
			pl.getInventory().setBoots(new ItemStack(Material.IRON_BOOTS));
			
		}
		public void stop(){
			plOne.getInventory().clear();
			plTwo.getInventory().clear();
			
			plOne.teleport(spawn);
			plTwo.teleport(spawn);
		}
		
		@EventHandler
		public void onHit(EntityDamageEvent ev){
			if(!ev.getEntity().getType().equals(EntityType.PLAYER))return;
			Player victim = (Player)ev.getEntity();
			if(ev.getFinalDamage()<= victim.getHealth())return;
			if(victim.equals(plOne) |victim.equals(plTwo))
			stop();
			
		}
	
		
		
		
}
