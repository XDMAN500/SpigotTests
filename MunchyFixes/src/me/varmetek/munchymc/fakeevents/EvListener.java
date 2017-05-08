package me.varmetek.munchymc.fakeevents;

import org.bukkit.event.Event;

/**
 * Created by XDMAN500 on 5/6/2017.
 */
public class EvListener<T extends Event>
{
  private final EvLogic<T> logic;
  private int priority = 0;
  private boolean runIfCanceled;

  private EvListener (EvLogic<T> ev){
    logic = ev;
  }

  public static class Builder<T extends Event>{
    private EvLogic<T> logic;
    private int priority = 0;
    private boolean runIfCanceled;

   private Builder(EvLogic<T> ev){
      logic = ev;
    }
    public Builder setPriority(int priority){
     this.priority = priority;
     return this;
    }

    public Builder runIfCanceled(boolean run){
      this.runIfCanceled = run;
      return this;
    }

    public EvListener<T> build(){
      EvListener<T> ev = new EvListener<T>(logic);
      ev.priority = this.priority;
      ev.runIfCanceled = this.runIfCanceled;
      return ev;
    }
  }

}
