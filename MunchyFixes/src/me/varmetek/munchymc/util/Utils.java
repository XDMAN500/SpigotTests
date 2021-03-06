package me.varmetek.munchymc.util;

import me.varmetek.core.util.Messenger;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;

import java.text.DecimalFormat;
import java.util.*;
import java.util.function.Function;

public final class Utils {
	private Utils(){}
	public static String colorCode(String s){
		return Messenger.color(s);//s.replaceAll("&", "§");

	}
	public static final String SYSCMD = "($)";
	public static final DecimalFormat decimalF = new DecimalFormat("#.##");
	public static String getHexName(UUID id){
		return Integer.toHexString(id.hashCode());
	}

	public static String getHexNameHidden(String s){
		StringBuilder builder = new StringBuilder();
		for(int i = 0; i< s.length(); i++)
		{
			builder.append('§');
			builder.append(s.charAt(i));
		}
		return builder.toString();
	}
	public static String getHexNameHidden(UUID s){
		return getHexNameHidden(getHexName(s));
	}
	public String getHexName(String s){
		return s.replace("§","");
	}


	public static final ConsoleCommandSender CONSOLE = Bukkit.getConsoleSender();
	public static List<String> matchString(Collection<? extends String> list, String input){

		List<String> output = new ArrayList<String>();
		if(list == null || input == null)return output;
		if(list.isEmpty())return output;
		for(String avalue:list){
			//debug("in Loop");
			if(avalue != null){
				String sub;
				if(avalue.length() > input.length()){sub = avalue.substring(0, input.length());}
				else  {sub = avalue;}
			
		
				if(sub.equalsIgnoreCase(input.toLowerCase())){
					output.add(avalue);
				}
			}
		}
		return output;
	}
	public static OfflinePlayer getPlayer(UUID u){
		if(Bukkit.getPlayer(u) == null){
			for(OfflinePlayer pl : Bukkit.getOfflinePlayers()){
				if(pl.getUniqueId().equals(u)){
					return pl;
				}
			}
			return null;
		}else{
			return Bukkit.getPlayer(u);
		}
	}
	public static List<String> matchOfflinePlayers(String input){
		List<String> tabs = new ArrayList<String>();
		if(input == null)return tabs;
		for(OfflinePlayer p : Bukkit.getOfflinePlayers()){
			tabs.add(p.getName());
		}
		return matchString(tabs, input.toLowerCase());
	}
	public static Set<Player> getOnlinePlayers(){
		Set<Player>  list = new HashSet<Player>();
		for(World w: Bukkit.getWorlds()){
			for(Player p: w.getPlayers()){
				list.add(p);
			}
		
		}
			
		return list;
	}
	public static List<String> matchOnlinePlayers(String input){
		List<String> tabs = new ArrayList<String>();
		if(input == null)return tabs;
			for(Player p : getOnlinePlayers()){
			tabs.add(p.getName());
		}
		return matchString(tabs, input.toLowerCase());
	}
	public static boolean isRightClicked(Action action){
		return action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK;
	}
	public static boolean isLeftClicked(Action action){
		return action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK;
	}

	public static int  getInt(String s){
		return (Integer)Try.of(()->{ return new Integer(s);}).getOrElse(null);
	}
	public static <T> List<String> toStringList(Collection<T> stuff , Function<T,String>  func){
		List<String> things = new ArrayList<>(stuff.size());
		for(T object : stuff){
			things.add(func.apply(object));
		}
		return things;
	}
	public static String placeholder(String id){ return "%"+id+"%";}
	public static String alphaCode(int num){
		return  Integer.toString(num,Character.MAX_RADIX);
	}
}
