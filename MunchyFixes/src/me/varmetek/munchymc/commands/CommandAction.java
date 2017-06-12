package me.varmetek.munchymc.commands;

import me.varmetek.core.commands.CmdCommand;
import me.varmetek.core.commands.CmdSender;
import me.varmetek.core.service.Element;
import me.varmetek.core.util.Messenger;
import org.apache.commons.lang.Validate;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Predicate;

/**
 * Created by XDMAN500 on 5/6/2017.
 */
public class CommandAction implements Element
{
  private final CmdCommand[] commands;
  private Plugin plugin;
  private Map<UUID,Action> maps = new HashMap<>(10);

  public CommandAction (Plugin plu){
    plugin = plu;

    commands = new CmdCommand[]{
      new CmdCommand.Builder("confirm", (cmd) -> {
        CmdSender sender = cmd.getSender();
        if (!sender.isPlayer()){
          Messenger.send(sender.asSender(), "&c Command is for players only");
          return;
        }
        Player pl = sender.asPlayer();
        if (hasAction(pl.getUniqueId(), ActionCommand.class)){
          confirmAction(pl.getUniqueId());
        } else {
          Messenger.send(pl, "&c No actions to confirm at the moment.");
        }

      }).build(),

      new CmdCommand.Builder("cancel", (cmd) -> {
        CmdSender sender = cmd.getSender();
        if (!sender.isPlayer()){
          Messenger.send(sender.asSender(), "&c Command is for players only");
          return;
        }
        Player pl = sender.asPlayer();
        if (maps.containsKey(pl.getUniqueId())){
          cancelAction(pl.getUniqueId());
        } else {
          Messenger.send(pl, "&c No actions to cancel at the moment.");
        }

      }).build()
    };
  }

  public boolean hasAction (UUID d, Class<? extends Action> cl){

    return maps.containsKey(d) && maps.get(d).getClass().equals(cl);
  }

  public <T extends Event> void setAction (UUID id,Action action){
    Validate.notNull(id);
    cancelAction(id);

    if(action != null){
      maps.put(id, action);
    }




  }

  public <T extends Event> void setActionEvent (UUID id, Predicate<T> runnable){
    Validate.notNull(id);
    cancelAction(id);

    if (runnable != null){
      maps.put(id, new ActionEvent<>(runnable));
    }
  }

 /* public void removeAction (UUID id){
    Validate.notNull(id);
    maps.remove(id);
  }*/

  public void confirmAction (UUID id){
    Validate.notNull(id);
    if(!maps.containsKey(id))return;
    maps.get(id).confirm();
    maps.remove(id);
  }

  public void cancelAction (UUID id){
    Validate.notNull(id);
    if(!maps.containsKey(id))return;
    maps.get(id).cancel();
    maps.remove(id);
  }

  @Override
  public void clean (){

  }

  @Override
  public CmdCommand[] supplyCmd (){
    return commands;
  }

  @Override
  public Listener supplyListener (){
    return null;
  }


  public interface Action{
    void confirm();

    void cancel();
  }


  public class ActionCommand implements Action{
    public void confirm(){

    }

    public void cancel(){

    }
  }

  public class ActionEvent<T extends Event>  implements Listener,Action{
    private final Predicate<T> run;
    public ActionEvent( Predicate<T> run) {
      this.run = run;

      plugin.getServer().getPluginManager().registerEvents(this,plugin);
    }

    @EventHandler
    protected void run(T event){
      if(run.test(event)){
       confirm();
      }

    }

    @Override
    public void confirm (){
      HandlerList.unregisterAll(this);
    }

    @Override
    public void cancel (){
      HandlerList.unregisterAll(this);
    }
  }


}
