package me.varmetek.core.scoreboard;

import com.google.common.collect.Maps;
import me.varmetek.core.util.Messenger;
import me.varmetek.core.util.SimpleMapEntry;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.bukkit.scoreboard.Objective;

import java.util.*;

/**
 * Created by XDMAN500 on 1/17/2017.
 */
public class Sidebar implements SimpleMapEntry
{

	protected final SidebarHandler handle;

	protected Map<String,String> entries;


	protected String title;
	protected final String id;
	protected final Random random = new Random();
	protected final RenderOrder renderOrder = new RenderOrder();

	protected Sidebar(SidebarHandler board, String id){
		this.id = id;
		this.handle = board;


		entries = Maps.newHashMap();

	}

	public RenderOrder getRenderOrder(){
		return renderOrder;
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




	public  void set(String key, String text){
		Validate.notNull(key);
		Validate.isTrue(StringUtils.isNotBlank(key));

		if(text == null){
			remove(key);
		}else{
			entries.put(key,text);
		}

	}








	public  void remove(String key){

		entries.remove(key);

	}

	public   String getText(String key){
		return entries.get(key);

	}


	protected void render(Objective obj){
		obj.setDisplayName(title);
		Iterator<String> stuff = renderOrder.iterator();
		int pos = renderOrder.size();
		while(stuff.hasNext()){
			String key = stuff.next();
			obj.getScore( prepareName( entries.get(key))).setScore(pos);
			pos--;
		}



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

	public class RenderOrder {

		protected List<String> renderOrder = new ArrayList(16);
		protected List<String> previous = new ArrayList(16);

		public void append(String key, String... keys){
			if(entries.containsKey(key))renderOrder.add(key);
			if(keys == null || keys.length == 0)return;

				for(int i = 0; i< keys.length; i++) {
					if (entries.containsKey(keys[i])) renderOrder.add(keys[i]);
				}

		}

		public int size(){
			return renderOrder.size();
		}
		public boolean isEmpty(){
			return renderOrder.isEmpty();
		}
		public void reset(){
			previous.clear();
			previous.addAll(renderOrder);
			renderOrder.clear();
		}
		public Iterator<String> iterator(){
			return renderOrder.iterator();
		}

		public List<String> getOrder(){
			return renderOrder;
		}
		public List<String> getPreviousOrder(){
			return renderOrder;
		}
	}


}
