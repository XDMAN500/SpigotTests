package me.varmetek.munchymc.mines;

import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.Iterator;

/**
 * Created by XDMAN500 on 5/13/2017.
 */
public abstract class  MineGenerator
{
  protected final Mine mine;
  protected final World world;


  protected final int minX;
  protected final int minY;
  protected final int minZ;

  protected final int maxX;
  protected final int maxY;
  protected final int maxZ;

  boolean working = false;
  public MineGenerator (Mine mine){
    this.mine = mine;
    world = mine.getWorld();
    if(world == null) throw new IllegalStateException(" Mine needs a world");


    minX = mine.getMinPoint().getX();
    minY = mine.getMinPoint().getY();
    minZ = mine.getMinPoint().getZ();

    maxX = mine.getMaxPoint().getX();
    maxY = mine.getMaxPoint().getY();
    maxZ = mine.getMaxPoint().getZ();
  }

  public abstract  void generate();
  public abstract void empty();

  public boolean isWorking(){
    return working;
  }

  public class BlockIterator implements Iterator<Block>
  {
    private int stepX = minX;
    private int stepY = minY;
    private int stepZ = minZ;

    private boolean done = false;

    private Block getBlock(){
      return world.getBlockAt(stepX, stepY, stepZ);
    }

    private void findNext(){
     if(done) return;

      if(stepY <= maxY){
        if(stepZ <= maxZ){
          if(stepX <= maxX){
              stepX++;
            }else{
              stepX = minX;
              stepZ++;
            }
          }else{
            stepZ = minZ;
            stepY++;

          }
      }else{
        done = true;
      }



    }

    @Override
    public boolean hasNext (){
      return !done;
    }

    @Override
    public Block next (){
      if(done) throw new IllegalStateException("Iterator has no next index");
      Block out = getBlock();
      findNext();
      return out;
    }


  }

  public static class MineResetException extends  RuntimeException{
    public MineResetException(String text,Throwable t){
      super(text,t);
    }

  }
}
