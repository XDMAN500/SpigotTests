package me.varmetek.munchymc.backend;

import me.varmetek.core.util.Cleanable;
import me.varmetek.core.util.Messenger;
import me.varmetek.munchymc.Main;
import me.varmetek.munchymc.util.UtilFile;
import me.varmetek.munchymc.util.UtilInventory;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by XDMAN500 on 1/14/2017.
 */
public final class DataManager implements Cleanable
{

  private final static String __ = File.separator;
  private final Random rand = new Random();
  protected Main plugin;
  private UserData userData;
  private KitData kitData;
  private PointData pointData;

  public DataManager (Main plugin){
    this.plugin = plugin;
    userData = new UserData();
    kitData = new KitData();
    pointData = new PointData();
  }

  public UserData asUserData (){
    return userData;
  }

  public KitData asKitData (){
    return kitData;
  }

  public PointData asPointData (){
    return pointData;
  }

  public void clean (){
    plugin = null;
    userData = null;

  }


  public void saveLocation (File file, String path, Location loc) throws IOException{
    Validate.notNull(file);
    Validate.notNull(loc, "Spawn location is null");
    if (!file.exists()){
      Messenger.debug("File is null @DataManager:saveLocation");

      return;

    }

    FileConfiguration config = YamlConfiguration.loadConfiguration(file);

    writeLocation(config, path, loc);

    config.save(file);

  }

  public void writeLocation (FileConfiguration config, String path, Location loc){

    Validate.notNull(loc, "location is null");

    config.set(path + ".x", loc.getX());
    config.set(path + ".y", loc.getY());
    config.set(path + ".z", loc.getZ());
    config.set(path + ".pitch", String.valueOf(loc.getPitch()));
    config.set(path + ".yaw", String.valueOf(loc.getYaw()));
    config.set(path + ".world", loc.getWorld().getName());

  }

  public Location readLocation (FileConfiguration config, String path){

    if (!config.isConfigurationSection(path))
      return null;

    return new Location(Bukkit.getWorld(config.getString(path + ".world")),

                         config.getDouble(path + ".x"), config.getDouble(path + ".y"), config.getDouble(path + ".z"),
                         Float.parseFloat(config.getString(path + ".yaw")), Float.parseFloat(config.getString(path + ".pitch"))

    );
  }

  public Location loadLocation (File file, String path){

    if (!file.exists()){
      return null;
    }

    FileConfiguration config = YamlConfiguration.loadConfiguration(file);
    return readLocation(config, path);
  }


  public void createCoreFiles (){
    CoreFile.createFiles();
  }

  public final class UserData
  {
    private UserData (){
    }


    public String getUserFolderName (PlayerSession user){
      return user.getUUID().toString();
    }

    public String getUserDataFileName (PlayerSession user){
      return getUserFolderName(user) + __ + "data.yml";
    }


    public File getUserFile (PlayerSession user) throws IllegalArgumentException, IOException{
      return getUserFile(user, "data.yml");
    }

    public File createUserFile (PlayerSession user) throws IllegalArgumentException, IOException{
      return createUserFile(user, "data.yml");
    }

    public File createUserFile (PlayerSession user, String extra) throws IllegalArgumentException, IOException{
      return UtilFile.create(getUserFile(user, extra));
    }


    public File getUserFile (PlayerSession user, String extra) throws IllegalArgumentException, IOException{
      String currentPath = getUserFolderName(user);

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


      return UtilFile.getFile(CoreFile.USER.getDirectory() + __ + currentPath + __ + extra);

    }


    // User


    public void saveUser (PlayerSession user) throws IOException{

      File file = createUserFile(user, "user.yml");
      FileConfiguration config = YamlConfiguration.loadConfiguration(file);
      config.set("data",user.getPlayerData());
      config.save(file);
      plugin.getLogger().info("Saving user (" + user.getName() + ")");


    }


    public void savePlayer (OfflinePlayer p) throws IOException{
      saveUser(plugin.getPlayerHandler().getSession(p));

    }


