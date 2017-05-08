package me.varmetek.munchymc.backend;

import me.varmetek.core.util.BasicMap;
import me.varmetek.core.util.Messenger;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by XDMAN500 on 1/29/2017.
 */
public class ChatPlaceholderMap
{
	private final String format;
	private final ChatPlaceholder msgFormater  = new ChatPlaceholder("playerMessage", (ev) -> {

		formatMessage(ev);
		return "%2$s";
	});

	private  BasicMap<ChatPlaceholder>  forMessage;
	private  BasicMap<ChatPlaceholder>  forFormating;



	public ChatPlaceholderMap(String format){
		this.format = format;
		forMessage = new BasicMap<>();
		forFormating = new BasicMap<>();

		forFormating.register(new ChatPlaceholder("playerName", (ev) ->{ return ev.getPlayer().getName();}  ));
		forFormating.register(new ChatPlaceholder("playerDisplayName", (ev) ->{ return ev.getPlayer().getDisplayName();}  ));

	}

	public BasicMap<ChatPlaceholder>  forMessage(){
		return forMessage;
	}
	public BasicMap<ChatPlaceholder>  forFormatting(){
		return forFormating;
	}

	public String getFormat(){
		return format;
	}

	private void formatMessage(AsyncPlayerChatEvent ev){
		AtomicReference<String> str = new AtomicReference<>(ev.getMessage());
		if(!forMessage.values().isEmpty())
		{
			forMessage.values().forEach((a) -> str.set(a.apply(ev, str.get())));
		}

		ev.setMessage( Messenger.color(str.get()));

	}
	public void format(AsyncPlayerChatEvent ev){
		AtomicReference<String> str = new AtomicReference<>(format);

		forFormating.values().forEach( (a)->  str.set(a.apply(ev,str.get())) );

		str.set(msgFormater.apply(ev, str.get()));
		ev.setFormat( Messenger.color(str.get()) );
	}
}
