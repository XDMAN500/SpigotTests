package me.varmetek.munchymc.commands;

import me.varmetek.core.commands.CmdCommand;
import me.varmetek.core.service.Element;
import me.varmetek.core.util.Messenger;
import me.varmetek.munchymc.MunchyMax;
import me.varmetek.munchymc.backend.PlayerSession;
import me.varmetek.munchymc.listeners.UserTestModeChangeEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class CommandTest implements Element
{

	private final CmdCommand[]  commands;

	public CommandTest(){

		commands = new CmdCommand[]{
			new CmdCommand.Builder("test", (sender,alias,args,length) ->{
				if(!sender.isPlayer())return ;
				Player pl = sender.asPlayer();
				PlayerSession user = MunchyMax.getPlayerHandler().getSession(pl);

				UserTestModeChangeEvent event = new UserTestModeChangeEvent(user,!user.isTesting());
				Bukkit.getServer().getPluginManager().callEvent(event);
				if(!event.isCancelled())user.setTestMode(event.nextMode());
				if (user.isTesting()){
					Messenger.send(pl, "");
					Messenger.send(pl, "&2   YOU HAVE ENTERED TEST MODE");
					Messenger.send(pl, "");

				}else{
					Messenger.send(pl, "");
					Messenger.send(pl, "&e   YOU HAVE EXITED TEST MODE");
					Messenger.send(pl, "");
				}


			}).build()
		};
	}



	@Override
	public void clean(){

	}

	@Override
	public CmdCommand[] supplyCmd (){
		return commands;
	}

	@Override
	public Listener supplyListener(){
		return null;
	}
}
