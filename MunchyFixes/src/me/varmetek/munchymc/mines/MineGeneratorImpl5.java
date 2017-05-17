package me.varmetek.munchymc.mines;


import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import me.varmetek.munchymc.MunchyMax;
import org.bukkit.World;
import org.bukkit.material.MaterialData;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by XDMAN500 on 5/10/2017.
 */
public class MineGeneratorImpl5 extends MineGenerator
{



  public MineGeneratorImpl5 (Mine mine){
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

    RandomMaterial rand = new RandomMaterial(mine.getBlockData(), mine.getRegion().volume());

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


              MaterialData chosen = rand.next(10);
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
   *
   * Base on valume with a randomized list;
   * **/
    private class RandomMaterial{

      private Map<LocalBlock,AtomicInteger> placed = new HashMap<>();
      private int volume = 0;
      private Random rand = new Random();
      private List<LocalBlock> list;
      private Comparator<LocalBlock> compare = (LocalBlock b1, LocalBlock b2) -> { return rand.nextInt(3)-1; };
      public RandomMaterial ( List<LocalBlock> blocks , int volume){
        this.list = blocks;
        this.volume = volume;
        for(LocalBlock block: blocks){
          placed.put(block, new AtomicInteger(0));
        }


      }

      public MaterialData next(int sample){

        LocalBlock chosen = null;
      for(int i = 0; i< sample ;i++){

          for (LocalBlock data : list){

            double prop = placed.get(data).doubleValue() / ((double)volume);

            if( data.getChance() > prop )continue;
            chosen = data;
            break;

          }
          if(chosen!= null){
            break;
          }
          list.sort(compare);
        }

        if(chosen == null){
            chosen =  list.get(rand.nextInt(list.size()));
        }

        placed.get(chosen).incrementAndGet();
        return chosen.getData();
      }
    }
}
