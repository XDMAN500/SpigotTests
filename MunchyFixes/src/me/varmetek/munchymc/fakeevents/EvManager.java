package me.varmetek.munchymc.fakeevents;

import org.apache.commons.lang.Validate;
import org.bukkit.event.Event;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by XDMAN500 on 5/6/2017.
 */
public class EvManager
{
  private  Map<Class<? extends Event>,HandlerList<? extends Event>>  regis;

  public <T extends Event> void register( EvListener<T>  list){
    Validate.notNull(list);
    regis.entrySet().forEach( (entry)->{
     // C
     // if(entry.getKey().isAssignableFrom( Class<T> ))


    });

  }
  private class HandlerList<T extends Event>{
    private List<EvListener<T>> list ;

    protected HandlerList(){
      list = new LinkedList<>();
    }

    private void register( EvListener<T>  listener){
      Validate.notNull(listener);
      list.add(listener);
    }

    private void registerall(EvListener<T>... listener){
      for(EvListener<T>  list : listener){
        if(list == null)continue;
        register(list);
      }
    }
  }
}
