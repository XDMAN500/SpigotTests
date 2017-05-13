package me.varmetek.munchymc.rares;

import me.varmetek.munchymc.MunchyMax;
import me.varmetek.munchymc.backend.RareItemListener;
import me.varmetek.munchymc.backend.test.CustomItemRare;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.*;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.BlockVector;
import org.bukkit.util.Vector;

import java.util.*;


public  final class RareParkourBoots extends RareItemListener
{
	//public static  RareParkourBoots INSTANCE = new RareParkourBoots();



	public RareParkourBoots(CustomItemRare rare){

		super(rare);
	}
	private static final Map<UUID, Set<BlockVector>> blocks = new HashMap<>();
	

	@Override
	public  boolean check(Player pl)
	{
		return  isEquiped(pl, EquipmentSlot.FEET) && !pl.isSneaking();
		 
	}


	private static final BlockVector convert(Location loc){
		return new BlockVector(loc.getBlockX(),loc.getBlockY(),loc.getBlockZ());
	}
	private static boolean compare(BlockVector v1, BlockVector v2){
		return  (v1.getBlockY() == v2.getBlockY() 
				&& v1.getBlockX() == v2.getBlockX() 
				&& v1.getBlockZ() == v2.getBlockZ());
	}
	private static boolean  contains(UUID id, BlockVector vec){
		return contains(id,vec,blocks.get(id));
	}
	
	private static boolean  contains(UUID id, BlockVector vec, Set<BlockVector> list){
		for(BlockVector loc : list){
			if(compare(vec,loc)){
				return true;
			}
		}
		return false;
	}
	private static Location copyLoc(Location loc){
		return new Location(loc.getWorld(),loc.getX(),loc.getY(),loc.getZ());
	}
	private static Location convertVec(World world, BlockVector vec){
		return new Location(world,vec.getBlockX(),vec.getBlockY(),vec.getBlockZ());
	}
	private static void removePlayer(UUID id){
		clearBlocks(id);
		blocks.remove(id);
	}
	private static HashSet<BlockVector> prepareFloor(Location loc, HashSet<BlockVector> set){
		set.add(convert(copyLoc(loc).add(0, -1, 0)));
		
		set.add(convert(copyLoc(loc).add(1, -1, 0)));
		set.add(convert(copyLoc(loc).add(-1, -1, 0)));
		
		set.add(convert(copyLoc(loc).add(0, -1, 1)));
		set.add(convert(copyLoc(loc).add(0, -1, -1)));
		
		set.add(convert(copyLoc(loc).add(1, -1, 1)));
		set.add(convert(copyLoc(loc).add(-1, -1, 1)));

		set.add(convert(copyLoc(loc).add(-1, -1, -1)));
		set.add(convert(copyLoc(loc).add(1, -1, -1)));
		
		return set;
	}
	private static void makeFloor(UUID id){ 
		Player pl = Bukkit.getPlayer(id);
		makeFloor(pl.getUniqueId(), new Vector(0,0,0));
	}
	private static void makeFloor(UUID id, Vector dir){
	
		if(!blocks.containsKey(id))return;
		Player pl = Bukkit.getPlayer(id);
		Location loc = pl.getLocation();
		int rounds  = 1;
		if(pl.hasPotionEffect(PotionEffectType.SPEED)){
			for(PotionEffect pe :pl.getActivePotionEffects()){
				if(pe.getType().equals(PotionEffectType.SPEED)){
					rounds += pe.getAmplifier()/3;
				}
			}
		}
		Vector vec = dir.setY(0);
		
		
		HashSet<BlockVector> append = new HashSet<BlockVector>();
		for(int i = rounds; i>0; i--){
			prepareFloor(loc,append);
			loc.add(vec);
		}
	
	
		HashSet<BlockVector> copy = new HashSet<BlockVector>(blocks.get(id));
		for(BlockVector bv : copy){
			if(!contains(id,bv,append)){
				removeBlock(id,bv);
			}
		}
		
		for(BlockVector vc :append){
			placeBlock(pl.getUniqueId(),vc);
		}
		
	
		

		
	}
	private static void clearBlocks(UUID id){
		if(!blocks.containsKey(id))return;
		HashSet<BlockVector> copy = new HashSet<BlockVector>(blocks.get(id));
		for(BlockVector loc : copy){
			removeBlock(id,loc);
		}
	}
	////////////////////////////////////////////////////////////////////////////////////////
	private static void placeBlock(UUID id, Location loc){ placeBlock(id,convert(loc));}
	