    public void saveAllUsers () throws IOException{

      for (PlayerSession user : plugin.getPlayerHandler().getAllUsers()) {
        try {

          saveUser(user);
        } catch (Exception e) {
          Bukkit.getLogger().warning("Could not save player " + user.getName() + " :" + e.getMessage());
        }
      }

    }


    public void saveAll (){
      for (Player p : Bukkit.getOnlinePlayers()) {
        try {
          savePlayer(p);
        } catch (Exception e) {
          Bukkit.getLogger().warning("Could not save player " + p.getName() + " :" + e.getMessage());
        }
      }
    }


    public void loadUser (PlayerSession user) throws IOException{


      File file = getUserFile(user, "user.yml");
      FileConfiguration config = YamlConfiguration.loadConfiguration(file);
      PlayerData data  = (PlayerData) config.get("data");
      user.setPlayerData(data);

      plugin.getLogger().info("Loading user (" + user.getName() + ")");

    }

    public void loadPlayer (Player user) throws IOException{
      loadUser(plugin.getPlayerHandler().getSession(user));
    }


    public void loadAll (){
      for (Player p : Bukkit.getOnlinePlayers()) {
        try {
          loadUser(plugin.getPlayerHandler().getSession(p));
        } catch (IOException e) {
          Bukkit.getLogger().warning("Could not load player " + p.getName() + " :" + e.getMessage());
        }

      }
    }


    public void reloadUsers () throws IOException{
      for (Player p : Bukkit.getOnlinePlayers()) {
        savePlayer(p);
        loadPlayer(p);
      }
    }


    /**
     * Write the inventory of a player to a file.
     */
    public void writeInventory (PlayerSession user){
      if (!user.isOnline()) return;


      File file;


      try {

        file = getUserFile(user);
        Bukkit.getLogger().info("Writing Inventory " + user.getName());
        Inventory inv = user.getPlayer().get().getInventory();
        FileConfiguration playerConfig = YamlConfiguration.loadConfiguration(file);
        playerConfig.set("inv", UtilInventory.toMap(inv));
        playerConfig.save(file);
        Bukkit.getLogger().info("Finished Writing Inventory " + user.getName());

      } catch (Exception e) {
        e.printStackTrace();
        Bukkit.getLogger().info("Faile to Write Inventory " + user.getName());

      }


    }


    public void readInventory (PlayerSession user){
      if (!user.isOnline()) return;


      File file;

      try {

        file = getUserFile(user);
        Bukkit.getLogger().info("Writing Inventory " + user.getName());
        Inventory inv = user.getPlayer().get().getInventory();
        inv.clear();
        FileConfiguration playerConfig = YamlConfiguration.loadConfiguration(file);
        if (playerConfig.contains("inv")){
          MemorySection ser = (MemorySection) playerConfig.get("inv");
          if (ser == null){
            Bukkit.getLogger().info("Failed to read Inventory " + user.getName());
          } else {
            UtilInventory.fromMap(ser, inv);
          }
          /*ConfigurationSection cs = playerConfig.getConfigurationSection("inv");
					cs.getKeys(false).forEach((String d) ->{
						Integer in =Utils.getInt(d);
						if(in != null &&  in.intValue()<inv.getSize() ){
							inv.setItem(in.intValue(),cs.getItemStack(d));
						}
					});*/
        } else {
          Bukkit.getLogger().info("Failed to read Inventory " + user.getName());
        }


      } catch (Exception e) {
        e.printStackTrace();
        Bukkit.getLogger().info("Failed to read Inventory " + user.getName());
        return;
      }


    }


  }

  public final class KitData
  {
    private static final String
      INV = "inv",
      EFFECTS = "effects";

    public String getKitDirectory (String kit){
      return CoreFile.KITS.getDirectory() + __ + kit + ".yml";
    }


    public File getKitFile (String kit) throws IllegalArgumentException, IOException{

      return UtilFile.getFile(getKitDirectory(kit));
    }

    public void saveKit (String name) throws IOException{

      saveKit(name, getKitFile(name));
    }


