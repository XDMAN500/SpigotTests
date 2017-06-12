package me.varmetek.core.scoreboard;

import me.varmetek.core.util.Messenger;
import me.varmetek.core.util.SimpleMapEntry;
import me.varmetek.munchymc.MunchyMax;
import org.apache.commons.lang.Validate;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.logging.Level;

/**
 * Created by XDMAN500 on 1/17/2017.
 */
public class Sidebar implements SimpleMapEntry
{

	protected final SidebarHandler handle;
	protected final List<Callable<String>> preOrder;
	protected final Callable<String> title;
	protected final int priority;
	protected final String id;



	protected Sidebar (Builder builder,SidebarHandler handle){
		this.id = builder.id;
		this.handle = handle;
		this.preOrder = new ArrayList(builder.order);
		this.title = builder.title;
		priority = builder.priority;



	}

	public static class SidebarRenderException extends Exception{
		public SidebarRenderException() {
			super();
		}

		public SidebarRenderException(String message) {
			super(message);
		}

		public SidebarRenderException(String message, Throwable cause) {
			super(message, cause);
		}


		public SidebarRenderException(Throwable cause) {
			super(cause);
		}

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


	/*public String getTitle(){
		return title;


	}
	public void setTitle(String s){
		title = Messenger.color(s);

	}*/











	public int getPriority(){
		return priority;
	}



	protected String prepareName(String name, int pos){
		return Messenger.color( name+ salt(pos));
	}

	protected void render(SidebarHandler.Bar bar) throws SidebarRenderException{
		try {

			bar.obj.setDisplayName(Messenger.color(title.call()));

		//int size = Math.min(preOrder.size(),bar.lines.length);
		//int slot = 0;

		for(int i = 0;  i <bar.lines.length; i++){
				if(preOrder.size() <= i){
					bar.clearLine(i);
					continue;
				}


				String text = prepareName(preOrder.get(i).call(),i + bar.hashCode());


				/*if(text.equals(bar.lines[i])){
					continue;
				}*/
				//if( (!text.equals(bar.lines[i]) )){
				//	bar.obj.getScoreboard().resetScores(bar.lines[i]);
				//}

				//postOrder[i] = text;
				bar.setLine(preOrder.size()-1-i,text);
				//slot++;
				//obj.getScore(text).setScore(size-slot);



		}
		}catch(Exception e){
			MunchyMax.logger().log(Level.WARNING,"error setting scoreboard",e);
			throw new SidebarRenderException(e);

		}


	}

	protected static final String salt = "\u00a0";


	protected String salt(int pos){
		String prefix = Integer.toHexString(pos);

		StringBuilder builder = new StringBuilder(salt);
		for(int i = 0; i< prefix.length(); i++)
		{
			builder.append(ChatColor.COLOR_CHAR);
			builder.append(prefix.charAt(i));
		}

		return builder.toString();
	}

	protected String stripSalt(String s){
		int index = s.lastIndexOf(salt);

		Validate.isTrue(index!= -1 && index < s.length(), "No salt to strip");
		return s.substring(0,index);

	}

	protected String stripSalt(String s,int pos){
		return s.replace(salt(pos),"");
	}


	@Override
	public String ID ()
	{
		return id;
	}


	public int size(){
		return preOrder.size();
	}

	public static class Builder{
		protected String id;
		protected List<Callable<String>> order = new ArrayList(16);
		protected Callable<String> title = ()->{return "";};
		protected int priority = 0;


		public Builder ( String id){
			this.id = id;

		}

		public Builder setTitle(Callable<String> title){
			Validate.notNull(title);
			this.title = title;
			return this;
		}

		public Builder setPriority(int p){
			priority = p;
			return this;
		}
		public Builder setTitle(String title){
			Validate.notNull(title);
			this.title = ()->{return title;};
			return this;
		}


		public Builder addText(Callable<String> text){
			Validate.notNull(text);
			if(order.size() >=16) throw new IllegalStateException("Sidebar cannot have more than 16 lines of text");
			order.add(text);
			return this;
		}

		public Builder addText(String text){
			Validate.notNull(text);
			if(order.size() >=16) throw new IllegalStateException("Sidebar cannot have more than 16 lines of text");
			order.add(()->{return text;});
			return this;
		}

		public Sidebar build(SidebarHandler handler){
			return new Sidebar(this,handler);

		}

		@Override
		public int hashCode(){
		  return id.hashCode();
    }

    @Override
    public boolean equals(Object e){
      return e.getClass() == this.getClass() && e.hashCode() == this.hashCode();
    }

	}
	/*public class RenderOrder {

		//protected List<Callable<String>> renderOrder = new ArrayList(16);
		protected List<String> previous = new ArrayList(16);


		public void pushOrder(List<Callable<String>> order){
			Validate.notNull(order,"Pushed order cannot be null");

			renderOrder.clear();
			for(Callable<String> s: order){
				if(s == null)continue;
				if(renderOrder.size() >= 16) break;
				renderOrder.add(s);
			}

		}
		public void pushOrder(Callable<String>[] order){
			Validate.notNull(order,"Pushed order cannot be null");

			renderOrder.clear();
			for(Callable<String> s: order){
				if(s == null)continue;
				if(renderOrder.size() >= 16) break;
				renderOrder.add(s);
			}

		}



		public int size(){
			return renderOrder.size();
		}
		public boolean isEmpty(){
			return renderOrder.isEmpty();
		}
	/*	protected void reset(){
			previous.clear();
			previous.addAll(renderOrder);
			renderOrder.clear();
		}*/
		/*public Iterator<Callable<String>> iterator(){
			return renderOrder.iterator();
		}

		public List<Callable<String>> getOrder(){
			return Collections.unmodifiableList(renderOrder);
		}
		public List<String> getPreviousOrder(){
			return Collections.unmodifiableList(previous);
		}
	}*/


}
