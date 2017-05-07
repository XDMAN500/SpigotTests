package me.varmetek.munchymc.backend;

import me.varmetek.core.util.Cleanable;
import me.varmetek.core.util.Messenger;
import me.varmetek.munchymc.Main;
import me.varmetek.munchymc.backend.kit.Kit;
import me.varmetek.munchymc.util.UtilFile;
import me.varmetek.munchymc.util.UtilInventory;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.Set;

/**
 * Created by XDMAN500 on 1/14/2017.
 */
public final class DataManager implements Cleanable
{

	private final static String __ = File.separator;
	private final Random  rand = new Random();
	protected Main plugin;
	private UserData  userData;
	private KitData  kitData;

	public DataManager(Main plugin){
		this.plugin = plugin;
		userData =  new UserData();
		kitData = new KitData();
	}
	public UserData asUserData(){
		return userData;
	}
	public KitData asKitData(){
		return kitData;
	}

	public void clean(){
		plugin = null;
		userData = null;

	}



	public  void saveLocation (File file, String path, Location loc) throws IOException{
		Validate.notNull(file);
		Validate.notNull(loc, "Spawn location is null");
		if (!file.exists())
		{
			Messenger.debug("File is null @DataManager:saveLocation");

			return;

		}

		FileConfiguration config = YamlConfiguration.loadConfiguration(file);

		writeLocation(config, path, loc);

		config.save(file);

	}

	public  void writeLocation (FileConfiguration config, String path, Location loc){

		Validate.notNull(loc, "location is null");

		config.set(path + ".x", loc.getX());
		config.set(path + ".y", loc.getY());
		config.set(path + ".z", loc.getZ());
		config.set(path + ".pitch", String.valueOf(loc.getPitch()));
		config.set(path + ".yaw", String.valueOf(loc.getYaw()));
		config.set(path + ".world", loc.getWorld().getName());

	}

	public  Location readLocation (FileConfiguration config, String path){

		if (!config.isConfigurationSection(path))
			return null;

		return new Location(Bukkit.getWorld(config.getString(path + ".world")),

				                   config.getDouble(path + ".x"), config.getDouble(path + ".y"), config.getDouble(path + ".z"),
				                   Float.parseFloat(config.getString(path + ".yaw")), Float.parseFloat(config.getString(path + ".pitch"))

		);
	}

	public  Location loadLocation (File file, String path){

		if (!file.exists())
		{
			return null;
		}

		FileConfiguration config = YamlConfiguration.loadConfiguration(file);
		return readLocation(config, path);
	}



	public  Point loadPoint (String name){
		/*File myFile = CoreFile.LOC.file;
		if (!myFile.exists())
			return null;
		FileConfiguration config = YamlConfiguration.loadConfiguration(myFile);
		Location loc = null;
		DataSection ds = null;
		if (!config.contains(name))
			return null;
		if (config.contains(name + ".location"))

			loc = readLocation(config, name + ".location");

		if (config.contains(name + ".data"))
			ds = DataSection.parse(config.getString(name + ".data"));

		Point p = Point.createPoint(name, loc);
		if (p != null)
			p.setData(ds);*/


		return null;// loadLocation(CoreFile.LOC.file,name);
	}

	public  boolean savePoint (Point p) throws IOException{

	/*	File myFile = CoreFile.LOC.file;
		if (!myFile.exists())
			myFile.createNewFile();
		FileConfiguration warpConfig = YamlConfiguration.loadConfiguration(myFile);

		writeLocation(warpConfig, p.name + ".location", p.location);
		warpConfig.set(p.name + ".data", p.data.toString());
		warpConfig.save(myFile);

		// Bukkit.broadcastMessage("WARP CREATED");*/
		return true;
	}

	public  Set<String> getPointList (){

		File myFile = CoreFile.LOC.file;
		if (myFile.exists())
		{
			FileConfiguration warpConfig = YamlConfiguration.loadConfiguration(myFile);
			if (warpConfig.getConfigurationSection("").getKeys(false) == null)
			{
				Messenger.debug("No list");
				return null;
			}
			if (warpConfig.getConfigurationSection("").getKeys(false).size() == 0)
			{
				Messenger.debug("No locs");
				return null;
			}

			return warpConfig.getConfigurationSection("").getKeys(false);

		}
		return null;
	}

	public  boolean doesPointExist (String warp)
	{
		return getPointList().contains(warp);
	}

	public  boolean removePoint (String name) throws IOException{
		File myFile = CoreFile.CONFIG.file;
		if (!myFile.exists())
			return false;
		FileConfiguration warpConfig = YamlConfiguration.loadConfiguration(myFile);
		warpConfig.set("name", null);

		warpConfig.save(myFile);

		return true;

		// User
	}

	public void createCoreFiles(){
		CoreFile.createFiles();
	}

	public final class UserData{
		private  UserData(){}



		public  String getUserFolderName (User user)
		{
			return user.getUUID().toString();
		}
		public  String getUserDataFileName (User user)
		{
			return getUserFolderName(user) + __ + "data.yml";
		}



