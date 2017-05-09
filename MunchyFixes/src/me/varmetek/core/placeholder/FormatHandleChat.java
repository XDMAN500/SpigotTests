package me.varmetek.core.placeholder;

import me.varmetek.core.util.Messenger;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 * Created by XDMAN500 on 2/5/2017.
 */
public class FormatHandleChat extends FormatHandleGeneric<AsyncPlayerChatEvent>
{
	private String format ;
	private FormatHandleGeneric<AsyncPlayerChatEvent>  forMessage;
	private static final String CHAT_MESSAGE = "%2$s";

	public FormatHandleChat(String format, String msgPlaceHolder){
		Validate.isTrue(StringUtils.isNotBlank(format));
		this.format = format;
		forMessage = new FormatHandleGeneric<>();
		register(msgPlaceHolder, (ev) -> {
			ev.setMessage(Messenger.color(forMessage.apply(ev.getMessage(),ev)));
			return CHAT_MESSAGE;
		});
	}

	public String apply(final String input, AsyncPlayerChatEvent ev){
		String output = input;

		for(String placeholder :placeHolders.keySet()){
			output = output.replace(placeholder, placeHolders.get(placeholder).apply(ev));
		}
		return output;


	}

	public FormatHandleGeneric<AsyncPlayerChatEvent>   forChatMessage(){
		return forMessage;
	}


	public void apply(AsyncPlayerChatEvent ev){
		ev.setFormat(Messenger.color(apply(format,ev)));
	}
}
