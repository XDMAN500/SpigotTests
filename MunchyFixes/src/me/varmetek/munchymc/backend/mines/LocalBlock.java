package me.varmetek.munchymc.backend.mines;

import org.bukkit.material.MaterialData;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by XDMAN500 on 5/9/2017.
 */
public class LocalBlock
{
  private final MaterialData data;
  private final double chance;
  private final int weight;
  private static final DecimalFormat df = new DecimalFormat("##0.0##%");

  public LocalBlock (MaterialData data, int weight, double chance){
    this.data = data;
    this.chance = chance;
    this.weight = weight;
  }

  public LocalBlock(int itemID, byte itemDat, double chance, int weight){
      this(new MaterialData(itemID,itemDat),weight,chance);
  }


  public void put(Map<MaterialData,Double> map){
    map.put(data, chance);
  }


  public MaterialData getData (){
    return data;
  }


  public double getChance(){
    return chance;
  }


  public int getWeight (){
    return weight;
  }



  //["STONE"<1:0>] x 33(13%)
  public String toString(){
    return  "[\""+data.getItemType().name() +"\""+ "<"+data.getItemTypeId()+":"+data.getData()+ ">] " +
              "x "+ weight + "(" + df.format(chance) +")";
  }

  public static Map<MaterialData,Double> toMap(List<LocalBlock> list){
    Map<MaterialData,Double> map = new HashMap<>();
    for(LocalBlock bl : list){
      bl.put(map);
    }
    return map;
  }
}
