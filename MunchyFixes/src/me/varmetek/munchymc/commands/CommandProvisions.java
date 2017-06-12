package me.varmetek.munchymc.commands;

import com.google.common.collect.ImmutableList;
import me.varmetek.core.commands.CmdCommand;
import me.varmetek.core.commands.CmdSender;
import me.varmetek.core.service.Element;
import me.varmetek.munchymc.MunchyMax;
import me.varmetek.munchymc.backend.user.PlayerSession;
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
      new CmdCommand.Builder("run").setLogic(

        (cmd)->{

          CmdSender sender = cmd.getSender();
          int len = cmd.getArguments().size();
          String alias = cmd.getAlias();
          ImmutableList<String> args = cmd.getArguments();

        if(!sender.isPlayer() || args.isEmpty())return;
        Player pl = sender.asPlayer();
        PlayerSession user = MunchyMax.getPlayerHandler().getSession(pl);
        switch (args.get(0).toLowerCase()){
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