	private static void placeBlock(UUID id, BlockVector loc){
		if(!blocks.containsKey(id))return;
		
		World world = Bukkit.getPlayer(id).getWorld();
		Block a = world.getBlockAt(convertVec(world,loc));
		if(a.getType().equals(Material.AIR) || contains(id,loc)){
			blocks.get(id).add(loc);
			
			a.setType(Material.GLASS);
		}
		
	}
	
	private static void removeBlock(UUID id, Location loc){ removeBlock(id,loc);}
	
	private static void removeBlock(UUID id, BlockVector loc){
		blocks.get(id).remove(loc);
		World world = Bukkit.getPlayer(id).getWorld();
		Block a = world.getBlockAt(convertVec(world,loc));
		if(a.getType().equals(Material.GLASS)){
			a.setType(Material.AIR);
		}
	}
	///////////////////////////////////////////////////////////////////////////////////////////////////


	@Override
	public void clean (){

	}

	@Override
	public Listener supplyListener (){
		return new Listener()
		{
			////////////////////////////////////////////////////////////////////////////////////////////////
			@EventHandler
			public void wear(InventoryClickEvent ev){
				Player pl = (Player)ev.getWhoClicked();
				Inventory inv = ev.getClickedInventory();
				if(inv == null)return;
				if(!inv.equals(pl.getInventory()))return;

				if(ev.getSlotType() !=  InventoryType.SlotType.ARMOR)return;
				MunchyMax.getTaskHandler().run(()->{
					if(check(pl)){

						blocks.put(pl.getUniqueId(), new HashSet<BlockVector>() );
						makeFloor(pl.getUniqueId());
					}else{

						removePlayer(pl.getUniqueId());
					}

				});
			}

			@EventHandler
			public void walk(PlayerMoveEvent e){
				Player pl = e.getPlayer();
				if(check(pl)){
					if(!blocks.containsKey(pl.getUniqueId())){
						blocks.put(pl.getUniqueId(), new HashSet<BlockVector>() );
					}

					if(blocks.containsKey(pl.getUniqueId())){

						makeFloor(pl.getUniqueId(),
							new Vector(e.getTo().getX()- e.getFrom().getX(), 0 ,e.getTo().getZ()- e.getFrom().getZ()));
					}
				}
			}

			@EventHandler
			public void onSneak(PlayerToggleSneakEvent ev){
				Player pl = ev.getPlayer();
				if(!ev.isSneaking())return;
				clearBlocks(pl.getUniqueId());
			}

			@EventHandler
			public void onLeave(PlayerQuitEvent ev){
				Player pl = ev.getPlayer();
				removePlayer(pl.getUniqueId());
			}

			@EventHandler
			public void onKick(PlayerKickEvent ev){
				Player pl = ev.getPlayer();
				removePlayer(pl.getUniqueId());
			}

			@EventHandler
			public void onTeleport(PlayerTeleportEvent ev){
				final Player pl = ev.getPlayer();
				clearBlocks(pl.getUniqueId());
				MunchyMax.getTaskHandler().run(()->{
					makeFloor(pl.getUniqueId());

				});
			}
			@EventHandler
			public void onDeath(	PlayerDeathEvent ev){

				final Player pl = ev.getEntity();
				clearBlocks(pl.getUniqueId());
			}

		};
	}

////////////////////////////////////////////////////////////////////////////
		

		
				
				
				
				
				
				
			
		
	}


