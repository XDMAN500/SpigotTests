package me.varmetek.munchymc.mines;

import com.sk89q.worldedit.BlockVector;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.material.MaterialData;

import java.util.*;

/**
 * Created by XDMAN500 on 5/15/2017.
 */
public class MineData
{
  public static final int DEFAULT_DELAY = 600;
  private World world = null;
  private int resetDelay = DEFAULT_DELAY;
  private BlockVector maxPoint = null, minPoint = null;
  private Location saveSpot = null;
  private Map<MaterialData,Integer> blocks = new HashMap<>();


  public Map<MaterialData,Integer> getRawBlockData(){
    return blocks;
  }
  public boolean hasEmptyBlockData(){
    return blocks.isEmpty();
  }

  public void setSafeSpot(Location loc){
    this.saveSpot = loc;
  }

  public Optional<Location> getSafeSpot(){
    return Optional.of(saveSpot);
  }

  public World getWorld(){
    return world;
  }

  public long getResetDelay(){
    return resetDelay;
  }

  public void setResetDelay(int time){

    resetDelay = time;
  }

  public int totalWeight(){

    int max = 0;
    for(Integer num :blocks.values()){
      if(num == null)continue;
      max += num.intValue();
    }

    return max;
  }

  public BlockVector getMaxPoint(){
    return maxPoint;
  }

  public BlockVector getMinPoint(){
    return minPoint;
  }

  public List<BlockData> getBlockData(){

    List<BlockData> output = new ArrayList<>(blocks.size());
    double totalWeight = totalWeight();


    blocks.entrySet().forEach( (entry) ->{

      output.add( new BlockData(entry.getKey(),entry.getValue(), entry.getValue().doubleValue()/totalWeight));
    });


    return output;
  }
}
