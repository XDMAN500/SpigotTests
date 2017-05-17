package me.varmetek.munchymc.backend.file;

import me.varmetek.munchymc.MunchyMax;
import me.varmetek.munchymc.backend.exceptions.ConfigException;
import me.varmetek.munchymc.mines.Mine;
import me.varmetek.munchymc.mines.MineManager;
import me.varmetek.munchymc.util.UtilFile;
import org.apache.commons.lang.Validate;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;

/**
 * Created by XDMAN500 on 5/15/2017.
 */
public final class MineFileManager implements FileManager
{
  private final static String __ = File.separator;
  private final static UtilFile.CoreFile FILE = UtilFile.CoreFile.MINES ;
  private MineManager mineManager;

  public MineFileManager (MineManager mineManager){
    Validate.notNull(mineManager);
    this.mineManager = mineManager;
  }







  public void saveAll(){

    for(String s: MunchyMax.getMineManager().getMineNames()){
      try{
        saveMine(s);
      }catch(Exception e){
        continue;
      }
    }

  }

  public void saveMine(String name) throws IOException{
    Optional<Mine> mineOp =  MunchyMax.getMineManager().getMine(name);
    Validate.isTrue(mineOp.isPresent());
    File file = FILE.getFile();
    if(!file.exists()){
      UtilFile.create(file);
    }
    YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
    config.set(name,mineOp.get().serialize() );
    config.save(file);

  }

  public void loadMine(String name) throws ConfigException{


    File file = FILE.getFile();
    try {
      if (!file.exists()){
        UtilFile.create(file);
      }
      YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
      Validate.isTrue(config.isConfigurationSection(name));
      Map<String,Object> data = config.getConfigurationSection("name").getValues(false);
      mineManager.deserializeMine(data,name);
    }catch(Exception e){
      throw new ConfigException(e);
    }




  }

  public void loadAll() throws IOException{
    File file = FILE.getFile();
    if(!file.exists()){
      UtilFile.create(file);
    }
    YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

    for(String s: config.getKeys(false)){
      try{
        loadMine(s);
      }catch(Exception e){
        continue;
      }
    }

  }
}
