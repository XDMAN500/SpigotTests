package me.varmetek.core.scoreboard;

import me.varmetek.core.util.MeshedMap;
import me.varmetek.core.util.Messenger;
import me.varmetek.core.util.SimpleMapEntry;
import me.varmetek.munchymc.Main;
import me.varmetek.munchymc.util.Utils;
import org.apache.commons.lang.Validate;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Created by XDMAN500 on 1/17/2017.
 */
public class Sidebar_OLD implements SimpleMapEntry
{

	protected final SidebarHandler handle;
	protected MeshedMap<String,String,Integer> data;
	//protected Map<String,Score> contents;
	protected  volatile   Objective  display;
	protected   volatile Objective  buffer;
	protected final String id;
	protected final Random random = new Random();
	int amount = 0;
	Set<String>  used = new HashSet<>();

	protected Sidebar_OLD (SidebarHandler board, String id){
		this.id = id;
		this.handle = board;

		data = new MeshedMap<>();
		//display.getScoreboard().
		display = handle.getHandle().getScoreboard().registerNewObjective(createName(id), "dummy");
		buffer = handle.getHandle().getScoreboard().registerNewObjective(createName(id), "dummy");
		//obj.setDisplaySlot(DisplaySlot.SIDEBAR);

		//contents = Maps.newHashMap();



	}
	private String createName(String id){
		String s = "-sb"+id+":"+ Utils.alphaCode(random.nextInt(100));
		if(used.contains(s)){
			Main.get().getLogger().warning( "DUP :"+ s);

		}
		used.add(s);
		return s;
	}
	public boolean isVisible(){
		return handle.isDisplayed(id);
	}

	public synchronized void  render(){
		//Objective intermediate = display;
		try{
		display.unregister();
		}catch(Exception e){
			//Main.get().getLogger().warning( "("+display.getName()+") "+e.getMessage());
			return;
		}
		display = buffer;
		//if(intermediate.isModifiable())intermediate.unregister();

		//buffer.unregister();
		buffer = handle.getHandle().getScoreboard().registerNewObjective(createName(id), "dummy");
		buffer.setDisplayName(display.getDisplayName());
		amount = 0;

		for(String key: data.keys()){
			buffer.getScore(data.getV1(key)).setScore(data.getV2(key));
			amount ++;
		}
		if(isVisible()){
			///handle.setDisplay(this);
		}

	}
	public void setVisible(boolean visible){

		if(visible){
		///	handle.setDisplay(this);
			//display.setDisplaySlot(DisplaySlot.SIDEBAR);
			//handle. = Optional.of(this);
		}else{
			//handle.setDisplay(null);
			//handle.scoreboard.clearSlot(DisplaySlot.SIDEBAR);
			//Optional.empty();
		}
	}

	public void setTitle(String s){
		buffer.setDisplayName(Utils.colorCode(s));
	}


	public String getTitle(){
		return buffer.getDisplayName();

	}

	public  void create(String index, String name,  int pos){
		if(has(index)){
			clearScore(data.getV1(index));
		}

		name = Messenger.color( name+ salt());
		Score score = buffer.getScore(name);
		score.setScore(pos);
		data.put(index,name,pos);


	}


	public  void setName(String index ,String newName){
		Validate.isTrue(has(index));
		int score = data.getV2(index);

		create(index,newName,score);

	}

	public  void setPosition(String index ,int pos){
		Validate.isTrue(has(index));

		Score entry =  buffer.getScore(data.getV1(index));
		entry.setScore(pos);

	}

	public  void remove(String index){
		if(has(index)){
			clearScore(data.getV1(index));
			data.remove(index);
		}

	}

	private  String geteName(String index){
		return stripSalt(data.getV1(index));

	}

	public boolean has(String index){
		return data.containsKey(index) && data.getV1(index) != null;
	}

	private String salt(){
		String prefix = Integer.toHexString(random.nextInt(1000));

		StringBuilder builder = new StringBuilder(" &r");
		for(int i = 0; i< prefix.length(); i++)
		{
			builder.append('&');
			builder.append(prefix.charAt(i));
		}

		return Messenger.color(builder.toString());
	}

	private String stripSalt(String s){
		int index = s.lastIndexOf(" ");

		if(index!= -1 && index < s.length()-1 && s.charAt(index+1) == ChatColor.COLOR_CHAR){
			return s.substring(0,index);
		}else{
			return s;
		}
	}
	public String getName(){
		return buffer.getName();
	}
	private void clearScore(String s){
		handle.getHandle().getScoreboard().resetScores(s);
	}
	@Override
	public String ID ()
	{
		return id;
	}

	public MeshedMap<String,String,Integer> map(){
		return data;
	}
	public int size(){
		return amount;
	}
}
