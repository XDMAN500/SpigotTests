package me.varmetek.munchymc.backend.file;

import me.varmetek.munchymc.MunchyMax;
import me.varmetek.munchymc.backend.Point;
import me.varmetek.munchymc.backend.PointManager;
import me.varmetek.munchymc.backend.exceptions.ConfigException;
import me.varmetek.munchymc.util.UtilFile;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

/**
 * Created by XDMAN500 on 5/15/2017.
 */
public final class PointFileManager implements FileManager
{
  private final static String __ = File.separator;
  private final static UtilFile.CoreFile FILE = UtilFile.CoreFile.Points ;
  private PointManager pointManager;

  public PointFileManager (PointManager pointManager){
    Validate.notNull(pointManager);
    this.pointManager = pointManager;
  }




  public Point loadPoint (String name) throws ConfigException{
    boolean isWarp = false;
    Location warpLoc = null;
    File file = FILE.getFile();

    try{
      if (!file.exists()){
        UtilFile.create(file);
      }

      FileConfiguration config = YamlConfiguration.loadConfiguration(file);
      Validate.isTrue(config.isConfigurationSection(name));


      ConfigurationSection sec = config.getConfigurationSection(name);
      isWarp = sec.isBoolean("warp");
      Object result = sec.get("location");
      Validate.isTrue(result instanceof Location);
      //DEBUG//Bukkit.getLogger().info("Point stuff Load: "+ sec.get("location").getClass());
      warpLoc = (Location)result;

    }catch (Exception e){
      throw new ConfigException(e);
    }

    Point p = new Point.Builder(warpLoc).build();


    pointManager.setPoint(name, p);
    if (isWarp) pointManager.setWarp(name);
    return p;


  }

  public boolean savePoint (String name) throws IOException{
    PointManager manager = MunchyMax.getPointManager();
    Validate.isTrue(manager.pointExist(name), "Point \"" + name + "\" cannot be saved as it does not exist");


    File file = FILE.getFile();
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
    PointManager manager = MunchyMax.getPointManager();
    File file = FILE.getFile();
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
      } catch (Exception expected) {
        Bukkit.getLogger().warning("Error saving point \""+ name+"\"");
        expected.printStackTrace();

      }


    }

  }

  public void loadllPoints () throws IOException{
    File file = FILE.getFile();
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
      } catch (Exception expected) {
        Bukkit.getLogger().warning("Error saving point \""+ key+"\"");
        expected.printStackTrace();

      }

    }


  }
}
