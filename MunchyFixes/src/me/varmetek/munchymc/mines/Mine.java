package me.varmetek.munchymc.mines;

import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag.State;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import me.varmetek.core.util.Messenger;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.material.MaterialData;

import javax.annotation.Nullable;
import java.util.*;

/**
 * Created by XDMAN500 on 5/10/2017.
 */
public class Mine
{
  public static final int DEFAULT_DELAY = 600;
  private MineManager manager;
  private ProtectedRegion pr;
  private String name;
  private World world;
  private int nextReset = 0;
  private int resetDelay = DEFAULT_DELAY;
  private int generator = 2;
  private MineGenerator[] genOptions = new MineGenerator[]{
    new MineGeneratorImpl0(this),
    new MineGeneratorImpl1(this),
    new MineGeneratorImpl2(this),
    new MineGeneratorImpl3(this),
    new MineGeneratorImpl4(this),
    new MineGeneratorImpl5(this),
    new MineGeneratorImpl6(this)
  };

  private Location saveSpot;

  public  Map<MaterialData,Integer> blocks = new HashMap<>();




   Mine(MineManager man, String name, BlockVector max, BlockVector min, World world){
    Validate.notNull(name);
    Validate.notEmpty(name);
    Validate.isTrue(StringUtils.isNotBlank(name));
     Validate.notNull(man);
    Validate.notNull(max);
    Validate.notNull(min);
    Validate.notNull(world);
     Validate.isTrue(StringUtils.isNotBlank(name));

    manager = man;

    setRegion(max,min);
    this.name = name;
    this.world = world;



    pr.setFlag(DefaultFlag.BLOCK_BREAK, State.ALLOW);
    pr.setFlag(DefaultFlag.BLOCK_PLACE, State.ALLOW);
    pr.setFlag(DefaultFlag.BUILD, State.ALLOW);
    pr.setFlag(DefaultFlag.EXP_DROPS,State.DENY);
    pr.setFlag(DefaultFlag.PASSTHROUGH,State.ALLOW);
    pr.setFlag(DefaultFlag.FALL_DAMAGE,State.DENY);
    pr.setFlag(DefaultFlag.MOB_SPAWNING,State.DENY);


  }
  public MineGenerator getGenerator(){
     return  genOptions[generator];
  }
  public Map<MaterialData,Integer> getRawBlockData(){
     return blocks;
  }
  public boolean hasEmptyData(){
     return blocks.isEmpty();
  }

  public void setSafeSpot(Location loc){
    this.saveSpot = loc;
  }

  public Optional<Location> getSafeSpot(){
    return Optional.of(saveSpot);
  }
  public ProtectedRegion getRegion(){
    return pr;
  }

  public World getWorld(){
    return world;
  }

  public long getResetDelay(){
    return resetDelay;
  }

  public void setResetDelay(int time){

    resetDelay = time;
    nextReset = 0;
  }

  public String getName(){
    return name;
  }

  public void teleportPlayers(){
    if(saveSpot == null)return;
    List<Player> players = world.getPlayers();



    for(Player player : players){
      Location loc = player.getLocation();
      if(pr.contains(loc.getBlockX(),loc.getBlockY(),loc.getBlockZ())){
        Messenger.send(player, "&7Mine &c"+name+" &7has reset.");
        player.teleport(saveSpot);

      }

    }
  }

  public void setGenOption(int option){
    generator = option% genOptions.length;
  }

  public int getGenOption(){
    return generator;
  }

  public void resetMine(){
    nextReset = 0;
    teleportPlayers();

   if(pr == null) throw new IllegalStateException("Mine \""+ name +"\" must not have a null region");
   if(blocks.isEmpty())return;//Skip reset if blocks are empty;
  genOptions[generator].generate();




  }


  public int totalWeight(){

    int max = 0;
    for(Integer num :blocks.values()){
      if(num == null)continue;
     max += num.intValue();
    }

    return max;
  }

  public void copyBlocks(Mine mine){

    blocks.clear();
    blocks.putAll(mine.blocks);

  }
  public void copy(Mine mine){
    manager = mine.manager;
    pr.getFlags().clear();
    for (Map.Entry<Flag<?>,Object> f : mine.pr.getFlags().entrySet()) {
      pr.getFlags().put(f.getKey(),f.getValue());
    }
    blocks.clear();
    blocks.putAll(mine.blocks);
    generator = mine.generator;
    resetDelay = mine.resetDelay;
    nextReset = 0;
  }

  public void setRegion(BlockVector max, BlockVector min){

    pr = new ProtectedCuboidRegion("MineMc_"+name,
                               max, min);

  }

  public List<LocalBlock> getBlockData(){

    List<LocalBlock> output = new ArrayList<>(blocks.size());



    blocks.entrySet().forEach( (entry) ->{

      output.add( new LocalBlock(entry.getKey(),entry.getValue(), entry.getValue().doubleValue()/totalWeight()));
    });
    return output;
  }
  public void timeSet(){
    nextReset++;
  }

  public <T extends Flag<V>, V> void setFlag(T flag, @Nullable V val){
    pr.setFlag(flag,val);
  }

  @Nullable
  public <T extends Flag<V>, V> V getFlag(T flag) {
    return pr.getFlag(flag);
  }

  public Map<String,Object> serialize(){
    Map<String,Object> output = new HashMap<>();
    output.put("world", world.getName());
    BlockVector max = pr.getMaximumPoint();
    output.put("blockMax", max.getBlockX() + "-" + max.getBlockY() + "-"+ max.getBlockZ());
    BlockVector min = pr.getMinimumPoint();
    output.put("blockMin", min.getBlockX() + "-" + min.getBlockY() + "-"+ min.getBlockZ());
    List<String> serial = new ArrayList<>();
    blocks.forEach( (data, id)->{serial.add( data.getItemTypeId() + "-" + data.getData()+ "-"+ id );});
    output.put("blocks",serial);
    return output;
  }

}
