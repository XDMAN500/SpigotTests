package me.varmetek.munchymc.mines;

import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.Vector;
import me.varmetek.core.util.Cleanable;
import me.varmetek.core.util.Messenger;
import me.varmetek.munchymc.backend.exceptions.ConfigException;
import me.varmetek.munchymc.util.BlockLoc;
import me.varmetek.munchymc.util.Utils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.material.MaterialData;

import java.util.*;

/**
 * Created by XDMAN500 on 5/10/2017.
 */
public class Mine implements Cleanable
{
  public static final int DEFAULT_DELAY = 600;



  private MineManager manager;
 // private ProtectedRegion pr;
  private final String name;
  private BlockLoc
    maxPoint = null,
    minPoint = null;
  private final World world;
  private long nextReset = Utils.getSystemSeconds();
  private int resetDelay = DEFAULT_DELAY;
  private ResetType resetType = ResetType.TIMER;
  private MineGenerator genOption;



  private Location saveSpot;

  public  Map<MaterialData,Integer> blocks = new HashMap<>();
  private static final Comparator<BlockData> compare = (a,b)->{return a.getWeight()-b.getWeight();};

  protected Mine(MineManager man, String name, Map<String,Object> data) throws ConfigException{
    Validate.notNull(name);

    int maxX,maxY,maxZ;
    int minX,minY,minZ ;
    List<String>  blockList;



    String prefix = "(" + name + ")";

    try{

      this.world = Bukkit.getWorld((String)data.get("world"));
      Validate.notNull(world);
    }catch(Exception e){
      throw new ConfigException(prefix + "Error extracting raw world data for Mine",e);

    }


    try{
      maxX = (int)data.get("maxPointX");
      maxY = (int)data.get("maxPointY");
      maxZ = (int)data.get("maxPointZ");
    }catch(Exception e){
      throw new ConfigException(prefix +"Error extracting raw max point data for Mine",e);

    }


    try{
      minX = (int)data.get("minPointX");
      minY = (int)data.get("minPointY");
      minZ = (int)data.get("minPointZ");

   }catch(Exception e){
    throw new ConfigException(prefix +"Error extracting raw min point data for Mine",e);

    }

    try{
     if(data.containsKey("resetDelay")){
       resetDelay = (int)data.get("resetDelay");
     }else{
       resetDelay = DEFAULT_DELAY;
     }
  }catch(Exception e){
    throw new ConfigException(prefix +"Error extracting resetDelay for Mine",e);

  }

    try{
      if(data.containsKey("resetType")){
        resetType = ResetType.valueOf((String)data.get("resetType"));
      }else{
        resetType = ResetType.TIMER;
      }

      if(resetType == null){
        resetType = ResetType.TIMER;
      }
    }catch(Exception e) {
      throw new ConfigException(prefix + "Error extracting resetType for Mine", e);
    }

    try{
      blockList = (List<String>)data.get("blocks");
      Validate.notNull(blockList);

    }catch(Exception e){
       throw new ConfigException(prefix + "Error extracting raw block list data for Mine",e);

    }



    try{

      blockList.forEach( (in)->{
        String[]  block = in.split("-");
       this.blocks.put( new MaterialData( Integer.parseInt(block[0]),Byte.parseByte(block[1])), Integer.parseInt(block[2]));
      });
    }catch(Exception e){
      throw new ConfigException(prefix + "Error parsing block list" ,e);
    }



    manager = man;
    this.name = name;
    this.setRegion(new BlockLoc(maxX,maxY,maxZ), new BlockLoc(minX,minY,minZ));
    this.genOption  = new MineGeneratorImpl2(this);

  }



  Mine(MineManager man, String name, BlockVector max, BlockVector min, World world){

    this(man,name,new BlockLoc(max), new BlockLoc(min),world);
  }




   Mine(MineManager man, String name, BlockLoc max, BlockLoc min, World world){
    Validate.notNull(name);
    Validate.notEmpty(name);
    Validate.isTrue(StringUtils.isNotBlank(name));
    Validate.notNull(man);
    Validate.notNull(max);
    Validate.notNull(min);
    Validate.notNull(world);
    Validate.isTrue(StringUtils.isNotBlank(name));

    manager = man;

    setRegion(max,min);
    this.name = name;
    this.world = world;
    this.genOption = new MineGeneratorImpl2(this);


/*
    pr.setFlag(DefaultFlag.BLOCK_BREAK, State.ALLOW);
    pr.setFlag(DefaultFlag.BLOCK_PLACE, State.ALLOW);
    pr.setFlag(DefaultFlag.BUILD, State.ALLOW);
    pr.setFlag(DefaultFlag.EXP_DROPS,State.DENY);
    pr.setFlag(DefaultFlag.PASSTHROUGH,State.ALLOW);
    pr.setFlag(DefaultFlag.FALL_DAMAGE,State.DENY);
    pr.setFlag(DefaultFlag.MOB_SPAWNING,State.DENY);
    */


  }
  public MineGenerator getGenerator(){
     return  genOption;
  }
  public Map<MaterialData,Integer> getRawBlockData(){
     return blocks;
  }
  public boolean hasEmptyData(){
     return blocks.isEmpty();
  }

