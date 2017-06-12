package me.varmetek.munchymc.backend.file;

import me.varmetek.munchymc.backend.user.PlayerData;
import me.varmetek.munchymc.backend.user.PlayerHandler;
import me.varmetek.munchymc.backend.user.PlayerSession;
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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by XDMAN500 on 5/15/2017.
 */
public final class PlayerFileManager implements FileManager
{
  private final static String __ = File.separator;
  private final static UtilFile.CoreFile FILE = UtilFile.CoreFile.USER;
  private PlayerHandler playerHandler;
  private Logger log;

  public PlayerFileManager (PlayerHandler playerHandler, Logger logger){
    this.playerHandler = playerHandler;
    this.log = logger;
  }



  private String getUserFolderName (PlayerSession user){
    return user.getUUID().toString();
  }

  private String getUserDataFileName (PlayerSession user){
    return getUserFolderName(user) + __ + "data.yml";
  }


  private File getUserFile (PlayerSession user) throws  IOException{
    return getUserFile(user, "data.yml");
  }

  private File createUserFile (PlayerSession user) throws IOException{
    return createUserFile(user, "data.yml");
  }

  private File createUserFile (PlayerSession user, String extra) throws IOException{
    return UtilFile.create(getUserFile(user, extra));
  }


  private File getUserFile (PlayerSession user, String extra) throws  IOException{
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



////////////////////////////////////////////////////////////////////////////////////
/*
     SAVING
*/
///////////////////////////////////////////////////////////////////////////////////


  public void saveUser (PlayerSession user) throws ConfigException{
    try {


      File file = createUserFile(user, "user.yml");
      FileConfiguration config = YamlConfiguration.loadConfiguration(file);
      config.set("data", user.getPlayerData().serialize());
      config.save(file);
      log.info("Saving user (" + user.getName() + ")");
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
        log.log(Level.WARNING,"Could not save player " + user.getName() + " :",e);
      }
    }
    log.info("Saved all players");

  }


  public void saveAllOnline (){
    for (Player p : Bukkit.getOnlinePlayers()) {
      try {
        savePlayer(p);
      } catch (ConfigException e) {
        log.log(Level.SEVERE,"Could not save player " +p.getName() + " :",e);
      }
    }
    log.info("Saved all online players");
  }




////////////////////////////////////////////////////////////////////////////////////
/*
     LOADING
*/
///////////////////////////////////////////////////////////////////////////////////




  public void loadUser (PlayerSession user) throws ConfigException{


    try {
      PlayerData data = null;
      File file = getUserFile(user, "user.yml");
      FileConfiguration config = YamlConfiguration.loadConfiguration(file);


      data =  new PlayerData(config.getConfigurationSection("data").getValues(false));
      log.info("Loading user (" + user.getName() + ")");
      user.setPlayerData(data);
    }catch(Exception e){
      throw new ConfigException(e);
    }



  }

  public void loadPlayer (Player user) throws ConfigException{
    loadUser(playerHandler.getSession(user));
  }


  public void loadAllOnline (){
    for (Player p : Bukkit.getOnlinePlayers()) {
      try {
        loadUser(playerHandler.getSession(p));
      } catch (ConfigException e) {
        log.log(Level.SEVERE,"Could not load player " + p.getName() + " :",e);
      }

    }

    log.info("Loaded all players");
  }


  public void reloadUsers (){
    for (Player p : Bukkit.getOnlinePlayers()) {
      try {
        savePlayer(p);
        loadPlayer(p);
      }catch(ConfigException e){
        log.log(Level.SEVERE,"Could not reload player " + p.getName() + " :",e);
      }
    }
    log.info("reLoaded all players");
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
      log.warning("Failed to Write Inventory " + user.getName());
      throw new ConfigException(e);



    }


  }


  public void readInventory (PlayerSession user) throws ConfigException{
    if (!user.isOnline()) return;


    File file;

    try {

      file = getUserFile(user);
      log.info("Writing Inventory " + user.getName());
      Inventory inv = user.getPlayer().get().getInventory();
      inv.clear();
      FileConfiguration playerConfig = YamlConfiguration.loadConfiguration(file);
      if (playerConfig.contains("inv")){
        MemorySection ser = (MemorySection) playerConfig.get("inv");
        if (ser == null){
          log.info("Failed to read Inventory " + user.getName());
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
        log.info("Failed to read Inventory " + user.getName());
      }


    } catch (Exception e) {
      log.info("Failed to read Inventory " + user.getName());
      throw new ConfigException(e);
     //

    }


  }


}
