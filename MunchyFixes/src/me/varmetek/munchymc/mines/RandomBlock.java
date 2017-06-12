package me.varmetek.munchymc.mines;

import org.bukkit.material.MaterialData;

import java.util.List;
import java.util.Random;

/**
 * Created by XDMAN500 on 5/25/2017.
 */
public class RandomBlock
{
  private Random rand = new Random();
  private List<BlockData> list;

  public RandomBlock ( List<BlockData> blocks ){
    this.list = blocks;


  }

  public MaterialData next(int samples){

    BlockData chosen = null;
    double roll;

    for(int i = 0; i< samples && chosen == null;i++){

      for (BlockData data : list){
        roll = rand.nextDouble();
        if (data.getChance() >= roll){
          chosen = data;
          break;
        }
      }
      list.sort((BlockData b1, BlockData b2)-> { return rand.nextInt(3)-1; });

    }

    if(chosen == null){
      chosen =  list.get(rand.nextInt(list.size()));
    }


    return chosen.getData();
  }
}
