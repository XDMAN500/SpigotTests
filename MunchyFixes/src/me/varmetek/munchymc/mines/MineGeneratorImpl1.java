package me.varmetek.munchymc.mines;


import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import me.varmetek.munchymc.MunchyMax;
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
public class MineGeneratorImpl1 extends MineGenerator
{



  public MineGeneratorImpl1 (Mine mine){
    super(mine);
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
      }.runTaskTimer(MunchyMax.getInstance(), 5L, 0L);
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
      MunchyMax.getInstance().getLogger().warning("Mine \"" + mine.getName() + " \" could not reset properly");
      e.printStackTrace();
    }

  }
  /**
   *
   *  Based on progressional composition
   * */
    private class RandomMaterial{

      private Map<LocalBlock,AtomicInteger> amount;
      private AtomicInteger total  = new AtomicInteger(0);
      private Random rand = new Random();
      private List<LocalBlock> list;

      public RandomMaterial ( List<LocalBlock> blocks ){
        this.list = blocks;

          amount = new HashMap<>(blocks.size());
        for(LocalBlock data:  blocks){
          amount.put(data,new AtomicInteger(0));
        }

      }

      public MaterialData next(){
        total.incrementAndGet();
        LocalBlock chosen = null;


        for(LocalBlock data: list){
          if(amount.get(data).doubleValue()/ total.doubleValue() <= data.getChance()){
            chosen = data;
            break;
          }
        }

        if(chosen == null){
            chosen =  list.get(rand.nextInt(list.size()));
        }
        amount.get(chosen).incrementAndGet();

        return chosen.getData();
      }
    }
}
