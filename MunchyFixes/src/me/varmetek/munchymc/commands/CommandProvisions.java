package me.varmetek.munchymc.commands;

import me.varmetek.core.commands.CmdCommand;
import me.varmetek.core.service.Element;
import me.varmetek.munchymc.Main;
import me.varmetek.munchymc.backend.PlayerSession;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

/**
 * Created by XDMAN500 on 1/26/2017.
 */
public class CommandProvisions implements Element
{
	private Main main;
  private CmdCommand[] commands;

  public CommandProvisions(Main main){
		this.main = main;
    commands = new CmdCommand[]{
      new CmdCommand.Builder("run",(sender, alias, args[], length) -> {
        if(!sender.isPlayer() || args.length == 0)return;
        Player pl = sender.asPlayer();
        PlayerSession user = main.getPlayerHandler().getSession(pl);
        switch (args[0]){
          case "respawn":
            user.revive();

        }
      }).build()
    };
	}



  @Override
  public void clean(){
		main = null;
	}

	@Override
	public CmdCommand[] supplyCmd()
	{
		return commands;
	}

  @Override
  public Listener supplyListener (){
    return null;
  }
}
