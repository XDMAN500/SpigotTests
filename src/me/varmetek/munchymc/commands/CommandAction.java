package me.varmetek.munchymc.commands;

import jdk.nashorn.internal.ir.Block;
import me.varmetek.core.commands.CmdCommand;
import me.varmetek.core.service.Element;
import me.varmetek.core.util.Messenger;
import me.varmetek.munchymc.Main;
import org.apache.commons.lang.Validate;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by XDMAN500 on 5/6/2017.
 */
public class CommandAction implements Element
{
  private final CmdCommand[] commands ;
  private Main main;

  private Map<UUID,Action> maps = new HashMap<>(10);

  public CommandAction(Main plugin){
    main = plugin;
    commands = new CmdCommand[]{
      new CmdCommand.Builder("confirm", (sender,alias,args,length) ->{
        if(!sender.isPlayer()){
          Messenger.send(sender.asSender(),"&c Command is for players only");
          return;
        }
        Player pl = sender.asPlayer();
        if(maps.containsKey(pl.getUniqueId()) || hasAction(pl.getUniqueId(), ActionCommand.class) ){
           confirmAction(pl.getUniqueId());
        }else{
          Messenger.send(pl,"&c No actions to confirm at the moment.");
        }

      }).build(),

      new CmdCommand.Builder("cancel", (sender,alias,args,length) ->{
        if(!sender.isPlayer()){
          Messenger.send(sender.asSender(),"&c Command is for players only");
          return;
        }
        Player pl = sender.asPlayer();
        if(maps.containsKey(pl.getUniqueId())){
          cancelAction(pl.getUniqueId());
        }else{
          Messenger.send(pl,"&c No actions to cancel at the moment.");
        }

      }).build()
    };
  }

  public boolean hasAction(UUID d, Class<? extends Action> cl){

    return maps.containsKey(d) && maps.get(d).getClass().equals( cl);
  }
  public <T extends Action> void setAction(UUID id, T action){
    Validate.notNull(id);
    if(action == null){
      removeAction(id);
    }else{
      maps.put(id,action);
    }
  }

  public void removeAction(UUID id){
    Validate.notNull(id);
     maps.remove(id);
  }

  public void confirmAction(UUID id){
    Validate.notNull(id);
    ((ActionCommand)maps.get(id)).run();
    maps.remove(id);
  }

  public void cancelAction(UUID id){
    Validate.notNull(id);
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
  public static interface Action{

  }
  public static interface ActionCommand extends Action{
      void run();
  }
  public static interface ActionBlock<T extends Event> extends Action{
      void run(Block block);
  }
  public static interface ActionChat extends Action{
      void run(String chat);
  }

}
