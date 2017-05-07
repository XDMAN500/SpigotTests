package me.varmetek.munchymc.listeners;

import me.varmetek.core.commands.CmdCommand;
import me.varmetek.core.placeholder.FormatHandleChat;
import me.varmetek.core.placeholder.FormatHandlePlayer;
import me.varmetek.core.service.Element;
import me.varmetek.core.util.Messenger;
import me.varmetek.munchymc.Main;
import me.varmetek.munchymc.backend.User;
import me.varmetek.munchymc.util.Container;
import me.varmetek.munchymc.util.Utils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.*;
import net.minecraft.server.v1_11_R1.NBTTagCompound;
import org.bukkit.craftbukkit.v1_11_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by XDMAN500 on 1/2/2017.
 */
public class ChatListener implements Element
{

	private static final String  permPrefix = "ench.look.bs." ;

	private final Main plugin;
	private final FormatHandlePlayer formatHandle = new FormatHandlePlayer();
	private final FormatHandleChat chatFormatHandle = new FormatHandleChat("&8<&b"+Utils.placeholder("playerName")+"&8> &7"+Utils.placeholder("playerMessage"), Utils.placeholder("playerMessage"));
	private final Listener listener;

	public ChatListener(Main plugin){
		this.plugin = plugin;

		formatHandle.register("%playerName%", (p)->{return p.getName();});
		chatFormatHandle.register(Utils.placeholder("playerName"),(ev)->{return ev.getPlayer().getName();});

		listener =  new Listener()
		{
			@EventHandler (priority =  EventPriority.LOW)
			public void chat(AsyncPlayerChatEvent ev)
			{
				User user = plugin.getUserHandler().getUser(ev.getPlayer());
				//plugin.getUserHandler().getUser(ev.getPlayer());

				//Player pl = ev.getPlayer();
				//plugin.getChatPlaceholderMap().format(ev);
				chatFormatHandle.apply(ev);
				Messenger.send(ev.getPlayer(),
					"Hey :"+ ev.getPlayer().isInvulnerable(),
					"Exist?:" + user.getPlayer().isPresent());


	/*	if(!ev.getMessage().startsWith(permPrefix))return;
		Messenger.send(pl, "Inside the event");
		String[] parts =  ev.getMessage().split("\\.");
		if(parts.length < 4)return;*/
	/*	if(pl.getItemInHand() != null){
			//Messenger.send(pl, "Item :" + Integer.toHexString(pl.getItemInHand().hashCode()));
			Messenger.send(pl, "Item :" + UtilInventory.getAlphaCode(pl.getInventory().getItemInMainHand()));
			Messenger.send(pl, "HexName :" + Utils.getHexNameHidden(pl.getUniqueId()) +Integer.toHexString(pl.getUniqueId().hashCode()));
			pl.getItemInHand().setType(Material.AIR);
		}
		String s = getPart(permPrefix, ev.getMessage());
		//StringBuilder sb = new StringBuilder();
		//for(String s: parts)sb.append(s);
		if(s != null)
		{
			Messenger.send(pl, "Third Part :" + s);
		}*/

			}
		};
	}




	private String getPart(String prefix, String message)
	{
		int loc = prefix.split("\\.").length;
		if(!message.startsWith(permPrefix))return null;
		String[] parts =  message.split("\\.");
		if(parts.length <= loc)return null;
		return parts[loc];
	}
	/*

	eee


	 */
	private final Map<String,BaseComponent[]> formatCodes = new HashMap<>();


