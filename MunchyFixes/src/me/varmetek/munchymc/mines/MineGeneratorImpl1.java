package me.varmetek.munchymc.mines;


import me.varmetek.munchymc.MunchyMax;
import org.bukkit.Material;
import org.bukkit.material.MaterialData;
import org.bukkit.scheduler.BukkitRunnable;

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
    new ResetTask().runTaskTimer(MunchyMax.getInstance(),1,5L);

  }

  @Override
  public void empty (){
    new EmptyTask().runTaskTimer(MunchyMax.getInstance(),1,5L);
  }


  protected class ResetTask extends BukkitRunnable
  {



    int y = minY;
    RandomBlock rand = new RandomBlock(mine.getBlockData());
    @Override
    public void run (){

      if (y > maxY) this.cancel();

      for (int x = minX; x < maxX; x++) {
        for (int z = minZ; z < maxZ; z++) {
          try {
            MaterialData chosen = rand.next(10);
            world.getBlockAt(x, y, z).setTypeIdAndData(chosen.getItemTypeId(), chosen.getData(), false);
          }catch(Exception e){
            this.cancel();
            throw new MineResetException("Error occured when reseting mine",e);

          }

        }

      }
      y++;
    }
  }

  protected class EmptyTask extends BukkitRunnable
  {



    int y = minY;

    @Override
    public void run (){

      if (y > maxY) this.cancel();

      for (int x = minX; x < maxX; x++) {
        for (int z = minZ; z < maxZ; z++) {
          try {

            world.getBlockAt(x, y, z).setType(Material.AIR);
          }catch(Exception e){
            this.cancel();
            throw new MineResetException("Error occured when emptying mine",e);

          }

        }

      }
      y++;
    }
  }





}
