package me.varmetek.munchymc.backend.file;

import me.varmetek.core.util.Cleanable;
import me.varmetek.core.util.Messenger;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Random;

/**
 * Created by XDMAN500 on 1/14/2017.
 */
public final class DataManager implements Cleanable
{

  private final static String __ = File.separator;
  private final Random rand = new Random();







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


  @Override
  public void clean (){

  }
}