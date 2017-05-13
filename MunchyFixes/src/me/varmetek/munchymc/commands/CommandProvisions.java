package me.varmetek.munchymc.commands;

import me.varmetek.core.commands.CmdCommand;
import me.varmetek.core.service.Element;
import me.varmetek.munchymc.MunchyMax;
import me.varmetek.munchymc.backend.PlayerSession;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

/**
 * Created by XDMAN500 on 1/26/2017.
 */
public class CommandProvisions implements Element
{

  private CmdCommand[] commands;

  public CommandProvisions(){

    commands = new CmdCommand[]{
      new CmdCommand.Builder("run",(sender, alias, args[], length) -> {
        if(!sender.isPlayer() || args.length == 0)return;
        Player pl = sender.asPlayer();
        PlayerSession user = MunchyMax.getPlayerHandler().getSession(pl);
        switch (args[0]){
          case "respawn":
            user.revive();

        }
      }).build()
    };
	}



  @Override
  public void clean(){

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
