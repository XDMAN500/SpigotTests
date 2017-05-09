package me.varmetek.core.scoreboard;

import com.google.common.collect.Maps;
import me.varmetek.core.util.Messenger;
import me.varmetek.core.util.SimpleMapEntry;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.bukkit.scoreboard.Objective;

import java.util.Map;
import java.util.Random;

/**
 * Created by XDMAN500 on 1/17/2017.
 */
public class Sidebar implements SimpleMapEntry
{

	protected final SidebarHandler handle;

	protected Map<String,Entry> entries;

	protected String title;
	protected final String id;
	protected final Random random = new Random();


	protected Sidebar(SidebarHandler board, String id){
		this.id = id;
		this.handle = board;


		entries = Maps.newHashMap();



	}


	protected String prepareName(String name){
		return Messenger.color( name+ salt());
	}

	public boolean isVisible(){
		return handle.isDisplayed(id);
	}




	public SidebarHandler getHandle(){
		return handle;
	}
	public void setVisible(boolean visible){

		if(visible){
			handle.getRenderOrder().request(this);
		}else{
			handle.getRenderOrder().leave(this);

		}
	}


	public String getTitle(){
		return title;


	}
	public void setTitle(String s){
		title = Messenger.color(s);

	}




	public  void set(String index, String name,  int pos){


		String newname = prepareName(name);
		entries.put(index, this.new Entry(newname,pos ));
		;
	}


	public  void setName(String index ,String newName){
		Validate.isTrue(has(index));

		entries.put(index, entries.get(index).withName(prepareName(newName)));



	}

	public  void setPosition(String index ,int pos){
		Validate.isTrue(has(index));
		entries.put(index, entries.get(index).withPosition(pos));



	}



	public  void remove(String index){

		entries.remove(index);

	}

	public   String getName(String index){
		return stripSalt(entries.get(index).getName());

	}
	public  int getPos(String index){
		return entries.get(index).getPos();

	}

	protected void render(Objective obj){
		obj.setDisplayName(title);
		entries.keySet().forEach( (str) ->{
			Entry e  = entries.get(str);
			obj.getScore(e.getName()).setScore(e.getPos());
		});



	}
	public boolean has(String index){
		return entries.containsKey(index);
	}
	protected static final String salt = Messenger.color("\u00a0&r");


	protected String salt(){
		String prefix = Integer.toHexString(random.nextInt(1000));

		StringBuilder builder = new StringBuilder(salt);
		for(int i = 0; i< prefix.length(); i++)
		{
			builder.append('&');
			builder.append(prefix.charAt(i));
		}

		return Messenger.color(builder.toString());
	}

	protected String stripSalt(String s){
		int index = s.lastIndexOf(salt);

		Validate.isTrue(index!= -1 && index < s.length(), "No salt to strip");
		return s.substring(0,index);

	}


	@Override
	public String ID ()
	{
		return id;
	}


	public int size(){
		return entries.size();
	}

	private class Entry
	{
		private final String name;
		private final int pos;

	 Entry(String name, int pos){
			Validate.isTrue(StringUtils.isNotBlank(name));
			this.name = name;
			this.pos = pos;
		}

		public String getName(){
			return name;
		}

		public int getPos(){
			return pos;
		}


		 private Entry withName(String name){
			return new Entry(name,pos);
		}
		private Entry withPosition(int pos){
			return new Entry(name,pos);
		}
	}
}