	private ComponentBuilder insertComponents(ComponentBuilder cb, BaseComponent[] comps)
	{
		Arrays.asList(comps).forEach(comp ->{
			if(!(comp instanceof TextComponent))return;
			TextComponent textComp = (TextComponent)comp;
			cb.append(textComp.getText());
			//if(textComp.h)



		});

		return null;
	}
	private BaseComponent addComponents(BaseComponent core ,BaseComponent[] comps)
	{
		Container<BaseComponent> cc = Container.of(core);

		Arrays.asList(comps).forEach( comp -> addComponent(cc.get(), comp));

		return cc.get();
	}
	private BaseComponent addComponent(BaseComponent core ,BaseComponent comps)
	{
		Container<BaseComponent> cc = Container.of(core);
		String s;

		cc.get().addExtra(comps);
		cc.set(comps);
		return cc.get();
	}
	/***
	 *
	 * e is madeout of 3 parts
	 * [0] is the root of the branches of extra componets
	 * [1] is the parent of the component to replace
	 * [2] is the component we are replacing
	 *
	 *
	 *
	 */

	private BaseComponent replaceComponent(BaseComponent[] e, String stuff, BaseComponent[] replacement){
		if(e == null)return null;
		//	Messenger.sendAll("check1");
		if(replacement == null)return e[0];
		//Messenger.sendAll("check2");
		if(replacement.length == 0)return e[0];
		//Messenger.sendAll("check3");
		//String them together
		if(replacement.length > 1)
		{
			Arrays.asList(replacement).forEach((comp) ->
			{
				if (comp != replacement[0])
				{
					Messenger.sendAll("Condencing");
					replacement[0].addExtra(comp);
				}
			});
		}
		List<BaseComponent> extras = e[2] == null ? null :e[2].getExtra();// Get the replacee's extras
		if(e[1] != null && e[1].getExtra() != null){
			Messenger.sendAll("Adding Extras");// Make sure the parent has extras
			e[1].getExtra().remove(e[2]);
			e[1].addExtra(replacement[0]);// Make replace the replacee's position in the heirarchy with the replacement
			if(!(extras == null ||  extras.isEmpty())){

				extras.forEach(comp -> replacement[0].addExtra(comp));// add in the replacee's extras to the replacement
			}

		}
		Messenger.sendAll("This is the end:" +e[0].toLegacyText());
		return e[0];
	}

