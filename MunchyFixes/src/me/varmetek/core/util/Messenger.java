package me.varmetek.core.util;

import me.varmetek.munchymc.util.Utils;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.*;

public final class Messenger {

	public static final Presets  PRESETS = new Presets();
	public static class Presets {
		private Map<String,String> cache = new HashMap<>();
		public  void add(String id, String message){
			cache.put(id,message);
		}
		public  void remove(String id){
			cache.remove(id);
		}
		public  String get(String id){
			return cache.get(id);
		}
	}

	public static final String COLOR = Character.valueOf(ChatColor.COLOR_CHAR).toString();
	public static final CommandSender CONSOLE = Bukkit.getConsoleSender();

//	private static final List<Character> chars = Arrays.asList(
	//		new Character[]{'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f','l','m','n','o','k','r'});


	/***
	 *
	 *
	 * Translates the color codes to something recognizable by minecraft
	 *
	 */

	public static String color(String s){
	  Validate.notNull(s);
		return ChatColor.translateAlternateColorCodes('&',s);

	}


	/*****
	 *
	 * Completely removes all codes the text
	 *
	 */

	public static String stripColor(String s){

    Validate.notNull(s);
    return ChatColor.stripColor(s);
	}

	/****
	 *  Reverse translates the translated color.
	 *
	 */

	public static String uncolor(String s){
    Validate.notNull(s);
		char[] b = s.toCharArray();
		for (int i = 0; i < b.length - 1; i++) {
			if (b[i] == ChatColor.COLOR_CHAR && "0123456789AaBbCcDdEeFfKkLlMmNnOoRr".indexOf(b[i+1]) > -1) {
				b[i] = '&';
				b[i+1] = Character.toLowerCase(b[i+1]);
			}
		}
		return new String(b);
	}

  private static void sendSingle( CommandSender sender, String message){

    sender.sendMessage( color(message));

  }

	public static void send( CommandSender sender, String message, String...extra){

		Validate.notNull(sender);
		Validate.notNull(message);
		sendSingle(sender,message);
    if(extra != null)
    {
      for (String sub : extra)
      {
        if(sub == null)continue;
        sendSingle(sender, sub);
      }
    }

	}



	public static void send(CommandSender[] senderList,String message ,String... extra )
  {
    Validate.notNull(senderList);
    Validate.notNull(message);
    Validate.noNullElements(senderList);
    if(extra == null)
    {
      for (CommandSender sender : senderList)
      {
        send(sender, message);
      }
    }else{
      String text = message;
      Iterator<String> it =  Arrays.asList(extra).iterator();
      do{
        if(text == null)
        {
          if ((text = it.next()) == null)  continue;
        }

        for (CommandSender sender : senderList)
        {

          send(sender, text);
        }
      }while(it.hasNext());
    }



  }


  public static void send(Collection<? extends CommandSender>  senderList,String message,String... extra  ){
    Validate.notNull(senderList);
    Validate.notNull(message);
    Validate.noNullElements(senderList);

    if(extra == null)
    {
      for (CommandSender sender : senderList)
      {
        send(sender, message);
      }
    }else{
      String text = message;
      Iterator<String> it =  Arrays.asList(extra).iterator();
      do{
        if(text == null)
        {
          if ((text = it.next()) == null)  continue;
        }

        for (CommandSender sender : senderList)
        {

          send(sender, text);
        }
      }while(it.hasNext());
    }
  }









	public static void sendConsole(String message){send(CONSOLE,message);}



	public static void sendAll(String message){
		send(Utils.getOnlinePlayers(),message);
	}

	public static void debug(String message){
		sendAll( message);
		sendConsole(message);
	}

}


	
	