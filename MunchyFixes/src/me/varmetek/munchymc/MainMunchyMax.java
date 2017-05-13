package me.varmetek.munchymc;

import ca.thederpygolems.armorequip.ArmorListener;
import io.netty.util.internal.ConcurrentSet;
import me.varmetek.core.commands.CmdCommand;
import me.varmetek.core.item.CustomItem;
import me.varmetek.core.item.ItemMap;
import me.varmetek.core.service.Element;
import me.varmetek.core.service.ElementManager;
import me.varmetek.core.util.PluginCore;
import me.varmetek.core.util.TaskHandler;
import me.varmetek.munchymc.backend.*;
import me.varmetek.munchymc.backend.hooks.HookManager;
import me.varmetek.munchymc.backend.mines.MineManager;
import me.varmetek.munchymc.backend.test.CustomItemRare;
import me.varmetek.munchymc.backend.test.EnumCustomItem;
import me.varmetek.munchymc.commands.*;
import me.varmetek.munchymc.listeners.ChatListener;
import me.varmetek.munchymc.listeners.ElytraListener;
import me.varmetek.munchymc.listeners.MixedListener;
import me.varmetek.munchymc.listeners.TickListener;
import me.varmetek.munchymc.rares.*;
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
import org.bukkit.event.Listener;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.scheduler.BukkitTask;

import java.util.Optional;
import java.util.Set;

public final class MainMunchyMax extends PluginCore
{
	private static Optional<MainMunchyMax> instance = Optional.empty();

	private ItemMap itemMap;
	private HookManager hookManager;
	private PointManager pointManager;
	private PlayerHandler playerHandler;
	private TaskHandler taskHandler;
	private DataManager dataManager;
	private ElementManager elementManager;
	private KitHandler kitHandler;
	private BukkitTask tickLoop;
	private Set<TickListener> tickListeners;
	public ShopView shop;
	private MineManager mineManager;
	Economy economy;
	Chat chat;
	Permission permission;

	private boolean dirty = false;

	public MainMunchyMax (){
		boolean dirty = instance.isPresent() && this != instance.get();

		if(dirty){
			throw new IllegalStateException("Singleton has already been set");
		}else{
			instance = Optional.of(this);
		}

	}

	public static MainMunchyMax getInstance(){
		return instance.orElseThrow( ()->{ return new IllegalStateException("Singleton has not been set");});
	}


	@Override
	public void onLoad ()
	{
		if(dirty)return;

		ConfigurationSerialization.registerClass(PlayerData.class);
		elementManager = new ElementManager(this);
		kitHandler = new KitHandler();
		itemMap = new ItemMap();
		dataManager = new DataManager(this);
		taskHandler = new TaskHandler(this);
		playerHandler = new PlayerHandler(this);
		hookManager = new HookManager(this);
		pointManager = new PointManager(this);
		mineManager = new MineManager(this);
		shop = new ShopView(this);
		tickListeners = new ConcurrentSet<>();
	}
	public void onEnable (){
		if(dirty){
			getLogger().warning("Plugin could not enable correctly");
			getServer().getPluginManager().disablePlugin(this);
			return;
		}

    dataManager.createCoreFiles();
		checkDepends();



		tickLoop = taskHandler.runTimer(()->{
			tickListeners.forEach((t)->

				t.tick()
			);
		},1,5);


		registerItems(this);
		itemMap.registerItemEvent();

		try {
			dataManager.asKitData().loadKits();

		}catch(Exception e){
		  getLogger().warning("Failed Loading kits");
		  e.printStackTrace();
    }

    try {
      dataManager.asPointData().loadllPoints();

    }catch(Exception e){
      getLogger().warning("Failed Loading points");
      e.printStackTrace();
    }
		dataManager.asUserData().loadAll();
		registerElements();
		hookManager.load();


	}



	@Override
	public void onDisable ()
	{
		if(dirty)return;
		try {
			dataManager.asKitData().saveKits();
		}catch(Exception e){
			getLogger().warning("Failed saving kits");
			e.printStackTrace();
		}

		try {
			dataManager.asPointData().saveAllPoints();

		}catch(Exception e){
			getLogger().warning("Failed Saving points");
			e.printStackTrace();
		}
		dataManager.asUserData().saveAll();
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
		kitHandler = null;
		playerHandler = null;
		elementManager = null;
		taskHandler = null;
		dataManager = null;
		itemMap = null;
		instance = Optional.empty();

	}

	public void addTickListener(TickListener e){
		tickListeners.add(e);
	}

	public void removeTickListener(TickListener e){
		tickListeners.remove(e);
	}
	public static MainMunchyMax get ()
	{
		return getPlugin(MainMunchyMax.class);
	}

	protected void registerElements ()
	{
	//	cmdManager.register( new CommandJoin(this));

		elementManager.registerAll(
      new ChatListener(this),
      new ElytraListener(),
      new MixedListener(this),
      playerHandler,
      shop,
      new Element(){
        @Override
        public CmdCommand[] supplyCmd (){
          return null;
        }

        @Override
        public Listener supplyListener (){
          return new ArmorListener(plugin);
        }

        @Override
        public void clean (){

        }
      }
    );

    elementManager.registerAll(
      new CommandJoin(this),
      new CommandKit(this),
      new CommandLoad(this),
      new CommandOpenInv(this),
      new CommandProvisions(this),
      new CommandRank(this),
      new CommandRares(this),
      new CommandShop(this),
      new CommandTest(this),
      new CommandAction(this),
      new CommandMsg(this),
      new CommandWarps(this),
      new CommandOpenInv(this)

    )


		;


		/*elementService.registerListener(new ArmorListener(this));
		elementService.registerListener(new ChatListener(this));
		elementService.registerListener(new ElytraListener());
		elementService.registerListener(new MixedListener(this));
		elementService.registerListener(userHandler);
		elementService.registerListener(shop);

		elementService.registerCommand(new CommandContainer("rares", new CommandRares(this)));
		elementService.registerCommand(new CommandContainer("test", new CommandTest(this)));
		elementService.registerCommand(new CommandContainer("save", new CommandSave(this)));
		elementService.registerCommand(new CommandContainer("load", new CommandLoad(this)));
		elementService.registerCommand(new CommandContainer("kit", new CommandKit(this)));
		elementService.registerCommand(new CommandContainer("run", new CommandProvisions(this)));
		elementService.registerCommand(new CommandContainer("openinv", new CommandOpenInv(this)));
		elementService.registerCommand(new CommandContainer("shop", new CommandShop(this)));*/


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

	private  void registerItems (MainMunchyMax plug)
	{
		itemMap
				.register(
					new CustomItem(EnumCustomItem.SLOT_PLACEHOLDER.name())
					{

						@Override
						protected ItemStack build ()
						{
							Bukkit.getLogger().warning("THIS WAS RAN");
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
							plug.getElementManager().register(new RareParkourBoots(this,plug));
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
            plug.getElementManager().register(new RareArtemisPlate(this,plug));
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
            plug.getElementManager().register(new RareHanSoloPants(this,plug));
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
            plug.getElementManager().register(new RareHawkEye(this,plug));
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
            plug.getElementManager().register(new RareOptimizer(this,plug));
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
            plug.getElementManager().register(new RareSuperChest(this,plug));
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
            plug.getElementManager().register(new RareVaderCape(this,plug));
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
            plug.getElementManager().register(new RareZuesBolt(this,plug));
					}
				}
				);




	}
}





