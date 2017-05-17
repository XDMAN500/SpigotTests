package me.varmetek.munchymc.backend.file;

import me.varmetek.munchymc.MunchyMax;
import me.varmetek.munchymc.backend.PlayerData;
import me.varmetek.munchymc.backend.PlayerHandler;
import me.varmetek.munchymc.backend.PlayerSession;
import me.varmetek.munchymc.backend.exceptions.ConfigException;
import me.varmetek.munchymc.util.UtilFile;
import me.varmetek.munchymc.util.UtilInventory;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.io.File;
import java.io.IOException;

/**
 * Created by XDMAN500 on 5/15/2017.
 */
public final class PlayerFileManager implements FileManager
{
  private final static String __ = File.separator;
  private final static UtilFile.CoreFile FILE = UtilFile.CoreFile.USER;
  private PlayerHandler playerHandler;

  public PlayerFileManager (PlayerHandler playerHandler){
    this.playerHandler = playerHandler;
  }

  private void logInfo(String info){
    MunchyMax.getInstance().getLogger().info(info);
  }

  private void logWarning(String info){
    MunchyMax.getInstance().getLogger().info(info);
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


    return UtilFile.getFile(FILE.getDirectory() + __ + currentPath + __ + extra);

  }


  // User


  public void saveUser (PlayerSession user) throws ConfigException{
    try {


      File file = createUserFile(user, "user.yml");
      FileConfiguration config = YamlConfiguration.loadConfiguration(file);
      config.set("data", user.getPlayerData());
      config.save(file);
      logInfo("Saving user (" + user.getName() + ")");
    }catch(Exception e){
      throw new ConfigException(e);
    }


  }


  public void savePlayer (OfflinePlayer p) throws ConfigException{
    saveUser(playerHandler.getSession(p));

  }


  public void saveAllUsers (){

    for (PlayerSession user : playerHandler.getAllUsers()) {
      try {

        saveUser(user);
      } catch (ConfigException e) {
        logWarning("Could not save player " + user.getName() + " :" + e.getMessage());
      }
    }

  }


  public void saveAllOnline (){
    for (Player p : Bukkit.getOnlinePlayers()) {
      try {
        savePlayer(p);
      } catch (ConfigException e) {
        logWarning("Could not save player " + p.getName() + " :" + e.getMessage());
      }
    }
  }


  public void loadUser (PlayerSession user) throws ConfigException{
    PlayerData data = null;

    try {
      File file = getUserFile(user, "user.yml");
      FileConfiguration config = YamlConfiguration.loadConfiguration(file);


      data =  (PlayerData) config.get("data");

    }catch(Exception e){
      throw new ConfigException(e);
    }
    logInfo("Loading user (" + user.getName() + ")");
    user.setPlayerData(data);

  }

  public void loadPlayer (Player user) throws ConfigException{
    loadUser(playerHandler.getSession(user));
  }


  public void loadAllOnline (){
    for (Player p : Bukkit.getOnlinePlayers()) {
      try {
        loadUser(playerHandler.getSession(p));
      } catch (ConfigException e) {
        logWarning("Could not load player " + p.getName() + " :" + e.getMessage());
      }

    }
  }


  public void reloadUsers () throws IOException{
    for (Player p : Bukkit.getOnlinePlayers()) {
      try {
        savePlayer(p);
        loadPlayer(p);
      }catch(ConfigException e){
        logWarning("Could not reload player " + p.getName() + " :" + e.getMessage());
      }
    }
  }


  /**
   * Write the inventory of a player to a file.
   */
  public void writeInventory (PlayerSession user) throws ConfigException{
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
      logWarning("Failed to Write Inventory " + user.getName());
      throw new ConfigException(e);



    }


  }


  public void readInventory (PlayerSession user) throws ConfigException{
    if (!user.isOnline()) return;


    File file;

    try {

      file = getUserFile(user);
      logInfo("Writing Inventory " + user.getName());
      Inventory inv = user.getPlayer().get().getInventory();
      inv.clear();
      FileConfiguration playerConfig = YamlConfiguration.loadConfiguration(file);
      if (playerConfig.contains("inv")){
        MemorySection ser = (MemorySection) playerConfig.get("inv");
        if (ser == null){
          logInfo("Failed to read Inventory " + user.getName());
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
        logInfo("Failed to read Inventory " + user.getName());
      }


    } catch (Exception e) {
      logInfo("Failed to read Inventory " + user.getName());
      throw new ConfigException(e);
     //

    }


  }


}
