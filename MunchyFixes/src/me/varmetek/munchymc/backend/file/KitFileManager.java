package me.varmetek.munchymc.backend.file;

import me.varmetek.core.util.InventorySnapshot;
import me.varmetek.munchymc.MunchyMax;
import me.varmetek.munchymc.backend.Kit;
import me.varmetek.munchymc.backend.KitHandler;
import me.varmetek.munchymc.backend.exceptions.ConfigException;
import me.varmetek.munchymc.util.UtilFile;
import org.apache.commons.lang.Validate;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.potion.PotionEffect;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by XDMAN500 on 5/15/2017.
 */
public final class KitFileManager implements FileManager
{

  private static final String
    INV = "inv",
    EFFECTS = "effects";

  private final static String __ = File.separator;
  private final static UtilFile.CoreFile FILE = UtilFile.CoreFile.KITS ;
  private final static FilenameFilter filter  = (dir,name)->{ return name.endsWith(".yml");};

  private KitHandler kitHandler;
  private Logger log;
  public KitFileManager (KitHandler kitHandler, Logger logger){
    Validate.notNull(kitHandler);
    Validate.notNull(logger);
    this.log = logger;
   this.kitHandler = kitHandler;
  }

  public String getKitDirectory (String kit){
    return FILE.getDirectory() + __ + kit + ".yml";
  }


  public File getKitFile (String kit) throws IOException{

    return UtilFile.getFile(getKitDirectory(kit));
  }



  public void removeKit (String k) throws IOException{
    File f = getKitFile(k);
    if (f.exists()){
      f.delete();
    }
  }

  public void reloadKits () throws IOException{
    kitHandler.clear();
    loadKits();

  }

  private static List<PotionEffect> convertEffects (List<Map<?,?>> input){
    List<PotionEffect> output = new ArrayList<>();

    for (Map<?,?> effect : input) {
      if (effect == null) continue;
      output.add(new PotionEffect((Map<String,Object>) effect));
    }
    return output;

  }





///////////////////////////////////////////////////////////////////////////////////////
/*
     SAVING
*/
///////////////////////////////////////////////////////////////////////////////////


  private void  write(YamlConfiguration config, String name) throws ConfigException{
    Validate.notNull(config);
    Validate.notNull(name);

    Optional<Kit> kit = kitHandler.getKit(name);
    Validate.isTrue(kit.isPresent());

    try {
      config.set("inventory", kit.get().getInventorySnapshot());
    }catch(Exception ex){
      throw new ConfigException(ex);
    }



  }


  public void saveKit (String name) throws ConfigException{

    try {
      saveKit(name, getKitFile(name));
    }catch(IOException ex){
      throw new ConfigException( ex);
    }
  }


  private void saveKit (String name, File f) throws ConfigException{
   try{UtilFile.create(f);
   }catch(IOException ex){
     throw new ConfigException(ex);
   }

   YamlConfiguration config = YamlConfiguration.loadConfiguration(f);
   write(config,name);

    try{  config.save(f);
    }catch(IOException ex){
      throw new ConfigException("Failed saving kit: \""+ name+"\"",ex);
    }



  }


  public void saveKits (){
    for (String k : kitHandler.getKits().keySet()) {
      try {
        saveKit(k);
      } catch (Exception e) {
        log.log(Level.WARNING,"Failed to save kit " + k,e);
      }
    }
    log.info("Kits have been saved");
  }





///////////////////////////////////////////////////////////////////////////////////
/*
     LOADING
*/
///////////////////////////////////////////////////////////////////////////////////

  private Kit read(YamlConfiguration config, String name) throws ConfigException{
    Validate.notNull(config);
    Validate.notNull(name);
    Kit kit;
    InventorySnapshot snapshot;

    Object data = config.get("inventory", null);
    if (data == null || !(data instanceof InventorySnapshot)){
      //"Unable to load inventory from file \"" + f.getName() + "\""
      throw new ConfigException();
    }

    try {
      snapshot = (InventorySnapshot) data;
      kit = new Kit.Builder().setInventory(snapshot).build();
    } catch(Exception ex){
      throw new ConfigException();
    }

    return kit;
  }


  public Kit loadKit (String k) throws ConfigException, IOException{

    try{ return loadKit(k, getKitFile(k));}
    catch(IOException ex){
      throw new ConfigException(ex);
    }
  }

  private Kit loadKit (String name, File f) throws ConfigException{


    try{UtilFile.create(f);
    }catch(IOException ex){
      throw new ConfigException( "Could not load kit \"" + name + "\" from non-existant file",ex);
    }

    Kit kit;
    try {
      YamlConfiguration config = YamlConfiguration.loadConfiguration(f);
      kit = read(config, name);
      kitHandler.setKit(name, kit);
    }catch(ConfigException ex){
      f.renameTo( new File(f.getParentFile(),f.getName()+".broken"));
      throw ex;
    }

    return kit;

  }

  private Kit loadKit (File f) throws ConfigException{

    if (!f.exists() || !f.getName().endsWith(".yml")) throw new IllegalArgumentException("Invalid file for kit");

    String name = f.getName().substring(0, f.getName().lastIndexOf('.'));


    return loadKit(name, f);
  }

  public void loadKits (){

    File f = new File(MunchyMax.getInstance().getDataFolder(), FILE.getDirectory());

    try{UtilFile.create(f);}
    catch(IOException e){
      log.log(Level.SEVERE,"Failed to load kit folder",e);
      return;

    }




    for (File file : f.listFiles(filter)) {

      try {
        loadKit(file);
      } catch (Exception e) {
        log.log(Level.WARNING, "Failed to load file " + file.getName(),e);
         // log.info("Could not load kit from file " + file.getName());
        }

    }
    log.info("Kits have been loaded");

  }



}