    private void saveKit (String name, File f) throws IOException{
      if (!f.exists()){
        UtilFile.create(f);
      }
      Validate.isTrue(plugin.getKitHandler().isKit(name), "Could not save kit \"" + name + "\" because it does not exist");
      Kit kit = plugin.getKitHandler().getKit(name).get();

      YamlConfiguration config = YamlConfiguration.loadConfiguration(f);

      config.set(INV, kit.getInv());
      config.set(EFFECTS, kit.getEffects());
      config.save(f);


    }


    public void saveKits () throws IOException{
      for (String k : plugin.getKitHandler().getKits().keySet()) {
        try {
          saveKit(k);
        } catch (Exception e) {
          Bukkit.getLogger().warning("Failed to save kit " + k);
          e.printStackTrace();
        }
      }
    }




    public Kit loadKit (String k) throws IOException{


      return loadKit(k, getKitFile(k));
    }

    private Kit loadKit (String name, File f) throws IOException{


      Validate.isTrue(f.exists(),"Could not load kit \""+ name+"\" from non-existant file" );

      Map<Integer,ItemStack> inv = Collections.EMPTY_MAP;
      List<PotionEffect> effects = Collections.EMPTY_LIST;

      YamlConfiguration config = YamlConfiguration.loadConfiguration(f);
      Object sec = config.get(INV);
      Bukkit.getLogger().info( "Kit \""+name+"\"load: Has effects? "+config.contains(EFFECTS));
      Bukkit.getLogger().info("Kit \""+name+"\"load: Is null? "+ (config.get(EFFECTS) == null));
      try {
        Bukkit.getLogger().info("Kit \"" + name + "\"load: WHat type? " + (config.get(EFFECTS).getClass()));
        List<?> list =((List<?>)(config.get(EFFECTS)));
        Bukkit.getLogger().info("Kit \"" + name + "\"load: Empty?? " + list.isEmpty());
        if(!list.isEmpty()){
          Bukkit.getLogger().info("Kit \"" + name + "\"load: inside? " + list.get(0).getClass());
        }
      }catch(NullPointerException ex){}

      List<PotionEffect> list = (List<PotionEffect>)config.get(EFFECTS);



      if (sec instanceof MemorySection){
        inv = UtilInventory.toInvMap((MemorySection) sec);
      }

      /*if (list != null){
        effects = convertEffects(list);
      }*/
      Kit kit = new Kit.Builder().setInventory(inv).setEffects(list).build();
      plugin.getKitHandler().setKit(name, kit);
      return kit;
    }

    private Kit loadKit (File f) throws IOException{

      if (!f.exists() || !f.getName().endsWith(".yml")) throw new IllegalArgumentException("Invalid file for kit");

      String name = f.getName().substring(0, f.getName().lastIndexOf('.'));


      return loadKit(name, f);
    }

    public void loadKits () throws IOException{

      File f = new File(plugin.getDataFolder(), CoreFile.KITS.getDirectory());
      if(!f.exists()){
        UtilFile.create(f);
        Bukkit.getLogger().warning("Folder for kits does not exist. Creating a blank.");
      }

      for (File file : f.listFiles()) {
        try {
          loadKit(file);
        } catch (IOException e) {
          if (file.getName().endsWith(".yml")){
            Bukkit.getLogger().info("Could not load kit from file " + f.getName());
            e.printStackTrace();
          }
        }
      }

    }

    public void removeKit (String k) throws IOException{
      File f = getKitFile(k);
      if (f.exists()){
        f.delete();
      }
    }

    public void reloadKits () throws IOException{
      plugin.getKitHandler().clear();
      loadKits();

    }

    private List<PotionEffect> convertEffects (List<Map<?,?>> input){
      List<PotionEffect> output = new ArrayList<>();

      for (Map<?,?> effect : input) {
        if (effect == null) continue;
        output.add(new PotionEffect((Map<String,Object>) effect));
      }
      return output;

    }


  }

  public final class PointData
  {