  public void setSafeSpot(Location loc){
    this.saveSpot = loc;
  }

  public Optional<Location> getSafeSpot(){
    return Optional.of(saveSpot);
  }


  public World getWorld(){
    return world;
  }

  public int getResetDelay(){
    return resetDelay;
  }

 /* public int getResetTime(){
    return nextReset;
  }*/

  public long timeTillReset(){
    return nextReset - Utils.getSystemSeconds();
  }

  public void setResetDelay(int time){

    resetDelay = time;
    nextReset = 0;
  }

  public String getName(){
    return name;
  }

  public void teleportPlayers(){

    List<Player> players = world.getPlayers();



    for(Player player : players){
      Location loc = player.getLocation();
      if(contains(player.getLocation())){
        Messenger.send(player, "&7 Mine &a"+name+" &7has reset.");
        if(saveSpot != null){
          player.teleport(saveSpot);
        }
      }


      /*if(pr.contains(loc.getBlockX(),loc.getBlockY(),loc.getBlockZ())){
        Messenger.send(player, "&7Mine &c"+name+" &7has reset.");
        player.teleport(saveSpot);

      }*/

    }
  }

  public void resetMine(){

    nextReset = Utils.getSystemSeconds() + resetDelay;


   //if(pr == null) throw new IllegalStateException("Mine \""+ name +"\" must not have a null region");
   if(blocks.isEmpty() || genOption.isWorking())return;//Skip reset if blocks are empty;
    teleportPlayers();
  genOption.generate();




  }

  public void setResetType(ResetType type){
    Validate.notNull(type);

    this.resetType = type;
  }
  public void clearMine(){
    nextReset = 0;
    teleportPlayers();
    genOption.empty();
  }

  public BlockLoc getMaxPoint(){
    return maxPoint;
  }

  public BlockLoc getMinPoint(){
    return minPoint;
  }



  public void copyBlocks(Mine mine){

    blocks.clear();
    blocks.putAll(mine.blocks);

  }
  public void copy(Mine mine){
    manager = mine.manager;
   /* pr.getFlags().clear();
    for (Map.Entry<Flag<?>,Object> f : mine.pr.getFlags().entrySet()) {
      pr.getFlags().put(f.getKey(),f.getValue());
    }*/
    blocks.clear();
    blocks.putAll(mine.blocks);
    resetDelay = mine.resetDelay;
    nextReset = 0;
  }

  public void setRegion(BlockVector max, BlockVector min){
   setRegion(max.getBlockX(),max.getBlockY(),max.getBlockZ(),
     min.getBlockX(),min.getBlockY(),min.getBlockZ());



  }

  public void setRegion(Location max, Location min){
    setRegion(max.getBlockX(),max.getBlockY(),max.getBlockZ(),
      min.getBlockX(),min.getBlockY(),min.getBlockZ());



  }

  public void setRegion(BlockLoc max, BlockLoc min){
    Validate.isTrue(max.getWorld() ==  min.getWorld(), "Points must be in the same world");
    setRegion(max.getX(),max.getY(),max.getZ(),
      min.getX(),min.getY(),min.getZ());

  /*  pr = new ProtectedCuboidRegion("MineMc_"+name,
                               max, min);*/

  }



  public void setRegion(int x1, int y1, int z1,int x2, int y2, int z2){
    int maxX = x1 > x2 ? x1 : x2;
    int maxY = y1 > y2 ? y1 : y2;
    int maxZ = z1 > z2 ? z1 : z2;

    int minX = x1 < x2 ? x1 : x2;
    int minY = y1 < y2 ? y1 : y2;
    int minZ = z1 < z2 ? z1 : z2;

    maxPoint = new BlockLoc(null,maxX,maxY,maxZ);
    minPoint = new BlockLoc(null,minX,minY,minZ);

  /*  pr = new ProtectedCuboidRegion("MineMc_"+name,
                               max, min);*/

  }

  public boolean contains(BlockLoc loc) {
   return contains(loc.getX(),loc.getY(),loc.getZ());
  }

  public boolean contains(org.bukkit.util.Vector loc) {
    return contains(loc.getBlockX(),loc.getBlockY(),loc.getBlockZ());
  }