		public  File getUserFile(User user) throws IllegalArgumentException, IOException{
			return getUserFile(user, "data.yml");
		}
		public  File createUserFile(User user) throws IllegalArgumentException, IOException{
			return createUserFile(user, "data.yml");
		}

		public File createUserFile(User user, String extra) throws IllegalArgumentException, IOException{
			return UtilFile.create( getUserFile(user,extra));
		}


		public File getUserFile(User user, String extra) throws IllegalArgumentException, IOException{
			String currentPath = getUserFolderName (user)  ;

			/*FileConfiguration  regis = YamlConfiguration.loadConfiguration(UtilFile.getFile(CoreFile.USERREG.getDirectory()));

			String previousPath = regis.getString(user.getUUID().toString(),"");


			// if user has changed names
			if(!previousPath.equals(currentPath)){
				File olddir= new File(Main.get().getDataFolder(),CoreFile.USER.getDirectory() + __ + previousPath);
				File newdir= new File(Main.get().getDataFolder(),CoreFile.USER.getDirectory() + __ + currentPath);
				olddir.renameTo(newdir);
				// Rename folder



				// Save change in registry
				regis.set(user.getUUID().toString(),currentPath );
				regis.save(CoreFile.USERREG.getFile());

			}*/


			return UtilFile.getFile( CoreFile.USER.getDirectory() + __ + currentPath +__+ extra);

		}




		// User






		public  void saveUser (User user) throws IOException
		{

			File file = createUserFile(user,"user.yml");
			FileConfiguration config = YamlConfiguration.loadConfiguration(file);
			config.set("joinMsg", user.getJoinMessage());
			config.set("leaveMsg", user.getLeaveMessage());
			config.save(file);
			plugin.getLogger().info("Saving user ("+user.getName()+")");


		}





		public void savePlayer (OfflinePlayer p) throws IOException
		{
			saveUser(plugin.getUserHandler().getUser(p));

		}





		public  void saveAllUsers () throws IOException
		{

			for (User user : plugin.getUserHandler().getAllUsers())
			{
				try
				{

				saveUser(user);
				}catch(Exception e){
					Bukkit.getLogger().warning( "Could not save player "+ user.getName() + " :" + e.getMessage());
				}
			}

		}






		public void saveAll () {
			for (Player p : Bukkit.getOnlinePlayers())
			{
				try
				{
					savePlayer(p);
				}catch(Exception e){
					Bukkit.getLogger().warning( "Could not save player "+ p.getName() + " :" + e.getMessage());
				}
			}
		}


		public void loadUser (User user) throws IOException
		{


			File file = getUserFile(user,"user.yml");
			FileConfiguration config = YamlConfiguration.loadConfiguration(file);
			user.setJoinMessage( config.getString("joinMsg",user.getJoinMessage()));
			user.setLeaveMessage( config.getString("leaveMsg",user.getLeaveMessage()));
			//config.save(file);

			plugin.getLogger().info("Loading user ("+user.getName()+")");

		}

		public void loadPlayer (Player user)throws IOException
		{
			loadUser(plugin.getUserHandler().getUser(user));
		}


		public  void loadAll ()
		{
			for (Player p : Bukkit.getOnlinePlayers())
			{
				try{
					loadUser(plugin.getUserHandler().getUser(p));
				}catch(IOException e){
					Bukkit.getLogger().warning( "Could not load player "+ p.getName() + " :" + e.getMessage());
				}

			}
		}





		public  void reloadUsers () throws IOException
		{
			for (Player p : Bukkit.getOnlinePlayers())
			{
				savePlayer(p);
				loadPlayer(p);
			}
		}



		/**
		 * Write the inventory of a player to a file.
		 *
		 * */
		public void writeInventory (User user){
			if (!user.isOnline()) return;


			File file;


			try
			{

				file = getUserFile(user);
				Bukkit.getLogger().info("Writing Inventory "+ user.getName());
				Inventory inv = user.getPlayer().get().getInventory();
				FileConfiguration playerConfig = YamlConfiguration.loadConfiguration(file);
				playerConfig.set("inv", UtilInventory.toMap(inv));
				playerConfig.save(file);
				Bukkit.getLogger().info("Finished Writing Inventory "+ user.getName());

			} catch (Exception e)
			{
				e.printStackTrace();
				Bukkit.getLogger().info("Faile to Write Inventory "+ user.getName());

			}


		}


		public void readInventory (User user){
			if (!user.isOnline()) return;


			File file;

			try
			{

				file = getUserFile(user);
				Bukkit.getLogger().info("Writing Inventory "+ user.getName());
				Inventory inv = user.getPlayer().get().getInventory();
				inv.clear();
				FileConfiguration playerConfig = YamlConfiguration.loadConfiguration(file);
				if(playerConfig.contains("inv")){
					MemorySection ser = (MemorySection)playerConfig.get("inv");
					if(ser == null){
						Bukkit.getLogger().info("Failed to read Inventory "+ user.getName());
					}else{
						UtilInventory.fromMap(ser,inv);
					}
					/*ConfigurationSection cs = playerConfig.getConfigurationSection("inv");
					cs.getKeys(false).forEach((String d) ->{
						Integer in =Utils.getInt(d);
						if(in != null &&  in.intValue()<inv.getSize() ){
							inv.setItem(in.intValue(),cs.getItemStack(d));
						}
					});*/
				}else{
					Bukkit.getLogger().info("Failed to read Inventory "+ user.getName());
				}



			} catch (Exception e)
			{
				e.printStackTrace();
				Bukkit.getLogger().info("Failed to read Inventory "+ user.getName());
				return;
			}


		}


	}
	public final class KitData{
		public  String getKitDirectory (Kit  kit)
		{
			return  CoreFile.KITS.getDirectory() + __+ kit.ID()+".yml";
		}
		public  String getKitDirectory (String   kit)
		{
			return  CoreFile.KITS.getDirectory() + __+ kit+".yml";
		}