    public Point loadPoint (String name) throws IOException{
      PointManager manager = plugin.getPointManager();
      File file = CoreFile.Points.file;

      if (!file.exists()){
        UtilFile.create(file);
      }

      FileConfiguration config = YamlConfiguration.loadConfiguration(file);
      Validate.isTrue(config.isConfigurationSection(name));
      ConfigurationSection sec = config.getConfigurationSection(name);
      boolean isWarp = sec.isBoolean("warp");
      Object result = sec.get("location");
      Bukkit.getLogger().info("Point stuff Load: "+ sec.get("location").getClass());

      Validate.isTrue(result instanceof Location);



      Location loc = (Location)result;
      Point p = new Point.Builder(loc).build();
      manager.setPoint(name, p);
      if (isWarp) manager.setWarp(name);
      return p;



    }

    public boolean savePoint (String name) throws IOException{
      PointManager manager = plugin.getPointManager();
      Validate.isTrue(manager.pointExist(name), "Point \"" + name + "\" cannot be saved as it does not exist");


      File file = CoreFile.Points.file;
      if (!file.exists()){
        UtilFile.create(file);
      }
      FileConfiguration config = YamlConfiguration.loadConfiguration(file);
      Point point = manager.getPoint(name).get();
      config.set(name + ".location", point.getLocation());
      config.set(name + ".warp", manager.isWarp(name));

      config.save(file);

      return true;
    }

    public void saveAllPoints () throws IOException{
      PointManager manager = plugin.getPointManager();
      File file = CoreFile.Points.file;
      if (!file.exists()){
        UtilFile.create(file);
      }

      FileConfiguration config = YamlConfiguration.loadConfiguration(file);
      //Cleaning
      for (String key : config.getKeys(false)) {
        config.set(key, null);
      }

      config.save(file);
      //Saving
      for (String name : manager.getPoints().keySet()) {
        try {
          ///Bukkit.getLogger().warning("Saving point \""+ name+"\"");
          savePoint(name);
        } catch (IOException expected) {
          Bukkit.getLogger().warning("Error saving point \""+ name+"\"");
          expected.printStackTrace();

        }


      }

    }

    public void loadllPoints () throws IOException{
      File file = CoreFile.Points.file;
      if(!file.exists()){
        Bukkit.getLogger().warning("File for points does not exist. Creating a blank.");
        UtilFile.create(file);
        return;
      }

      FileConfiguration config = YamlConfiguration.loadConfiguration(file);

      //Loading
      for (String key : config.getKeys(false)) {
        try {
          loadPoint(key);
        } catch (IOException expected) {
          Bukkit.getLogger().warning("Error saving point \""+ key+"\"");
          expected.printStackTrace();

        }

      }


    }

  }

  private void validateTrue (boolean c, String reason) throws IOException{
    if (!c) throw new IOException(reason);
  }

  private enum CoreFile
    {
      WARPS("", ".yml"), LOC("", ".yml"), CONFIG("", ".yml"), USER("", __), USERREG(USER.getDirectory(), ".yml"),
      KITS("", __), Points("", ".yml");

      private String path;
      private File file = null;
      private String suffix;

      CoreFile (String argPath, String suffix){
        this.suffix = suffix;
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

      public String getPath (){
        return path;
      }

      public String getName (){
        return name() + suffix;
      }

      public String getDirectory (){

        return path + __ + getName();
      }

      public boolean isFile (){
        return UtilFile.isFile(getName());
      }

      public File getFile (){
        return file == null ?
                 (isFile()
                    ? new File(Main.get().getDataFolder(), getDirectory()) : null) : file;
      }


      public static void createFiles (){

        for (CoreFile cf : values()) {


          if (!cf.isFile()){

            continue;
          }

          if (!cf.getFile().exists())
            try {
              UtilFile.create(cf.getFile());


            } catch (Exception e) {
              try {
                Bukkit.getLogger().warning("Could not create file " + cf.name() + " | " + e.getMessage());
                Bukkit.getLogger().warning(cf.name() + ": " + cf.getFile().getAbsolutePath() + " | " + e.getMessage());
                Bukkit.getLogger().warning(cf.name() + ":" + cf.path);
                e.printStackTrace();
              } catch (Exception ex) {
                Bukkit.getLogger().warning("WTF is this? - " + ex.getMessage());
              }
            }

        }
      }
    }
  }