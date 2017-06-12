package me.varmetek.core.scoreboard;

import com.google.common.collect.Maps;
import me.varmetek.core.util.Cleanable;
import me.varmetek.core.util.Messenger;
import org.apache.commons.lang.Validate;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;

import java.util.*;

/**
 * Created by XDMAN500 on 1/21/2017.
 */
public class SidebarHandler implements Cleanable
{

  private Scoreboardx scoreboard;
  private final SidebarHandler sb = this;
  private BukkitRunnable autoRender = new BukkitRunnable()
  {
    @Override
    public void run (){
      try {
        sb.render();
      }catch(Sidebar.SidebarRenderException e){
        e.printStackTrace();
      }
    }
  };

  private Map<String,Sidebar> map;
  private Bar[] display;

  private String lastname = "";
  private RenderOrder order = new RenderOrder();


  SidebarHandler (Scoreboardx scoreboard){
    Validate.notNull(scoreboard);
    this.scoreboard = scoreboard;
    display = new Bar[]{
      new Bar("sidebar0"), new Bar("sidebar1")
    };
    map = Maps.newHashMap();
    autoRender.runTaskTimer(scoreboard.plugin, 1, 20L);


  }

  protected class Bar
  {
    final String name;
    final Objective obj;
    final String[] lines = new String[16];

    public Bar (String name){
      this.name = name;
      obj = scoreboard.getScoreboard().registerNewObjective(name, "dummy");
    }

    @Override
    public int hashCode(){
      return name.hashCode();
    }


    public void clean (){
     for(int i = 0; i< lines.length; i++){
       clearLine(i);
     }
    }

    public void clearLine (int i){
      if (lines[i] == null) return;
      scoreboard.getScoreboard().resetScores(lines[i]);
      lines[i] = null;
    }

    public void display (){
      obj.setDisplaySlot(DisplaySlot.SIDEBAR);
    }

    public void hide (){
      obj.setDisplaySlot(DisplaySlot.BELOW_NAME);
    }

    public void setLine (int i, String text){

      if(text == null){
        clearLine(i);
      }else{
        if(text.equals(lines[i])) return;
        clearLine(i);
        lines[i] = text;
        obj.getScore(text).setScore(i);
      }


    }

    public void destroy (){
      clean();
      obj.unregister();
    }

    private void debug(){
      Messenger.sendAll("[");
      for(int i = display.length-1; i>= 0; i--){
        Messenger.sendAll(lines[i]);
      }

      Messenger.sendAll("] ");
    }
  }

  public Scoreboardx getHandle (){
    return scoreboard;
  }


  public Sidebar register (Sidebar.Builder bar){
    Validate.notNull(bar);
    Sidebar sb = bar.build(this);
    map.put(sb.ID(), sb);
    return sb;

  }

  public void unregister (Sidebar bar){
    Validate.notNull(bar);
    unregister(bar.id);

  }

  public void unregister (String id){
    map.remove(id);

  }

  public Optional<Sidebar> get (String id){
    return Optional.ofNullable(map.get(id));
  }

  public RenderOrder getRenderOrder (){
    return order;
  }

  public boolean hasDisplay (){
    return !order.isEmpty();
  }

  boolean isDisplayed (String id){
    return hasDisplay() && get(id).isPresent() && order.hasRequested(map.get(id));
  }


  public boolean has (String id){
    return get(id).isPresent();
  }


  public void clear (){

  }

  private int _next (){
    return display.length - 1;
  }

  protected void render () throws Sidebar.SidebarRenderException{

    if (!order.getCurrent().isPresent())return;

    Sidebar current = order.getCurrent().get();

    Bar next = display[0];
    display[0] = display[1];
    display[1] = next;

    current.render(display[0]);
    display[0].display();

    current.render(display[1]);



  }

  private void cleanBar (){
    for (Bar obj : display) {
      obj.clean();
    }
   // Bukkit.getLogger().info("[SidebarHandler] cleaning sidebar");
  }

  @Override
  public void clean (){
    autoRender.cancel();
    autoRender = null;
    scoreboard = null;
    map.clear();
    map = null;
    display = null;
    order.clean();
    order = null;


  }

  public class RenderOrder implements Cleanable
  {

    private Map<Sidebar,Long> cannidate = new HashMap<>();

    private List<Sidebar> order = new ArrayList<>();
    private Comparator<Sidebar> compare = (a, b) -> {
      int dPriority = a.getPriority() - b.getPriority();
      if (dPriority != 0) return dPriority;

      if (cannidate.containsKey(a)){
        if (cannidate.containsKey(b)){
          return (int) (cannidate.get(a) - cannidate.get(b));
        } else return 1;

      } else if (cannidate.containsKey(b)){
        return -1;

      } else return 0;


    };


    public void request (Sidebar b){
      if (hasRequested(b)) return;

      cannidate.put(b, System.currentTimeMillis());
      /*if (getCurrent().isPresent() && compare.compare(b, getCurrent().get()) > -1){
        //cleanBar();
      }*/
      reSort();


    }

    public void leave (Sidebar b){
      if (!hasRequested(b)) return;
      /*if (getCurrent().isPresent() && getCurrent().get().equals(b)){
        //cleanBar();

      }*/

      cannidate.remove(b);
      reSort();

    }

    private void reSort (){
      order.clear();
      order.addAll(cannidate.keySet());
      order.sort(compare);
    }

    public boolean isEmpty (){
      return order.isEmpty();
    }

    public Optional<Sidebar> getCurrent (){
      if (order.isEmpty()) return Optional.empty();

      return Optional.ofNullable(order.get(0));
    }


    public boolean hasRequested (Sidebar b){
      return order.contains(b);
    }


    @Override
    public void clean (){
      order.clear();
      order = null;
    }
  }


}
