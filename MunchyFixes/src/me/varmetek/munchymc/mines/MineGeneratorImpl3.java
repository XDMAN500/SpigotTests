package me.varmetek.munchymc.mines;


import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import me.varmetek.munchymc.MunchyMax;
import org.bukkit.World;
import org.bukkit.material.MaterialData;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Random;

/**
 * Created by XDMAN500 on 5/10/2017.
 */
public class MineGeneratorImpl3 extends MineGenerator
{



  public MineGeneratorImpl3 (Mine mine){
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


              MaterialData chosen = rand.next(100);
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
   *  Based on rolls
   * */
    private class RandomMaterial{

      private Random rand = new Random();
      private List<LocalBlock> list;

      public RandomMaterial ( List<LocalBlock> blocks ){
        this.list = blocks;


      }

      public MaterialData next(int sample){

        LocalBlock chosen = null;
      for(int i = 0; i< sample ;i++){
        double roll = rand.nextDouble();
          for (LocalBlock data : list){
            roll = rand.nextDouble();
            if (data.getChance() >= roll){
              chosen = data;
              break;
            }
          }
        if(chosen != null){
          break;
        }
        }

        if(chosen == null){
            chosen =  list.get(rand.nextInt(list.size()));
        }


        return chosen.getData();
      }
    }
}
