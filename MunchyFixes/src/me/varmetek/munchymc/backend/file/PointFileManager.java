package me.varmetek.munchymc.backend.file;

import me.varmetek.munchymc.backend.Point;
import me.varmetek.munchymc.backend.PointManager;
import me.varmetek.munchymc.backend.exceptions.ConfigException;
import me.varmetek.munchymc.util.UtilFile;
import org.apache.commons.lang.Validate;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by XDMAN500 on 5/15/2017.
 */
public final class PointFileManager implements FileManager
{
  private final static String __ = File.separator;
  private final static UtilFile.CoreFile FILE = UtilFile.CoreFile.POINTS ;
  private final static UtilFile.CoreFile BROKEN= UtilFile.CoreFile.POINTS_BROKEN;
  private PointManager pointManager;
  private Logger log;

  public PointFileManager (PointManager pointManager, Logger logger){
    Validate.notNull(pointManager);
    Validate.notNull(logger);
    this.pointManager = pointManager;
    this.log =logger;
  }




///////////////////////////////////////////////////////////////////////////////////
/*
     LOADING
*/
///////////////////////////////////////////////////////////////////////////////////

  public Point loadPoint (String name) throws ConfigException{
    File file = FILE.getFile();


    try {UtilFile.create(file);
    } catch (IOException e) {
     throw new ConfigException("Failed to create file " + file.getName(),e);
    }

    YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
    Point p;

    try{
     p = read(config,name);
    }catch(ConfigException ex){
      file = BROKEN.getFile();

      try { UtilFile.create(file);}
      catch(IOException e ) {
        throw new ConfigException("Failed to create file " + file.getName(),ex);
      }

      Map<String,Object> data = config.getConfigurationSection(name).getValues(false);
      config = YamlConfiguration.loadConfiguration(file);
      config.set(name,data);


      try {   config.save(file);}
      catch(IOException e ) {
        throw new ConfigException("Failed to save file "+ file.getName(),ex);
      }
      throw ex;

    }
    return p;


  }

  public void loadllPoints (){
    File file = FILE.getFile();



    try {
      UtilFile.create(file);
    } catch (Exception e) {
      log.log(Level.SEVERE, "Failed to create file \"" + file.getName() + "\"");
      return;
    }


    YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

    //Loading
    for (String key : config.getKeys(false)) {
      try {
        read(config,key);
      } catch (Exception expected) {
        log.log(Level.WARNING,"Error saving point \""+ key+"\"",expected);

      }

    }

    log.info("Loaded all points");
  }

  private Point read(YamlConfiguration config, String name) throws ConfigException{
    Validate.notNull(config);
    Validate.notNull(name);
    Validate.isTrue(config.isConfigurationSection(name));
    boolean isWarp;
    Location warpLoc;
    Point p;
    try {

      ConfigurationSection sec = config.getConfigurationSection(name);
      isWarp = sec.isBoolean("warp");
      Object result = sec.get("location");
      Validate.isTrue(result instanceof Location);
      //DEBUG//Bukkit.getLogger().info("Point stuff Load: "+ sec.get("location").getClass());
      warpLoc = (Location) result;
       p = new Point.Builder(warpLoc).build();


      pointManager.setPoint(name, p);
      if (isWarp) pointManager.setWarp(name);
    }catch(Exception e){
      throw new ConfigException(e);
    }

    return p;
  }

///////////////////////////////////////////////////////////////////////////////////
/*
     Saving
*/
///////////////////////////////////////////////////////////////////////////////////



  private void write(YamlConfiguration config, String name) throws ConfigException{
    Validate.notNull(config);
    Validate.notNull(name);
    Optional<Point> point = pointManager.getPoint(name);
    Validate.isTrue(point.isPresent(), "Point \"" + name + "\" cannot be saved as it does not exist");

    try {
      config.set(name + ".location", point.get().getLocation());
      config.set(name + ".warp", pointManager.isWarp(name));
    }catch(Exception e){
      throw new ConfigException(e);
    }
  }

  public void savePoint (String name) throws ConfigException{

    File file = FILE.getFile();

    //Creating file
    try {UtilFile.create(file);

    }catch(IOException e){
      throw new ConfigException(e);
    }

    //Opening file
    YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

    //Writing
    write(config,name);


    //Saving config
    try{config.save(file);

    }catch(IOException e){
      throw new ConfigException(e);
    }


  }

  public void saveAllPoints (){

    File file = FILE.getFile();

    //Creating file
    try {UtilFile.create(file);

    }catch(IOException e){
        log.log(Level.SEVERE, "Failed to create file \""+ file.getName()+"\"");
        return;
    }

    //Opening file
   YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

    //Cleaning
    for (String key : config.getKeys(false)) {
      config.set(key, null);
    }



    //Writing
    for (String name : pointManager.getPoints().keySet()) {
      try {

        write(config,name);
      } catch (Exception expected) {
        log.log(Level.WARNING, "Error saving point \""+ name+"\"",expected );
      }


    }

    //Saving
    try {
      config.save(file);
      log.info("Saved all points");
    }catch(IOException ex){
      log.log(Level.SEVERE,"Failed to save points",ex);
    }

  }


}
