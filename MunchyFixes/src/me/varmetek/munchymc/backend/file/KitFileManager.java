package me.varmetek.munchymc.backend.file;

import me.varmetek.core.util.InventorySnapshot;
import me.varmetek.munchymc.MunchyMax;
import me.varmetek.munchymc.backend.Kit;
import me.varmetek.munchymc.backend.KitHandler;
import me.varmetek.munchymc.backend.exceptions.ConfigException;
import me.varmetek.munchymc.util.UtilFile;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.potion.PotionEffect;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
  private KitHandler kitHandler;

  public KitFileManager (KitHandler kitHandler){
    Validate.notNull(kitHandler);
   this.kitHandler = kitHandler;
  }

  public String getKitDirectory (String kit){
    return FILE.getDirectory() + __ + kit + ".yml";
  }


  public File getKitFile (String kit) throws IllegalArgumentException, IOException{

    return UtilFile.getFile(getKitDirectory(kit));
  }

  public void saveKit (String name) throws ConfigException , IOException{

    saveKit(name, getKitFile(name));
  }


  private void saveKit (String name, File f) throws ConfigException{
    Validate.isTrue(kitHandler.isKit(name), "Could not save kit \"" + name + "\" because it does not exist");

    try {

      if (!f.exists()){
        UtilFile.create(f);
      }


      Kit kit = kitHandler.getKit(name).get();

      YamlConfiguration config = YamlConfiguration.loadConfiguration(f);
      config.set("inventory", kit.getInventorySnapshot());
      //  config.set("kit",kit.serialize());

      //config.set(INV, kit.getInv());
      // config.set(EFFECTS, kit.getEffects());
      config.save(f);
    }catch(Exception e){
      Bukkit.getLogger().warning("[ERROR] "+ "Failed saving kit: \""+ name+"\"");
      throw new ConfigException("Failed saving kit: \""+ name+"\"",e);
    }


  }


  public void saveKits (){
    for (String k : kitHandler.getKits().keySet()) {
      try {
        saveKit(k);
      } catch (Exception e) {
        Bukkit.getLogger().warning("Failed to save kit " + k);
        //e.printStackTrace();
      }
    }
  }




  public Kit loadKit (String k) throws ConfigException, IOException{


    return loadKit(k, getKitFile(k));
  }

  private Kit loadKit (String name, File f) throws ConfigException{
    InventorySnapshot snapshot = null;

    try {
      Validate.isTrue(f.exists(), "Could not load kit \"" + name + "\" from non-existant file");
      YamlConfiguration config = YamlConfiguration.loadConfiguration(f);



      Object data = config.get("inventory", null);
      if (data == null || !(data instanceof InventorySnapshot)){
        throw new ConfigException("Unable to load inventory from file \"" + f.getName() + "\"");
      }

      snapshot = (InventorySnapshot) data;

    }catch(Exception e){
      throw new ConfigException(e);

    }


    Kit kit = new Kit.Builder().setInventory(snapshot).build();
    kitHandler.setKit(name, kit);
    return kit;
  }

  private Kit loadKit (File f) throws ConfigException{

    if (!f.exists() || !f.getName().endsWith(".yml")) throw new IllegalArgumentException("Invalid file for kit");

    String name = f.getName().substring(0, f.getName().lastIndexOf('.'));


    return loadKit(name, f);
  }

  public void loadKits (){

    File f = new File(MunchyMax.getInstance().getDataFolder(), FILE.getDirectory());

    if(!f.exists()){
      Bukkit.getLogger().warning("Folder for kits does not exist. Attempting to create a blank.");
      try {
        UtilFile.create(f);
        Bukkit.getLogger().info("Kit Folder created");
      }catch(IOException e){
        Bukkit.getLogger().info("An error occured while creating the kitfolder");
        return;
      }

    }

    for (File file : f.listFiles()) {
      try {
        loadKit(file);
      } catch (ConfigException e) {
        if (file.getName().endsWith(".yml")){
          Bukkit.getLogger().info("Could not load kit from file " + file.getName());
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
    kitHandler.clear();
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
