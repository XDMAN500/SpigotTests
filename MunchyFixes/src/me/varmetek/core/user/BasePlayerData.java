package me.varmetek.core.user;

import me.varmetek.core.util.Cleanable;
import me.varmetek.munchymc.backend.user.PlayerData;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.Map;

/**
 * Created by XDMAN500 on 5/9/2017.
 */
public abstract class BasePlayerData implements  ConfigurationSerializable, Cleanable
{

  /**
   *
   * Used to deserialize data
   * **/
  public BasePlayerData (Map<String,Object> data){
   ///Serialize stuff
  }

  /***
   * Used to copy data;
   * */
  public BasePlayerData (PlayerData data){
    this(data.serialize());
  }

  /****
   *
   * Used to load default data
   *
   */

  public BasePlayerData(){
    //Default data
  }

  protected boolean checkInput(Class<?> type,Object input){
    return input!= null &&
             type.isInstance(input);
  }

  protected <T> boolean setInput( T source,Object input){
    if(checkInput(source.getClass(),input)){
      source  = (T)input;
      return true;
    }
    return false;
  }

  protected <T> boolean setInput( T source,Object input, T def){
    if(!setInput(source,input)){
      source = def;
      return false;
    }

    return true;
  }


  public abstract BasePlayerData copy();
}
