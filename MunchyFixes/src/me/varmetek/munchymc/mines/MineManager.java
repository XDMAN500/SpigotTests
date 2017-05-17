package me.varmetek.munchymc.mines;

import com.sk89q.worldedit.BlockVector;
import me.varmetek.munchymc.backend.exceptions.DeserializeException;
import org.apache.commons.lang3.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.material.MaterialData;
import org.spigotmc.CaseInsensitiveMap;

import java.util.*;

/**
 * Created by XDMAN500 on 5/10/2017.
 */
public class MineManager
{

  private Map<String,Mine> mines = new CaseInsensitiveMap<>();

  public  MineManager(){

  }
  public Optional<Mine> getMineAt(Location loc){
    return getMineAt(loc.getWorld(),loc.getBlockX(),loc.getBlockY(),loc.getBlockZ());
  }

  public Optional<Mine> getMineAt(World world,int x, int y, int z){
    Optional<Mine> output = Optional.empty();

    if(mines.isEmpty()){
      return output;
    }
    for(Mine mine: mines.values()){
      if(!mine.getWorld().equals(world))continue;

      if(mine.getRegion().contains(x, y, z)){
        output = Optional.of(mine);
        break;

      }
    }

    return output;
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

  public Set<String> getMineNames(){
    return mines.keySet();
  }

  public void deserializeMine(Map<String,Object> input, String name) throws  DeserializeException {

    String worldS = null;
    String block_max = null;
    String block_min = null;
    List<String>  blockList = null;

    String errorMsg = "Error extracting raw data for Mine";
    try{

      worldS = (String)input.get("world");
      block_max = (String)input.get("blockMax");
      block_min = (String)input.get("blockMin");
      blockList = (List<String>)input.get("blocks");
      Validate.notNull(name);
      Validate.notNull(worldS);
      Validate.notNull(block_max);
      Validate.notNull(block_min);
      Validate.notNull(blockList);

    }catch(Exception e){
      e.printStackTrace();
      throw new IllegalArgumentException(errorMsg+": "+ e.getMessage());

    }

    World world = null;
    BlockVector max = null, min = null;
    Map<MaterialData,Integer> blocks = new HashMap<>();
    try{
      world = Bukkit.getWorld(name);
      String[] Vdata = block_max.split("-");
      max = new BlockVector(Integer.parseInt(Vdata[0]),Integer.parseInt(Vdata[1]),Integer.parseInt(Vdata[2]) );
      Vdata = block_min.split("-");
      min = new BlockVector(Integer.parseInt(Vdata[0]),Integer.parseInt(Vdata[1]),Integer.parseInt(Vdata[2]) );
      blockList.forEach( (in)->{
        String[]  data = in.split(":");
        blocks.put( new MaterialData( Integer.parseInt(data[0]),Byte.parseByte(data[1])), Integer.parseInt(data[2]));
      });
    }catch(Exception e){
      throw new DeserializeException("Error extracting raw data for Mine" ,e);


    }
    Mine mine = create(name, max,min,world);
    mine.getRawBlockData().putAll(blocks);

  }
}
