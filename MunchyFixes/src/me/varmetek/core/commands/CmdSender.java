package me.varmetek.core.commands;

import org.apache.commons.lang.Validate;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

/**
 * Created by XDMAN500 on 3/4/2017.
 */
public  class CmdSender
{

	private final CommandSender sender;

	public CmdSender (CommandSender sender){
		Validate.notNull(sender);
		this.sender = sender;
	}

	private <T extends CommandSender> void illegalState(boolean condition, Class<T> clasz) throws IllegalStateException{
		if(!condition){throw new IllegalStateException("Source is not of type "+ clasz.getName());}
	}
	/****
	 *
	 * @return whether the sender is assignable to the type
	 * */
	public <T extends CommandSender> boolean isType(Class<T> type){
		Validate.notNull(type, "Type cannot be null");
		return (type.isAssignableFrom(sender.getClass()));
	}


	/**

	 @return the Player that send the command
	 @throws IllegalArgumentException if the sender is not assignable to the type
	 */
	public<T extends CommandSender> T asType(Class<T> type) throws IllegalStateException{
		illegalState(isType(type),type);
		return (T)sender;
	}
	/****
	 *
	 * @return if the sender is assignable to the player type
	 * */
	public boolean isPlayer(){
		return isType(Player.class);
	}
	/**

	@return the Player that send the command
	 @throws IllegalArgumentException if the sender is not assignable to type <tt>Player</tt>
	*/
	public Player asPlayer(){
		return asType(Player.class);
	}



	public boolean isConsole(){
		return isType(ConsoleCommandSender.class);
	}

	public ConsoleCommandSender asConsole(){
		return asType(ConsoleCommandSender.class);
	}

	public boolean isRCON(){
		return isType(RemoteConsoleCommandSender.class);
	}
	public RemoteConsoleCommandSender asRCON(){
		return asType(RemoteConsoleCommandSender.class);
	}

	public boolean isBlock(){
		return (sender instanceof BlockCommandSender);
	}
	public BlockCommandSender asBlock(){
		return asType(BlockCommandSender.class);
	}
	public boolean isProxied(){
		return (sender instanceof ProxiedCommandSender);
	}
	public ProxiedCommandSender asProxied(){
		return asType(ProxiedCommandSender.class);
	}

	public CommandSender asSender(){
		return sender;
	}


}
