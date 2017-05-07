package me.varmetek.munchymc.backend;

import me.varmetek.core.util.SimpleMapEntry;
import me.varmetek.munchymc.util.Utils;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.function.Function;

/**
 * Created by XDMAN500 on 1/29/2017.
 */
public class ChatPlaceholder implements SimpleMapEntry
{
	private Function<AsyncPlayerChatEvent,String> action;
	private String id ;
	public ChatPlaceholder (String id, Function<AsyncPlayerChatEvent,String> action){
		this.id = id;
		this.action =action;
	}
	@Override
	public String ID ()
	{
		return id;
	}

	public String apply(AsyncPlayerChatEvent ev, String input){
		return input.replace(Utils.placeholder(id), action.apply(ev));

	}
}
