package me.varmetek.munchymc.mines;


import me.varmetek.munchymc.MunchyMax;
import org.bukkit.Material;
import org.bukkit.material.MaterialData;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Created by XDMAN500 on 5/10/2017.
 */
public class MineGeneratorImpl2 extends MineGenerator
{

  private long blockCount = 0;


  public MineGeneratorImpl2 (Mine mine){
    super(mine);


  }


  @SuppressWarnings ("deprecation")
  public void generate (){

    new ResetTask(500).runTaskTimer(MunchyMax.getInstance(),1L,5L);


  }

  public long getBlockCount(){

    return blockCount;
  }

  public void calculateBlockCount(){
    new TallyTask(10000).runTaskTimer(MunchyMax.getInstance(),1L,5L);
  }

  @Override
  public void empty (){
    new EmptyTask(500).runTaskTimer(MunchyMax.getInstance(),1L,5L);
  }


  protected class ResetTask extends BukkitRunnable
  {


    int stepX = minX;
    int stepY = minY;
    int stepZ = minZ;

    final int threshhold;


    final  RandomBlock rand = new RandomBlock(mine.getBlockData());

    public ResetTask (int threshhold){

      this.threshhold = threshhold;

    }
    @Override
    public void cancel(){
      super.cancel();
      working = false;
    }
    @Override
    public void run (){
      working = true;
      int i = 0;
      for (; stepY <= maxY; stepY++) {
        for (; stepZ <= maxZ; stepZ++) {
          for (; stepX <= maxX; stepX++) {
            try {
              MaterialData chosen = rand.next(10);
              world.getBlockAt(stepX, stepY, stepZ).setTypeIdAndData(chosen.getItemTypeId(), chosen.getData(), false);
              i++;
            // Bukkit.broadcastMessage("Mine gen: "+ i + " ("+stepX +","+ stepY +","+stepZ+") /" +" ("+maxX +","+ maxY +","+maxZ+")"  );

              if (i >= threshhold){

                return;
              }

            }
            catch(Exception e){
              this.cancel();
              throw new MineResetException("Error occured when reseting mine",e);

            }
          }
           stepX = minX;
        // Bukkit.broadcastMessage("Mine gen: Finished X");
        }
        stepZ = minZ;
     //   Bukkit.broadcastMessage("Mine gen: Finished Z");
      }
     // Bukkit.broadcastMessage("Mine gen: Finished Y");
      this.cancel();
      blockCount = mine.volume();



    }


  }
  protected class EmptyTask extends BukkitRunnable
  {


    int stepX = minX;
    int stepY = minY;
    int stepZ = minZ;

    final int threshhold;




    public EmptyTask (int threshhold){

      this.threshhold = threshhold;

    }

    @Override
    public void cancel(){
      super.cancel();
      working = false;
    }

    @Override
    public void run (){
      working = true;
      int i = 0;
      for (; stepY <= maxY; stepY++) {
        for (; stepZ <= maxZ; stepZ++) {
          for (; stepX <= maxX; stepX++) {
            try {

              world.getBlockAt(stepX, stepY, stepZ).setType(Material.AIR);
              i++;
              // Bukkit.broadcastMessage("Mine gen: "+ i + " ("+stepX +","+ stepY +","+stepZ+") /" +" ("+maxX +","+ maxY +","+maxZ+")"  );

              if (i >= threshhold){

                return;
              }

            }
            catch(Exception e){
              this.cancel();
              throw new MineResetException("Error occured when emptying mine",e);

            }
          }
          stepX = minX;
          // Bukkit.broadcastMessage("Mine gen: Finished X");
        }
        stepZ = minZ;
        //   Bukkit.broadcastMessage("Mine gen: Finished Z");
      }
      // Bukkit.broadcastMessage("Mine gen: Finished Y");
      this.cancel();
      blockCount = 0;




    }


  }

  protected class TallyTask extends BukkitRunnable
  {


    int stepX = minX;
    int stepY = minY;
    int stepZ = minZ;

    final int threshhold;
    long count = 0;



    public TallyTask (int threshhold){

      this.threshhold = threshhold;

    }


    @Override
    public void run (){

      int i = 0;
      for (; stepY <= maxY; stepY++) {
        for (; stepZ <= maxZ; stepZ++) {
          for (; stepX <= maxX; stepX++) {
            try {

              if (world.getBlockAt(stepX, stepY, stepZ).getType() != Material.AIR){
                count++;
              }
              i++;
              // Bukkit.broadcastMessage("Mine gen: "+ i + " ("+stepX +","+ stepY +","+stepZ+") /" +" ("+maxX +","+ maxY +","+maxZ+")"  );

              if (i >= threshhold){

                return;
              }

            }
            catch(Exception e){
              this.cancel();
              throw new MineResetException("Error occured when tallying mine",e);

            }
          }
          stepX = minX;
          // Bukkit.broadcastMessage("Mine gen: Finished X");
        }
        stepZ = minZ;
        //   Bukkit.broadcastMessage("Mine gen: Finished Z");
      }
      // Bukkit.broadcastMessage("Mine gen: Finished Y");

      this.cancel();
      blockCount = count;


    }


  }

}
