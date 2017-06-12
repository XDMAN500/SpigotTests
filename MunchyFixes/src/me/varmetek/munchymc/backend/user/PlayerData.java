package me.varmetek.munchymc.backend.user;

import com.google.common.collect.Maps;
import me.varmetek.core.user.BasePlayerData;

import java.util.Map;

/**
 * Created by XDMAN500 on 5/9/2017.
 */
public class PlayerData extends BasePlayerData
{
  //Constants
  private static final String
    KEY_MSG_JOIN = "joinMessage",
    KEY_MSG_LEAVE = "leaveMessage"
    ;
  protected String joinMessage = "&b{PLAYER} &7has joined the game";
  protected String leaveMessage = "&b{PLAYER} &7has left the game";

  public PlayerData (Map<String,Object> data){
    if(data.containsKey(KEY_MSG_JOIN) && data.get(KEY_MSG_JOIN)instanceof  String ){
      joinMessage = (String)data.get(KEY_MSG_JOIN);
    }

    if(data.containsKey(KEY_MSG_LEAVE) && data.get(KEY_MSG_LEAVE)instanceof  String ){
      leaveMessage = (String)data.get(KEY_MSG_LEAVE);
    }
  // setInput(  joinMessage ,data.get(KEY_MSG_JOIN));
   //setInput(  leaveMessage ,data.get(KEY_MSG_LEAVE));
  }

  public PlayerData (PlayerData data){
    this(data.serialize());
  }
  public PlayerData(){}

  @Override
  public PlayerData copy (){
    return new PlayerData(this.serialize());

  }

  public void setJoinMessage (String msg){
    joinMessage = msg;
  }

  public void setLeaveMessage (String msg){
    leaveMessage = msg;
  }

  public String getJoinMessage (){
    return joinMessage;
  }

  public String getLeaveMessage (){
    return leaveMessage;
  }


  @Override
  public Map<String,Object> serialize (){
    Map<String,Object> output = Maps.newHashMap();
    output.put(KEY_MSG_JOIN, joinMessage);
    output.put(KEY_MSG_LEAVE, leaveMessage);
    return output;
  }

  public static PlayerData deserialize(Map<String,Object> data){
    return new PlayerData(data);
  }

  @Override
  public void clean (){
    joinMessage = null;
    leaveMessage = null;
  }
}
