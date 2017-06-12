package me.varmetek.munchymc;

import ca.thederpygolems.armorequip.ArmorListener;
import io.netty.util.internal.ConcurrentSet;
import me.varmetek.core.item.CustomItem;
import me.varmetek.core.item.ItemMap;
import me.varmetek.core.service.ElementManager;
import me.varmetek.core.util.InventorySnapshot;
import me.varmetek.core.util.PluginCore;
import me.varmetek.core.util.TaskHandler;
import me.varmetek.munchymc.backend.ChatPlaceholderMap;
import me.varmetek.munchymc.backend.KitHandler;
import me.varmetek.munchymc.backend.PointManager;
import me.varmetek.munchymc.backend.RareType;
import me.varmetek.munchymc.backend.file.*;
import me.varmetek.munchymc.backend.hooks.HookManager;
import me.varmetek.munchymc.backend.test.CustomItemRare;
import me.varmetek.munchymc.backend.test.EnumCustomItem;
import me.varmetek.munchymc.backend.user.PlayerHandler;
import me.varmetek.munchymc.commands.*;
import me.varmetek.munchymc.listeners.*;
import me.varmetek.munchymc.mines.MineManager;
import me.varmetek.munchymc.rares.*;
import me.varmetek.munchymc.util.UtilFile;
import me.varmetek.munchymc.util.Utils;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class MunchyMax extends PluginCore
{
	private static Optional<MunchyMax> instance = Optional.empty();

	private ItemMap itemMap;
	private HookManager hookManager;
	private PointManager pointManager;
	private PlayerHandler playerHandler;
	private TaskHandler taskHandler;
	private DataManager dataManager;
	private ElementManager elementManager;
	private KitHandler kitHandler;
	private TaskHandler.Task tickLoop;
	private Set<TickListener> tickListeners;
	public ShopView shop;
	private MineManager mineManager;

	private MineFileManager mineFileManager;
	private KitFileManager kitFileManager;
	private PlayerFileManager playerFileManager;
	private PointFileManager pointFileManager;

	Economy economy;
	Chat chat;
	Permission permission;



	private boolean dirty = false;

	public MunchyMax (){
		boolean dirty = instance.isPresent();

		if(dirty){
			throw new IllegalStateException("Singleton is already set");
		}else{
			instance = Optional.of(this);
		}

	}

	public static MunchyMax getInstance(){
		return instance.orElseThrow( ()->{ return new IllegalStateException("Singleton has not been set");});
	}

	public static Optional<MunchyMax> getInstanceSafe(){
		return instance;
	}




///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/*

	SERVER EVENTS

*/
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////





  @Override
	public void onLoad ()
	{
		if(dirty)return;
		this.getLogger().getParent().setLevel(Level.ALL);
    //ConfigurationSerialization.registerClass(PlayerData.class);
    ConfigurationSerialization.registerClass(InventorySnapshot.class);
    tickListeners = new ConcurrentSet<>();

		elementManager = new ElementManager(this,"MM");
		kitHandler = new KitHandler();
		itemMap = new ItemMap(this);
		dataManager = new DataManager();
		taskHandler = new TaskHandler(this);
		playerHandler = new PlayerHandler(this);
		hookManager = new HookManager();
		pointManager = new PointManager();
		mineManager = new MineManager(this);
		shop = new ShopView();

		mineFileManager = new MineFileManager(mineManager, this.getLogger());
		kitFileManager = new KitFileManager(kitHandler,this.getLogger());
		playerFileManager = new PlayerFileManager(playerHandler,this.getLogger());
		pointFileManager = new PointFileManager(pointManager,this.getLogger());

	}


	public void onEnable (){
		if(dirty){
			getLogger().warning("Plugin could not enable correctly");
			getServer().getPluginManager().disablePlugin(this);
			return;
		}

		MineListener.setupRegistry();
    UtilFile.CoreFile.createFiles();
		checkDepends();

	 tickLoop = taskHandler.new Task(){
			public void run(){
				for(TickListener t: tickListeners){
					if(t == null)continue;
					t.tick();
				}
			}
		}.runTimer(1,5);




		registerItems();
		itemMap.registerItemEvent();

		try {
			kitFileManager.loadKits();

		}catch(Exception e){
		  getLogger().warning("Failed Loading kits");
		  e.printStackTrace();
    }

    try {
      pointFileManager.loadllPoints();

    }catch(Exception e){
      getLogger().warning("Failed Loading points");
      e.printStackTrace();
    }

    try {
      mineFileManager.loadAll();

    }catch(Exception e){
      getLogger().warning("Failed Loading mines");
      e.printStackTrace();
    }
		try{
    	playerFileManager.loadAllOnline();
		}catch(Exception e){
			getLogger().warning("Failed Loading user");
			e.printStackTrace();
		}
		registerElements();
		hookManager.load();
		playerHandler.runTask();
		mineManager.startTask();
	}



	@Override
	public void onDisable ()
	{


		if(dirty)return
			         ;
		tickLoop.cancel();
		try {
			kitFileManager.saveKits();
		}catch(Exception e){
			getLogger().warning("Failed saving kits");
			e.printStackTrace();
		}

    try {
      mineFileManager.saveAll();
    }catch(Exception e){
      getLogger().warning("Failed saving mines");
      e.printStackTrace();
    }

		try {
			pointFileManager.saveAllPoints();

		}catch(Exception e){
			getLogger().warning("Failed Saving points");
			e.printStackTrace();
		}
		try {
			playerFileManager.saveAllUsers();
		}catch(Exception e){
			getLogger().warning("Failed Saving users");
			e.printStackTrace();
		}
		clean();


	}




///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/*

	GETTERS AND SETTERS

*/
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////





	public static MineManager getMineManager(){
		return getInstance().mineManager;
	}

	public static PointManager getPointManager(){
		return getInstance().pointManager;
	}

	public static ChatPlaceholderMap getChatPlaceholderMap(){return null;}//chatPlaceholderMap;}

	public static ConsoleCommandSender getConsole ()
	{
		return Bukkit.getServer().getConsoleSender();
	}

	public static PlayerHandler getPlayerHandler ()
	{
		return getInstance().playerHandler;
	}

	public static TaskHandler getTaskHandler ()
	{

		return getInstance().taskHandler;
	}

	public static ItemMap getItemMap ()
	{

		return getInstance().itemMap;
	}


  public static ShopView getShop(){
	  return getInstance().shop;
  }

	public static DataManager getDataManager ()
	{
		return getInstance().dataManager;
	}

	public static KitHandler getKitHandler ()
	{
		return getInstance().kitHandler;
	}

	public static ElementManager getElementManager(){ return getInstance().elementManager;}

	public static HookManager getHookManager(){
		return getInstance().hookManager;
	}

	public static KitFileManager getKitFileManager ()
	{
		return getInstance().kitFileManager;
	}

	public static MineFileManager getMineFileManager ()
	{
		return getInstance().mineFileManager;
	}

	public static PlayerFileManager getPlayerFileManager(){ return getInstance().playerFileManager;}

	public static PointFileManager getPointFileManager(){
		return getInstance().pointFileManager;
	}


	public static void addTickListener(TickListener e){
		getInstance().tickListeners.add(e);
	}

	public static void removeTickListener(TickListener e){
		getInstance().tickListeners.remove(e);
	}
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/*

	Utility Methods

*/
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private void checkDepends(){





	}

	@Override
	public void clean ()
	{
		tickLoop.cancel();
		playerHandler.clean();
		elementManager.clean();
		dataManager.clean();
		taskHandler.clean();
		itemMap.clean();
		kitHandler.clean();
		mineManager.clean();
		kitHandler = null;
		playerHandler = null;
		elementManager = null;
		taskHandler = null;
		dataManager = null;
		itemMap = null;
		mineManager = null;

		instance = Optional.empty();

	}




	protected void registerElements ()
	{

		elementManager.registerAllListener(
      new ChatListener(),
      new ElytraListener(),
      new MixedListener(),
			new MineListener(playerHandler,mineManager),

      shop,
      new ArmorListener(this)
    );

    elementManager.registerAll(playerHandler,
      new CommandJoin(),
      new CommandKit(kitHandler),
      new CommandLoad(),
      new CommandOpenInv(),
      new CommandProvisions(),
      new CommandRank(),
      new CommandRares(),
      new CommandShop(),
      new CommandTest(),
      new CommandAction(this),
      new CommandMsg(),
      new CommandWarps(),
      new CommandOpenInv(),
      new CommandMines(mineManager),
	    new CommandServer(this),
      new CommandCheckInv(),
	    new CommandAutoAction(playerHandler),
	    new CommandItemEdit()

    );


	}


	private boolean setupPermissions()
	{
		RegisteredServiceProvider<Permission> permissionProvider = getServer().getServicesManager().getRegistration(Permission.class);
		if (permissionProvider != null) {
			permission = permissionProvider.getProvider();
		}
		return (permission != null);
	}

	private boolean setupChat()
	{
		RegisteredServiceProvider<Chat> chatProvider = getServer().getServicesManager().getRegistration(Chat.class);
		if (chatProvider != null) {
			chat = chatProvider.getProvider();
		}

		return (chat != null);
	}

	private boolean setupEconomy()
	{
		RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(Economy.class);
		if (economyProvider != null) {
			economy = economyProvider.getProvider();
		}

		return (economy != null);
	}

	private  void registerItems ()
	{
		itemMap
				.register(
					new CustomItem(EnumCustomItem.SLOT_PLACEHOLDER.name())
					{

						@Override
						protected ItemStack build ()
						{
							//Bukkit.getLogger().warning("THIS WAS RAN");
							ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 8);
							ItemMeta im = item.getItemMeta();

							im.addItemFlags(ItemFlag.values());
							im.setDisplayName(" ");

							item.setItemMeta(im);
							return item;
						}


					})
				.register(

					new CustomItemRare(EnumCustomItem.PARKOUR_BOOTS.name(), RareType.ULTRA)
					{

						@Override
						protected ItemStack build ()
						{

							return buildCustomLeather(Color.TEAL, "&3&lParkour Boots", EquipmentSlot.FEET);
						}

						@Override
						public void registerEvent ()
						{
							getElementManager().register(new RareParkourBoots(this));
						}
					})
				.register(
				new CustomItemRare(EnumCustomItem.BOOMER_BOW.name(), RareType.LEGENDARY)
				{

					@Override
					protected ItemStack build ()
					{

						ItemStack item = new ItemStack(Material.BOW);
						item.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 39);
						item.addUnsafeEnchantment(Enchantment.ARROW_INFINITE, 1);
						ItemMeta im = item.getItemMeta();

						im.setUnbreakable(true);
						im.setDisplayName(Utils.colorCode("&e&lBOOMER BOW"));

						item.setItemMeta(im);
						item.getItemMeta();
						return item;

					}


				})
				.register(
				new CustomItemRare(EnumCustomItem.ARTEMIS_PLATE.name(), RareType.ULTRA)
				{

					@Override
					protected ItemStack build ()
					{
						return CustomItemRare.buildCustomLeather(Color.FUCHSIA, "&d&lArtemis Plate", EquipmentSlot.CHEST);
					}

					@Override
					public void registerEvent ()
					{
            getElementManager().register(new RareArtemisPlate(this));
					}
				})
				.register(
				new CustomItemRare(EnumCustomItem.HANSOLO_PANTS.name(), RareType.ULTRA)
				{

					@Override
					protected ItemStack build ()
					{
						return CustomItemRare.buildCustomLeather(Color.AQUA, "&b&lHanSolo Pants", EquipmentSlot.LEGS);
					}

					@Override
					public void registerEvent ()
					{
            getElementManager().register(new RareHanSoloPants(this));
					}
				})
				.register(
				new CustomItemRare(EnumCustomItem.HAWK_EYE.name(), RareType.ULTRA)
				{

					@Override
					protected ItemStack build ()
					{
						return CustomItemRare.buildCustomLeather(Color.BLACK, "&0&lHawk Eye", EquipmentSlot.HEAD);
					}

					@Override
					public void registerEvent ()
					{
            getElementManager().register(new RareHawkEye(this));
					}
				})
				.register(
				new CustomItemRare(EnumCustomItem.OPTIMIZER.name(), RareType.LEGENDARY)
				{

					@Override
					protected ItemStack build ()
					{
						ItemStack item = new ItemStack(Material.STONE_AXE);
						item.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 7);
						ItemMeta im = item.getItemMeta();

						im.setUnbreakable(true);
						im.setDisplayName(Utils.colorCode("&7&lOptimizer"));

						item.setItemMeta(im);
						return item;
					}

					@Override
					public void registerEvent ()
					{
            getElementManager().register(new RareOptimizer(this));
					}
				})
				.register(
				new CustomItemRare(EnumCustomItem.SUPER_CHEST.name(), RareType.ULTRA)
				{

					@Override
					protected ItemStack build ()
					{
						ItemStack item = new ItemStack(Material.ENDER_CHEST);
						item.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 7);
						ItemMeta im = item.getItemMeta();

						im.spigot().setUnbreakable(true);
						im.setDisplayName(Utils.colorCode("&7&lSuper Chest"));

						item.setItemMeta(im);
						return item;
					}

					@Override
					public void registerEvent ()
					{
            getElementManager().register(new RareSuperChest(this));
					}
				})
				.register(
				new CustomItemRare(EnumCustomItem.VADER_CAPE.name(), RareType.ULTRA)
				{

					@Override
					protected ItemStack build ()
					{
						return CustomItemRare.buildCustomLeather(Color.BLACK, "&0&lVader Cape", EquipmentSlot.LEGS);
					}

					@Override
					public void registerEvent ()
					{
            getElementManager().register(new RareVaderCape(this));
					}
				})
				.register(
				new CustomItemRare(EnumCustomItem.ZEUS_BOLT.name(), RareType.ULTRA)
				{

					@Override
					protected ItemStack build ()
					{
						ItemStack item = new ItemStack(Material.BLAZE_ROD);

						item.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 7);
						ItemMeta im = item.getItemMeta();

						im.setUnbreakable(true);
						im.setDisplayName(Utils.colorCode("&1&lZues Bolt"));

						item.setItemMeta(im);
						item.getItemMeta();
						return item;
					}

					@Override
					public void registerEvent ()
					{
            getElementManager().register(new RareZuesBolt(this));
					}
				}
				);




	}

	public static Logger logger(){
			return	getInstance().getLogger();
	}


}





