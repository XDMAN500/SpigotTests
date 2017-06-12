package me.varmetek.munchymc.backend.file;

import me.varmetek.munchymc.backend.exceptions.ConfigException;
import me.varmetek.munchymc.mines.Mine;
import me.varmetek.munchymc.mines.MineManager;
import me.varmetek.munchymc.util.UtilFile;
import org.apache.commons.lang.Validate;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by XDMAN500 on 5/15/2017.
 */
public final class MineFileManager implements FileManager
{
  private final static String __ = File.separator;
  private final static UtilFile.CoreFile FILE = UtilFile.CoreFile.MINES ;
  private final static UtilFile.CoreFile BROKEN = UtilFile.CoreFile.MINES_BROKEN ;
  private MineManager mineManager;
  private Logger log;

  public MineFileManager (MineManager mineManager, Logger logger){
    Validate.notNull(mineManager);
    Validate.notNull(logger);
    this.log = logger;
    this.mineManager = mineManager;
  }






///////////////////////////////////////////////////////////////////////////////////
/*
     Saving
*/
///////////////////////////////////////////////////////////////////////////////////

  private void write(YamlConfiguration config, Mine mine) throws ConfigException{
    Validate.notNull(config, "Config cannot be null");
    Validate.notNull(mine, "Mine cannot be null");


    try {
      config.set(mine.getName(), mine.serialize());
    }catch(Exception ex){
      throw new ConfigException(ex);
    }

  }


  public void saveMine(Mine mine) throws ConfigException{
    Validate.notNull(mine, "Mine cannot be null");
    File file = FILE.getFile();

    try {
      UtilFile.create(file);
    }catch(IOException ex){
        throw new ConfigException(ex);

    }

    YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

    write(config,mine);

    try{
      config.save(file);
    }catch(Exception e){
      throw new ConfigException("Failed to save mine \"" + mine.getName()+"\".",e);
    }

  }
  public void saveAll(){
    File file = FILE.getFile();


    try { UtilFile.create(file);}
    catch (IOException e) {
     log.log(Level.SEVERE,"Could not create mine file",e);
      return;
    }


    YamlConfiguration config = YamlConfiguration.loadConfiguration(file);


    //Cleaning
    for(String key :config.getKeys(false)){
      config.set(key,null);
    }


    for(Mine mine: mineManager.getAllMines()){
      try{
        write(config,mine);
      }catch(ConfigException e){
        log.log(Level.WARNING,"Failed to write mine", e);
      }
    }

    try {
      config.save(file);
      log.info("Mines have been saved");
    } catch (IOException e) {
      log.log(Level.SEVERE,"Could not save mines",e);

    }



  }









////////////////////////////////////////////////////////////////////////////////////
/*
     LOADING
*/
///////////////////////////////////////////////////////////////////////////////////





  public void loadMine(String name) throws ConfigException{


    File file = FILE.getFile();

    try { UtilFile.create(file);}
    catch(IOException ex) {
     throw new ConfigException(ex);
    }

    YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
    try {
      read(config, name);
    }catch(ConfigException ex){
      file = BROKEN.getFile();

      try { UtilFile.create(file);}
      catch(IOException e ) {
        throw new ConfigException("Failed to create backup mine file",ex);
      }

      Map<String,Object> data = config.getConfigurationSection(name).getValues(false);
      config = YamlConfiguration.loadConfiguration(file);
      config.set(name,data);


      try {   config.save(file);}
      catch(IOException e ) {
        throw new ConfigException("Failed to backup mine \""+ name +"\"",ex);
      }
      throw ex;
    }
  }

  private void read(YamlConfiguration config, String name) throws ConfigException{
    Validate.notNull(config);
    Validate.notNull(name);
    Validate.isTrue(config.isConfigurationSection(name));

    try {

      Map<String,Object> data = config.getConfigurationSection(name).getValues(false);
      mineManager.deserializeMine(data, name);
    }catch(Exception e){
      throw new ConfigException("Error saving mine \""+ name+ "\"",e);
    }
  }

  public void loadAll(){
    File file = FILE.getFile();


    try { UtilFile.create(file);}
    catch (IOException e) {
      log.log(Level.SEVERE,"Could not create mine file",e);
      return;
    }


    YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

    for(String s: config.getKeys(false)){
      try{
        read(config,s);
      }catch(ConfigException e){

        log.log(Level.WARNING,"Failed to load mine", e);
      }
    }
    log.info("Mines have been loaded");


  }
}
