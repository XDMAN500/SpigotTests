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
  private  Map<Class<? extends Event>,HandlerList>  regis;

  public <T extends Event> void register( EvListener<T>  list){
    Validate.notNull(list);
    if(!regis.containsKey(list.getEventType())){
      regis.put(list.getEventType(),new HandlerList());
    }

    regis.get(list.getEventType()).register(list);
  }
  private class HandlerList{
    private List<EvListener> list ;

    protected HandlerList(){
      list = new LinkedList<>();
    }

    private void register( EvListener  listener){
      Validate.notNull(listener);
      list.add(listener);
    }

    private void registerall(EvListener... listener){
      for(EvListener  list : listener){
        if(list == null)continue;
        register(list);
      }
    }
  }
}
