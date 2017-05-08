package me.varmetek.core.scoreboard;

import me.varmetek.core.util.MeshedMap;
import me.varmetek.core.util.Messenger;
import me.varmetek.core.util.SimpleMapEntry;
import me.varmetek.munchymc.Main;
import me.varmetek.munchymc.util.Utils;
import org.apache.commons.lang.Validate;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;

import java.util.*;

/**
 * Created by XDMAN500 on 1/17/2017.
 */
public class Sidebar_OLD2 implements SimpleMapEntry
{

	protected final SidebarHandler handle;
	protected MeshedMap<String,String,Integer> data;
	protected MeshedMap<String,String,Integer> displayedData;
	protected String title;
	//protected Map<String,Score> contents;
	protected  final Objective[]  objz = new Objective[2];
	//protected  final Objective  display;
//	protected  Objective  buffer;
	protected final String id;
	protected final Random random = new Random();
	int amount = 0;
	int slot = 0;

	Set<String>  used = new HashSet<>();

	protected Sidebar_OLD2 (SidebarHandler board, String id){
		this.id = id;
		this.handle = board;

		data = new MeshedMap<>();
		displayedData = new MeshedMap<>();
		//display.getScoreboard().
		objz[0] = handle.getHandle().getScoreboard().registerNewObjective(createName(id), "dummy");
		objz[1] = handle.getHandle().getScoreboard().registerNewObjective(createName(id), "dummy");
		//obj.setDisplaySlot(DisplaySlot.SIDEBAR);

		//contents = Maps.newHashMap();



	}
	private String createName(String id){
		String s = "-sb"+id+":"+ Utils.alphaCode(random.nextInt(1000));
		if(used.contains(s)){
			Main.get().getLogger().warning( "DUP :"+ s);

		}
		used.add(s);
		return s;
	}
	public boolean isVisible(){
		return handle.isDisplayed(id);
	}


	public Objective getDisplay(){
		return objz[slot];
	}


	public Objective getBuffer(){
		return objz[slot];
		//return objz[(slot+1) % 2];
	}


	public  void  render(){
		//Objective old = display;
		//display = buffer;
		//buffer = old;
		//slot = (slot+1) % 2;
		slot = slot;
		if (isVisible())
		{
			//handle.getHandle().getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
			getDisplay().setDisplaySlot(DisplaySlot.SIDEBAR);

		}
		Collection<String> keys = new ArrayList<>(displayedData.keys());
		for(String key : keys){
			if(!data.containsKey(key)){
				clearScore(displayedData.getV1(key));
			}
		}
		displayedData.clear();
		displayedData.putAll(data);
		/*Collection<String> keys = new ArrayList<>(displayedData.keys());
		for(String key : keys){
			clearScore(displayedData.getV1(key));
			displayedData.remove(key);
		}
		 keys = new ArrayList<>(data.keys());
		for(String key : keys){

			create(key, stripSalt(data.getV1(key)), data.getV2(key));
			displayedData.put(key,  data.getV1(key), data.getV2(key));
		}
		setTitle(title);*/

		//if(getDisplay().getName().equals(getBuffer().getName()))
		//Main.get().getLogger().warning(data.size() +" vs "+ displayedData.size());
	/*	Objective old = display;


		//if(intermediate.isModifiable())intermediate.unregister();

		//buffer.unregister();
		Objective buffer = handle.getHandle().getScoreboard().registerNewObjective(createName(id), "dummy");
		buffer.setDisplayName(title);

		amount = 0;

		for(String key: data.keys()){
			buffer.getScore(data.getV1(key)).setScore(data.getV2(key));
			amount ++;
		}
		display = buffer;


		used.remove(old.getName());
		Main.get().getTaskHandler().run( ()->
		{
			if (isVisible())
			{
				handle.setDisplay(this);
			}
			Main.get().getTaskHandler().run( ()->
			{
				old.unregister();
			});
		});*/
	}
	public void setVisible(boolean visible){

		if(visible){
			//handle.setDisplay(this);
			//display.setDisplaySlot(DisplaySlot.SIDEBAR);
			//handle. = Optional.of(this);
		}else{
			//handle.setDisplay(null);
			//handle.scoreboard.clearSlot(DisplaySlot.SIDEBAR);
			//Optional.empty();
		}
	}

	public void setTitle(String s){
		title = Utils.colorCode(s);
		getBuffer().setDisplayName(title);
	}


	public String getTitle(){
		return title;
		//return buffer.getDisplayName();

	}

	public  void create(String index, String name,  int pos){


		if(has(index)){
			if(!stripSalt(data.getV1(index)).equals(Messenger.color(name)))
			{
				clearScore(data.getV1(index));

				Main.get().getLogger().info(
						"Appending " + "\""+name+"\"{"+name.length() +"} | \""+stripSalt(data.getV1(index))+"\"" +"{"+stripSalt(data.getV1(index)).length() +"}");
			}else{
				//if(getBuffer().getScore(data.getV1(index)).isScoreSet())
					return;
			}
		}
		String newname = Messenger.color( name+ salt());
		//	Score score = buffer.getScore(name);
		//score.setScore(pos);
		data.put(index,newname,pos);
		getBuffer().getScore(newname).setScore(pos);




	}


	public  void setName(String index ,String newName){
		Validate.isTrue(has(index));
		int score = data.getV2(index);

		create(index,newName,score);

	}

	public  void setPosition(String index ,int pos){
		Validate.isTrue(has(index));
		data.put(index,data.getV1(index),pos);
		getBuffer().getScore(data.getV1(index)).setScore(pos);
	//	Score entry =  buffer.getScore(data.getV1(index));
		//entry.setScore(pos);

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

		StringBuilder builder = new StringBuilder(Messenger.color(" &r"));
		for(int i = 0; i< prefix.length(); i++)
		{
			builder.append('&');
			builder.append(prefix.charAt(i));
		}

		return Messenger.color(builder.toString());
	}

	private String stripSalt(String s){
		int index = s.lastIndexOf(Messenger.color(" &r"));
	//	Main.get().getLogger().info("Index "+ index + " {"+s.length()+"]\""+s+"\"");
		Validate.isTrue(index!= -1 && index < s.length(), "No salt to strip");

			return s.substring(0,index);

	}
	public String getName(){
		return getDisplay().getName();
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
