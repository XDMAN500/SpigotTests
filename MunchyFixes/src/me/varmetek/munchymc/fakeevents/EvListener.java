package me.varmetek.munchymc.fakeevents;

import org.bukkit.event.Event;

/**
 * Created by XDMAN500 on 5/6/2017.
 */
public class EvListener<T extends Event>
{
  private final EvLogic<T> logic;
  private Class<T> event;
  private byte priority = 0;
  private boolean runIfCanceled;


  private  EvListener (Class<T> eventType,EvLogic<T> ev){

    logic = ev;
    event = eventType;
  }

  public byte getPriority(){
    return priority;
  }

  public boolean runIfCanceled(){
    return runIfCanceled;
  }

  public Class<T> getEventType(){
    return event;
  }

  public static class Builder{
    private final EvLogic logic;
    private Class eventType;
    private byte priority = 0;
    private boolean runIfCanceled;

   private <E extends Event> Builder (Class<E> eventType,EvLogic<E> ev){
      logic = ev;
      this.eventType = eventType;
    }
    public Builder setPriority(byte priority){
     this.priority = priority;
     return this;
    }

    public Builder runIfCanceled(boolean run){
      this.runIfCanceled = run;
      return this;
    }

    public EvListener build(){
      EvListener ev = new EvListener(eventType,logic);
      ev.priority = this.priority;
      ev.runIfCanceled = this.runIfCanceled;
      return ev;
    }
  }

}
