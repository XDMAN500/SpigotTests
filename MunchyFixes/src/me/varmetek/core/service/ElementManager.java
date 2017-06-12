package me.varmetek.core.service;

import com.google.common.collect.Maps;
import me.varmetek.core.commands.CmdCommand;
import me.varmetek.core.commands.CmdSupplier;
import me.varmetek.core.util.Cleanable;
import me.varmetek.core.util.PluginCore;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.CommandMap;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.event.Listener;

import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by XDMAN500 on 3/5/2017.
 */
public class ElementManager implements Cleanable
{
  private PluginCore plugin;
  private final String commandPrefix;
  private Map<String,CmdCommand> commands = Maps.newHashMap();
  private Map<Class<? extends Element>,Element> elementRegisty = new HashMap<>();

  private CommandMap cmdMap = null;


  public ElementManager (PluginCore plugin, String commandPrefix){
    this.plugin = plugin;
    this.commandPrefix = commandPrefix;
    try{
      findCommandMap();
    }catch(Exception e){
      plugin.getLogger().warning("Failed to load the ElementManager");
      e.printStackTrace();
      Bukkit.getServer().getPluginManager().disablePlugin(plugin);
    }

  }


  public void findCommandMap() throws Exception{
    Server server = Bukkit.getServer();
    Method getCommandMap = server.getClass().getDeclaredMethod("getCommandMap");
    getCommandMap.setAccessible(true);
    cmdMap = (CommandMap) getCommandMap.invoke( server);
    if(!cmdMap.getClass().equals(SimpleCommandMap.class)){
      plugin.getLogger().warning("[!!!!] The command map has been tampered with. Hopefully commands still work.");
      plugin.getLogger().warning("[!!!!] Offender " + cmdMap.getClass().getName());
    }

  }


  public ElementManager registerListener(Listener list){
    Validate.notNull(list,"Listener cannot be null");
    register(new Element(){

      @Override
      public Listener supplyListener (){
        return list;
      }

      @Override
      public CmdCommand[] supplyCmd (){
        return null;
      }

      @Override
      public void clean (){

      }
    });
    return this;
  }
  public ElementManager registerAllListener(Listener... list){
    Validate.notNull(list);
    for(Listener i : list){
      registerListener(i);
    }
    return this;
  }

  public ElementManager register (Element ele){
    Validate.notNull(ele);
    CmdCommand[] cmds = ele.supplyCmd();
    Listener event = ele.supplyListener();

    if ((cmds != null && cmds.length != 0)){
      registerall(cmds);
    }

    if (event != null){
      plugin.getServer().getPluginManager().registerEvents(event, plugin);
    }

    elementRegisty.put(ele.getClass(), ele);
    return this;
  }

  public void registerAll (Element... extra){

    for (Element el : extra) {
      if (el == null) continue;
      register(el);
    }

  }

  public <T extends Element> Optional<T> getElement (Class<T> name){
    return Optional.of((T) elementRegisty.get(name));
  }


  private void registerCmd (CmdCommand cmd){
  /*  PluginCommand command = Bukkit.getPluginCommand(cmd.getName());
    Validate.notNull(command, "Command \"" + cmd.getName() + "\" is not registered in the plugin.yml");


    commands.put(cmd.getName(), cmd);
    command.setExecutor(this);
    command.setTabCompleter(this);
    if (cmd.hasAliases()) command.setAliases(cmd.getAliases());*/
  cmdMap.register(commandPrefix, cmd);

  }

  private void registerall (CmdCommand... cmd){
    List<String> errors = new ArrayList<>();


    for (int i = 0; i < cmd.length; i++) {
      try {
        registerCmd(cmd[i]);
      } catch (IllegalArgumentException expected) {
        errors.add(cmd[i].getName());
        continue;
      }
    }
    if (!errors.isEmpty()){
      throw new IllegalArgumentException("Error: these commands are not in the plugin.yml " + errors);
    }
  }

  private void register (CmdSupplier cmd){
    registerall(cmd.supplyCmd());

  }

 /* @Override
  public boolean onCommand (CommandSender sender, org.bukkit.command.Command command, String label, String[] args){
    CmdCommand cc = commands.get(command.getName());
    if (cc != null){
      cc.getCmdLogic().execute(new CmdSender(sender), label, args, args.length);
    }
    return false;
  }

  @Override
  public List<String> onTabComplete (CommandSender sender, org.bukkit.command.Command command, String alias, String[] args){
    CmdCommand cc = commands.get(command.getLabel());

    if (cc != null){
      return cc.getTabLogic().execute(new CmdSender(sender), alias, args, args.length);
    }
    return null;

  }*/

  @Override
  public void clean (){
    plugin = null;
    commands.clear();
    commands = null;
    elementRegisty.clear();
    elementRegisty = null;
  }


}
