package me.varmetek.munchymc.backend.mines;

import org.bukkit.material.MaterialData;

import java.util.Map;

/**
 * Created by XDMAN500 on 5/9/2017.
 */
public class LocalBlock implements Map.Entry<MaterialData,Integer>, Comparable<LocalBlock>
{
  private MaterialData data;
  private int weight;

  public LocalBlock (MaterialData data, int weight){
    this.data = data;
    this.weight = weight;
  }

  public LocalBlock(int itemID, byte itemDat, int weight){
    this.data = new MaterialData(itemID,itemDat);
    this.weight = weight;
  }

  public void put(Map<MaterialData,Integer> map){
    map.put(getKey(),getValue());
  }



  @Override
  public MaterialData getKey (){
    return data;
  }

  @Override
  public Integer getValue (){
    return weight;
  }

  @Override
  public Integer setValue (Integer value){
    return weight = value.intValue();
  }

  @Override
  public int compareTo (LocalBlock other){
    return this.weight - other.weight;
  }

  //"AIR"[0:3]x33
  public String toString(){
    return  "\""+data.getItemType().name() +"\""+ "["+data.getItemTypeId()+":"+data.getData()+"]x"+weight;
  }
}