	private BaseComponent[] getComponent(BaseComponent e, String stuff){
		//Messenger.sendAll("check1");
		if(e == null)return null;
		//String str = e.toPlainText();
		//Messenger.sendAll("check2");
		//if(!str.contains(stuff))return null;
		Messenger.sendAll("it is somewhere");

		AtomicReference<BaseComponent> current = new AtomicReference<>(e);
		AtomicReference<BaseComponent> previous = new AtomicReference<>(e);
		if(e instanceof TextComponent && ((TextComponent)e).getText().contains(stuff)){
			current.set(((TextComponent)e));
			Messenger.sendAll("it is root");
		}else{
			if(e.getExtra() == null ||e.getExtra().isEmpty()){
				current.set(e);
			}else{
				Messenger.sendAll("checking subs");
				e.getExtra().forEach( (comp)-> {

					BaseComponent[] tc = getComponent(comp,stuff);
					if(Objects.nonNull(tc[2]))
					{
						current.set(tc[2]);
						Messenger.sendAll("its in subs");
						return;
					}else{
						previous.set(tc[2]);
					}
				});
			}
		}
		return new BaseComponent[]{e,previous.get(),current.get()};
	}
	private List<BaseComponent> forceInheir(BaseComponent parent){
		List<BaseComponent> current = new ArrayList<>();
		if(parent == null)return current;
		current.add(parent);
		if(parent.getExtra() == null || parent.getExtra().isEmpty())return current;
		List<BaseComponent> children = parent.getExtra();
		children.forEach(o->{

			o.setColor(o.getColor() );
			o.setClickEvent(o.getClickEvent());
			o.setHoverEvent(o.getHoverEvent());
			o.setBold(o.isBold());
			o.setItalic(o.isItalic());
			o.setObfuscated(o.isObfuscated());
			o.setStrikethrough(o.isStrikethrough());
			parent.getExtra().remove(o);
			current.add(o);

		});
		return current;
	}
	//private  List<BaseComponent>

//	@EventHandler
	public void add(AsyncPlayerChatEvent ev)
	{


		formatCodes.put("player", new ComponentBuilder(ev.getPlayer().getName()).color(ChatColor.GREEN)
				                          .event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/tell " + ev.getPlayer().getName() +" ")).create());
		formatCodes.put("message", TextComponent.fromLegacyText(Utils.colorCode("&7"+ev.getMessage())));
		//editMessage(ev);
	}
	private void editMessage(AsyncPlayerChatEvent ev)
	{

		Player pl = ev.getPlayer();
		if(pl.getItemInHand() != null)
		{

			String s = CraftItemStack.asNMSCopy(pl.getItemInHand()).save(new NBTTagCompound()).toString();
			String name = pl.getItemInHand().getItemMeta().hasDisplayName()? pl.getItemInHand().getItemMeta().getDisplayName()
					              : pl.getItemInHand().getType().name();
			ComponentBuilder cb = new ComponentBuilder("[").append(name).event(
					new HoverEvent(HoverEvent.Action.SHOW_ITEM, new ComponentBuilder( s).create())
			).append("]");
			////TextComponent tc = new TextComponent("[");
			//	tc.setColor(ChatColor.DARK_GREEN.);

			//tc.setHoverEvent();
			//pl.spigot().sendMessage(cb.create());
			////for(int i = 0 ; i<formatCodes.get("message").length; i++ )
			//{

			//}
			Arrays.asList(formatCodes.get("message")).forEach(o ->{
				Messenger.send(pl,"Working on parting the red sea:" + o.toLegacyText());
				//o =	replaceComponent(getComponent(o, "[item]"),"[item]",cb.create());
			});
		}


	}

	private static final String format = Messenger.color("%player%&7> %message%");
	//@EventHandler(priority = EventPriority.HIGH)
	public void chatFormater(AsyncPlayerChatEvent ev)
	{
		ev.setFormat("");
		ev.setCancelled(true);
		Player pl = ev.getPlayer();
		List<BaseComponent > components = new ArrayList<>();
		BaseComponent current = new TextComponent();
		boolean isRegularText = true;
		StringBuilder sb = new StringBuilder();

		/*if(pl.getItemInHand() != null)
		{
			String s = CraftItemStack.asNMSCopy(pl.getItemInHand()).save(new NBTTagCompound()).toString();
			String name = pl.getItemInHand().getItemMeta().hasDisplayName()? pl.getItemInHand().getItemMeta().getDisplayName()
					              : pl.getItemInHand().getType().name();
			ComponentBuilder cb = new ComponentBuilder("[").append(name).event(
					new HoverEvent(HoverEvent.Action.SHOW_ITEM, new ComponentBuilder( s).create())
			).append("]");
			////TextComponent tc = new TextComponent("[");
			//	tc.setColor(ChatColor.DARK_GREEN.);

			//tc.setHoverEvent();
			pl.spigot().sendMessage(cb.create());
		}*/
		for(int i = 0 ; i< format.length(); i++)
		{

			char ch =  format.charAt(i);

			if(ch == '%')
			{
				if (isRegularText)
				{

					//	new TextComponent());

					//components.addAll( Arrays.asList(TextComponent.fromLegacyText(sb.toString())));

					addComponents(current,new ComponentBuilder(sb.toString()).create() );





				} else
				{

					BaseComponent[] input = formatCodes.get(sb.toString());
					//	Messenger.send(pl," || "+ sb.toString());
					if(input != null){
						addComponents(current,input);
						//components.addAll(Arrays.asList(input));


					}

				}
				isRegularText = !isRegularText;
				sb.delete(0, sb.length());
				//sb.append(ch);
			}else{
				sb.append(ch);
			}
			//sb.append(ch);



		}
		ev.getRecipients().forEach(player ->{

			//player.spigot().sendMessage(components.toArray( new BaseComponent[0]));
			player.spigot().sendMessage(current);
		});
	}

	@Override
	public void clean (){

	}

	@Override
	public CmdCommand[] supplyCmd (){
		return null;
	}

	@Override
	public Listener supplyListener (){
		return listener;
	}
}
