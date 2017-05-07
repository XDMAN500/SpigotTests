package me.varmetek.core.service;

import com.google.common.collect.Maps;
import me.varmetek.core.commands.CmdCommand;
import me.varmetek.core.commands.CmdSender;
import me.varmetek.core.commands.CmdSupplier;
import me.varmetek.core.util.Cleanable;
import me.varmetek.core.util.PluginMain;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created by XDMAN500 on 3/5/2017.
 */
public class ElementManager implements CommandExecutor, TabCompleter, Cleanable
{
	private PluginMain plugin;
	private Map<String,CmdCommand> commands = Maps.newHashMap();
	private Map<Class<? extends Element>, Element>  elementRegisty= new HashMap<>();


	public ElementManager (PluginMain plugin) {
    this.plugin = plugin;

  }

	public ElementManager register(Element ele){
		Validate.notNull(ele);
		CmdCommand[] cmds = ele.supplyCmd();
		Listener event = ele.supplyListener();

		if((cmds != null && cmds.length != 0)){
			 registerall(cmds);
		}

		if(event != null){
			plugin.getServer().getPluginManager().registerEvents(event,  plugin);
		}

		elementRegisty.put(ele.getClass(),ele);
		return this;
	}

  public void registerAll(Element... extra){

    for(Element el: extra){
      if(el == null)continue;
      register(el);
    }

  }

  public <T extends Element> Optional<T> getElement(Class<T> name){
      return Optional.of((T)elementRegisty.get(name));
  }


	private void registerCmd(CmdCommand cmd) throws IllegalArgumentException{
    PluginCommand command = Bukkit.getPluginCommand(cmd.getName());
    Validate.notNull(command, "Command \""+ cmd.getName() +"\" is not registered in the plugin.yml");


    commands.put(cmd.getName(),cmd);
		command.setExecutor(this);
		command.setTabCompleter(this);
		if(cmd.hasAliases()) command.setAliases(cmd.getAliases());

	}

	private void registerall(CmdCommand... cmd) throws IllegalArgumentException {
		for(int i = 0; i <cmd.length; i++){
			registerCmd(cmd[i]);
		}
	}

	private void register(CmdSupplier cmd){
		registerall(cmd.supplyCmd());

	}

	@Override
	public boolean onCommand (CommandSender sender, org.bukkit.command.Command command, String label, String[] args)
	{
		CmdCommand cc   = commands.get(command.getName());
		if(cc!= null){ cc.getCmdLogic().execute( new CmdSender(sender), label, args,args.length);}
		return false;
	}

	@Override
	public List<String> onTabComplete (CommandSender sender,  org.bukkit.command.Command command, String alias, String[] args)
	{
		CmdCommand cc   =  commands.get(command.getLabel());

		if(cc != null){
			return cc.getTabLogic().execute( new CmdSender(sender), alias, args, args.length);
		}
		return null;

	}

	@Override
	public void clean ()
	{
		plugin = null;
		commands.clear();
		commands = null;
		elementRegisty.clear();
		elementRegisty = null;
	}
}
