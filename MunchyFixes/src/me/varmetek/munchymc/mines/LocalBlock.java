package me.varmetek.munchymc.mines;

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



  // Desired format => "STONE" [0:1] x 33 ( 13% )
  public String toString(){
    StringBuilder sb = new StringBuilder();
    sb.append("\""+data.getItemType().name() +"\""); // Item name
    sb.append(" "); //Spacing
    sb.append("["+data.getItemTypeId()+":"+data.getData()+ "]"); // Item id
    sb.append(" x "); //Spacing
    sb.append(weight);
    sb.append(" "); //Spacing
    sb.append("( " + df.format(chance) +" )");// Percentage


    return  sb.toString();
  }

  public static Map<MaterialData,Double> toMap(List<LocalBlock> list){
    Map<MaterialData,Double> map = new HashMap<>();
    for(LocalBlock bl : list){
      bl.put(map);
    }
    return map;
  }
}