  public boolean contains(Vector loc) {
  return contains(loc.getBlockX(),loc.getBlockY(),loc.getBlockZ());
}

  public boolean contains(Location loc) {

    return world.getUID() == loc.getWorld().getUID() ? contains(loc.getBlockX(),loc.getBlockY(),loc.getBlockZ()) : false;
  }


  public boolean contains(int x, int y, int z) {

    boolean bX = x >= this.minPoint.getX() && x < (this.maxPoint.getX() + 1);
    boolean bY = y >= this.minPoint.getY() && y < (this.maxPoint.getY() + 1);
    boolean bZ = z >= this.minPoint.getZ() && z < (this.maxPoint.getZ() + 1);
    return bX && bY && bZ;
  }



  public long volume() {
    long xLength = this.maxPoint.getX() - this.minPoint.getX() + 1;
    long yLength = this.maxPoint.getY() - this.minPoint.getY() + 1;
    long zLength = this.maxPoint.getZ() - this.minPoint.getZ() + 1;

    long result = xLength * yLength;
    result = result < Long.MAX_VALUE ? result * zLength : Long.MAX_VALUE;
    return result+1;


    /*try {
      long v = MathUtils.checkedMultiply((long)xLength, (long)yLength);
      v = MathUtils.checkedMultiply(v, (long)zLength);
      return v > 2147483647L?2147483647:(int)v;
    } catch (ArithmeticException var7) {
      return 2147483647;
    }*/
  }


  public List<BlockData> getBlockData(){

    List<BlockData> output = new ArrayList<>(blocks.size());


    blocks.entrySet().forEach( (entry) ->{

      output.add( new BlockData(entry.getKey(),entry.getValue(), entry.getValue().doubleValue()/totalWeight()));
    });
    output.sort(compare);
    return output;
  }


  public boolean containsData(MaterialData data){
    Validate.notNull(data);
    return blocks.containsKey(data);
  }
  public void setData(MaterialData data, int weight){
    Validate.notNull(data);
    blocks.put(data,weight);
  }

  public void removeData(MaterialData data){
    Validate.notNull(data);
    blocks.remove(data);
  }

  public int totalWeight(){

    int max = 0;
    for(Integer num :blocks.values()){
      if(num == null)continue;
      max += num.intValue();
    }

    return max;
  }
  public int getDataWeight(MaterialData data){
    Validate.notNull(data);
    Validate.isTrue(containsData(data));
    return blocks.get(data);
  }

  public long getFilledBlocks(){
    return ((MineGeneratorImpl2)genOption).getBlockCount();
  }

  public long getEmptyBlocks(){
    return volume()- getFilledBlocks();
  }

  public double getComposition(){
    Long blocks = ((MineGeneratorImpl2)genOption).getBlockCount();
    return blocks.doubleValue() / Long.valueOf(this.volume()).doubleValue();
  }

  public void poke(){
    ((MineGeneratorImpl2)genOption).calculateBlockCount();
    switch (resetType){
      case COMPOSITION:{



        if(getComposition() <= 0.30){
          resetMine();
        }

      }break;
      case TIMER:{
        timeSet();
      }
    }
  }
  public void timeSet(){

    if(Utils.getSystemSeconds() >= nextReset){
      resetMine();
    }

   /* nextReset++;
    if(nextReset >= resetDelay){

    }*/
  }
/*
  public <T extends Flag<V>, V> void setFlag(T flag, @Nullable V val){
    pr.setFlag(flag,val);
  }

  @Nullable
  public <T extends Flag<V>, V> V getFlag(T flag) {
    return pr.getFlag(flag);
  }
*/
  public Map<String,Object> serialize(){
    Map<String,Object> output = new LinkedHashMap<>();
    output.put("world", world.getName());
   // BlockLoc max = pr.getMaximumPoint();
    output.put("maxPointX", maxPoint.getX());
    output.put("maxPointY", maxPoint.getY());
    output.put("maxPointZ", maxPoint.getZ());
    //BlockVector min = pr.getMinimumPoint();
    output.put("minPointX", minPoint.getX());
    output.put("minPointY", minPoint.getY());
    output.put("minPointZ", minPoint.getZ());

    output.put("resetDelay", resetDelay);
    output.put("resetType",resetType.name());

    List<String> serial = new ArrayList<>();
    blocks.forEach( (data, id)->{serial.add( data.getItemTypeId() + "-" + data.getData()+ "-"+ id );});
    output.put("blocks",serial);
    return output;
  }

  @Override
  public void clean (){
    manager = null;
    maxPoint = null;
    minPoint = null;
    blocks = null;
    genOption = null;

  }

  public enum ResetType{
    TIMER,COMPOSITION
  }
}
