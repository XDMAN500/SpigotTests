package me.varmetek.munchymc.backend;

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
    MSG_JOIN = "joinMessage",
    MSG_LEAVE = "leaveMessage"
    ;
  protected String joinMessage = "&b{PLAYER} &7has joined the game";
  protected String leaveMessage = "&b{PLAYER} &7has left the game";

  public PlayerData (Map<String,Object> data){
    setInput(  joinMessage ,data.get(MSG_JOIN));
    setInput(  leaveMessage ,data.get(MSG_LEAVE));
  }

  public PlayerData (PlayerData data){
    this(data.serialize());
  }
  public PlayerData(){}

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
    output.put(MSG_JOIN, joinMessage);
    output.put(MSG_LEAVE, leaveMessage);
    return output;
  }


  @Override
  public void clean (){
    joinMessage = null;
    leaveMessage = null;
  }
}
