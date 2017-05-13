package me.varmetek.munchymc.backend.mines;

import com.sk89q.worldedit.BlockVector;
import me.varmetek.munchymc.Main;
import org.apache.commons.lang3.Validate;
import org.bukkit.World;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Created by XDMAN500 on 5/10/2017.
 */
public class MineManager
{
  Main main;
  private Map<String,Mine> mines = new HashMap<>();

  public  MineManager(Main plugin){
    main = plugin;
  }

  public Optional<Mine> getMine(int x, int y, int z){
    Mine output = null;
    if(mines.isEmpty()){
      return Optional.empty();
    }
    for(Mine mine: mines.values()){
      if(mine.getRegion().contains(x, y, z)){
        output = mine;
        break;
      }
    }

    return Optional.ofNullable(output);
  }

  public Optional<Mine> getMine(String name){
    return Optional.ofNullable(mines.get(name));
  }

  public Mine create(String name, BlockVector max, BlockVector min, World world){

    Validate.isTrue(!getMine(name).isPresent()," A mine with name \""+name +"\" already exists");

    Mine mine = new Mine(this,name,max,min,world);
    mines.put(name,mine);
    return mine;
  }

  public boolean hasMine(String name){
    return mines.containsKey(name);
  }

  public void remove(String name){
    mines.remove(name);
  }


}
