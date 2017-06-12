package me.varmetek.munchymc.mines;

import com.sk89q.worldedit.BlockVector;
import me.varmetek.core.util.Cleanable;
import me.varmetek.munchymc.backend.exceptions.ConfigException;
import org.apache.commons.lang3.Validate;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.spigotmc.CaseInsensitiveMap;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Created by XDMAN500 on 5/10/2017.
 */
public class MineManager implements Cleanable
{

  private Map<String, Mine > mines =new CaseInsensitiveMap<>();
  private final PokeMineTask pokeTask = new PokeMineTask();
  //private Table<UUID,String,Mine> mines = Tables.newCustomTable(Maps.newHashMap(),()->{return new CaseInsensitiveMap<>();});

  private Plugin plugin;
  public  MineManager(Plugin pl){
    plugin = pl;

  }

  public void startTask(){
    pokeTask.runTaskTimer(plugin,40L,100L);
  }
  public Collection<Mine> getAllMines(){
    return mines.values();
  }

  public Optional<Mine> getMineAt(Location loc){
    return getMineAt(loc.getWorld(),loc.getBlockX(),loc.getBlockY(),loc.getBlockZ());
  }

  public Optional<Mine> getMineAt(World world,int x, int y, int z){
    Validate.notNull(world,"World cannot be null");
    ;

    if(mines.isEmpty()){
      return Optional.empty();
    }

    for(Mine mine: mines.values()){
      if(!mine.getWorld().equals(world))continue;

      if(mine.contains(x, y, z)){
        return Optional.of(mine);


      }
    }

    return Optional.empty();
  }



  public Optional<Mine> getMine(String name){
   // Validate.notNull(world,"World cannot be null");
    Validate.notNull(name, "Mine name cannot be null");
    if(mines.isEmpty()) return Optional.empty();




    return Optional.ofNullable(mines.get(name));
  }

  public Mine create(String name, BlockVector max, BlockVector min, World world){

    Validate.isTrue(!getMine(name).isPresent()," A mine with name \""+name +"\" already exists");

    Mine mine = new Mine(this,name,max,min,world);
    mines.put(name,mine);
    return mine;
  }


  public boolean hasMine(World world, String name){
   Validate.notNull(world, "World cannot be null");
    Validate.notNull(name, "Mine name cannot be null");
    try {
      return mines.containsKey(name);
    }catch(NullPointerException ex){
      return false;
    }
  }

  public void remove(String name){
    //Validate.notNull(world,"World cannot be null");
    Validate.notNull(name, "Mine name cannot be null");

    mines.remove(name);
  }

  public Set<String> getMineNames(){
    return mines.keySet();

  }
  private void register(Mine mine){
    Validate.notNull(mine, "Mine cannot be null");
    mines.put(mine.getName(),mine);
  }

  public Mine deserializeMine(Map<String,Object> input, String name) throws ConfigException{
    Mine mine = new Mine(this,name,input);
    register(mine);
    return mine;


  }

  @Override
  public void clean (){
    plugin = null;
    if(pokeTask.getTaskId() != -1)
      pokeTask.cancel();
    mines.values().forEach(Mine::clean);
    mines.clear();
    mines = null;
  }


  private class PokeMineTask extends BukkitRunnable{
    public void run(){
      //MunchyMax.logger().info("[MINES] running poke");
      mines.values().forEach( Mine::poke);
    }
  }
}