		public File getKitFile(Kit kit) throws IllegalArgumentException, IOException{

			return UtilFile.getAndCreate( getKitDirectory(kit));
		}
		public File getKitFile(String kit) throws IllegalArgumentException, IOException{

			return UtilFile.getFile( getKitDirectory(kit));
		}

		public void saveKit(Kit k)throws IOException{
				saveKit( k, getKitFile(k));
		}


		private void saveKit(Kit k, File f) throws IOException{


				YamlConfiguration config =  YamlConfiguration.loadConfiguration(f);

				config.set("inv", k.getInv());
				config.save(f);


		}



		public void saveKits(){
			for(Kit k: plugin.getKitHandler().values()){
				try{
					saveKit(k);
				}catch(Exception e){
					Bukkit.getLogger().warning("Failed to save kit " + k.ID());
				}
			}
		}



		public Kit loadKit(String  k) throws IOException{

				File f = getKitFile(k);
				if(!f.exists())throw new IOException("Kit file doesnt exist for kit " +k);

				YamlConfiguration config = YamlConfiguration.loadConfiguration(f);

				Object sec = config.get("inv");
				if(sec instanceof  MemorySection){
					return new Kit(k, UtilInventory.toInvMap((MemorySection)sec));
				}else{

				}


			return null;
		}
		private Kit loadKit(File f) throws IOException{

			if(!f.exists() || !f.getName().endsWith(".yml")) throw new IllegalArgumentException("Invalid file for kit");


			YamlConfiguration config = YamlConfiguration.loadConfiguration(f);
			String name = f.getName().substring(0,f.getName().lastIndexOf('.'));
			Object sec = config.get("inv");
			if(sec instanceof  MemorySection){
				return new Kit(name, UtilInventory.toInvMap((MemorySection)sec));
			}else{

			}


			return null;
		}

		public void loadKits(){

			File f =  new File(plugin.getDataFolder(),CoreFile.KITS.getDirectory());
			if(!f.exists()) return;

			for(File file: f.listFiles()){
				try{
					plugin.getKitHandler().register(loadKit(file));
				}catch(Exception e){
					if(file.getName().endsWith(".yml")){
						Bukkit.getLogger().info("Could not load kit from file "+ f.getName());
					}
				}
			}

		}
		public void removeKit(String k) throws IOException{
			File f = getKitFile(k);
			if(f.exists()){
				f.delete();
			}
		}

		public void reloadKits(){
			plugin.getKitHandler().unregisterAll();
			loadKits();

		}
	}

	private enum CoreFile{
		WARPS("", ".yml"), LOC("", ".yml"), CONFIG("", ".yml"), USER("",__), USERREG(USER.getDirectory(),".yml"),
		KITS("",__);

		private String path;
		private File file = null;
		private String suffix;

		CoreFile (String argPath, String suffix)
		{
			this.suffix =suffix;
			this.path = argPath;
			file = getFile();
			//file =new File(Main.get().getDataFolder(), name() + suffix);

			/*TaskHandler.Async.run(()->{

					try
					{
						file.createNewFile();
					} catch (IOException e)
					{
						e.printStackTrace();
					}


				} );*/
		}

		public String getPath ()
		{
			return path;
		}
		public String getName(){
			return name() + suffix;
		}
		public String getDirectory ()
		{

			return path +  __ + getName();
		}
		public boolean isFile(){
			return UtilFile.isFile(getName());
		}
		public File getFile()
		{
			return file == null ?
					       (isFile()
							        ? new File(Main.get().getDataFolder(), getDirectory()):null  ): file;
		}





		public static void createFiles ()
		{

			for (CoreFile cf : values())
			{


				if(!cf.isFile()){

					continue;
				}

				if (!cf.getFile().exists())
					try
					{
						UtilFile.create(cf.getFile());


					}catch(Exception e){
						try
						{
							Bukkit.getLogger().warning("Could not create file " + cf.name() + " | " + e.getMessage());
							Bukkit.getLogger().warning(cf.name() + ": " + cf.getFile().getAbsolutePath() + " | " + e.getMessage());
								Bukkit.getLogger().warning(cf.name() + ":" + cf.path);
							e.printStackTrace();
						}catch(Exception ex){
							Bukkit.getLogger().warning("WTF is this? - "+ ex.getMessage());
						}
					}

			}
		}
	}
}