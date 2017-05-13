package me.varmetek.munchymc.backend.mines;


import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import me.varmetek.munchymc.Main;
import org.bukkit.World;
import org.bukkit.material.MaterialData;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by XDMAN500 on 5/10/2017.
 */
public class MineGenerator
{

  Mine mine;

  public MineGenerator(Mine mine){
    this.mine = mine;
  }
  @SuppressWarnings("deprecation")
  public void generate(){
    World world = mine.getWorld();

    if(world == null)return;
    ProtectedRegion pr =  mine.getRegion();


    int minX = pr.getMinimumPoint().getBlockX();
    int minY = pr.getMinimumPoint().getBlockY();
    int minZ = pr.getMinimumPoint().getBlockZ();
    int maxX = pr.getMaximumPoint().getBlockX();
    int maxY = pr.getMaximumPoint().getBlockY();
    int maxZ = pr.getMaximumPoint().getBlockZ();

    RandomMaterial rand = new RandomMaterial(mine.getBlockData());

    try{

      new BukkitRunnable(){
        int y = minY;
        public void run(){
          if(y>maxY){
            this.cancel();
            return;
          }

          for (int z = minZ; z <= maxZ; z++) {

            for (int x = minX; x <= maxX; x++) {


              MaterialData chosen = rand.next();
              world.getBlockAt(x,y,z).setTypeIdAndData(chosen.getItemTypeId(),chosen.getData(),false);

            }
          }
          y++;
        }
      }.runTaskTimer(Main.get(), 5L, 0L);
	 /*     for (int y = minY; y <= maxY; y++) {
	        for (int z = minZ; z <= maxZ; z++) {
	          for (int x = minX; x <= maxX; x++) {

	        	  LocalBlock chosen  = Rmat.nextBlock(Main.resetSamples);

	        	   world.getBlockAt(x, y, z).
	        	   setTypeIdAndData(chosen.getID(), chosen.getData(), false);

	          }
	        }

	      }*/
    }
    catch(NullPointerException e){
      Main.get().getLogger().warning("Mine \"" + mine.getName() + " \" could not reset properly");
      e.printStackTrace();
    }

  }

    private class RandomMaterial{
      private Map<MaterialData,Double> chances;
      private Map<MaterialData,AtomicInteger> num;
      private AtomicInteger total  = new AtomicInteger(0);
      private Random rand = new Random();
      private List<LocalBlock> list;

      public RandomMaterial ( List<LocalBlock> blocks ){
        this.list = blocks;
         chances = LocalBlock.toMap(blocks);
          num = new HashMap<>(blocks.size());
        for(MaterialData data:  chances.keySet()){
          num.put(data,new AtomicInteger(0));
        }

      }

      public MaterialData next(){
        total.incrementAndGet();
        MaterialData chosen = null;


        for(MaterialData data: chances.keySet()){
          if(num.get(data).doubleValue()/ total.doubleValue() <= chances.get(data)){
            chosen = data;
            break;
          }
        }
        if(chosen == null){
            chosen =  list.get(rand.nextInt(list.size())).getData();
        }
        num.get(chosen).incrementAndGet();

        return chosen;
      }
    }
}
