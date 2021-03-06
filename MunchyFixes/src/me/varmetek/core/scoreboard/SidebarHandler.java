package me.varmetek.core.scoreboard;

import me.varmetek.core.util.BasicMap;
import me.varmetek.core.util.Cleanable;
import org.apache.commons.lang.Validate;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;

import java.util.Optional;
import java.util.Stack;

/**
 * Created by XDMAN500 on 1/21/2017.
 */
public class SidebarHandler implements Cleanable
{

	private Scoreboardx scoreboard;
	private BasicMap<Sidebar> map;
	private Objective  display;
	private RenderOrder order = new RenderOrder();
	private int slot = 0;

	 SidebarHandler (Scoreboardx scoreboard){
			Validate.notNull(scoreboard);
			this.scoreboard = scoreboard;
			map = new BasicMap<>();
			display = scoreboard.getScoreboard().registerNewObjective("sidebar","dummy");




	}


	public Scoreboardx getHandle(){
		return scoreboard;
	}

	public Sidebar add(String id){
		if(map.isRegistered(id)){
			throw new IllegalArgumentException("The entry is already full");
		}else{
			Sidebar sb = new Sidebar(this,id);
			map.register(sb);
			return sb;
		}

	}
	public  RenderOrder getRenderOrder(){
		return order;
	}
	public boolean hasDisplay(){
		return !order.isEmpty();
	}

	 boolean isDisplayed(String id){
		return hasDisplay() && map.get(id).isPresent() && order.hasRequested(map.get(id).get());
	}
	public void remove(String id){
		map.unregister(id);
	}

	public boolean has(String id){
		return map.isRegistered(id);
	}

	public Optional<Sidebar> get(String id){
		return map.get(id);
	}

	public void clear(){
	 	map.clean();
	}

	public void render(){

		if(order.getCurrent().isPresent()){
			slot = (slot+1) % 2;
			Objective old = display;

			display = scoreboard.getScoreboard().registerNewObjective("sidebar"+slot,"dummy");
			order.getCurrent().get().render(display);
			display.setDisplaySlot(DisplaySlot.SIDEBAR);
			old.unregister();
		}
	}

	@Override
	public void clean ()
	{
		scoreboard = null;
		map.clean();
		map = null;
		display.unregister();
		display = null;
		 order.clean();
		 order = null;

	}

	public class RenderOrder implements Cleanable
	{
		private Stack<Sidebar> order = new Stack<Sidebar>();

		public void request(Sidebar b){
			if(!hasRequested(b)){
				order.push(b);
			}
		}

		public void leave(Sidebar b){
			order.remove(b);

		}
		public boolean isEmpty(){
			return order.isEmpty();
		}
		public Optional<Sidebar> getCurrent(){
			if(order.isEmpty()) return Optional.empty();

			return Optional.ofNullable(order.peek());
		}

		public  boolean hasRequested(Sidebar b){
			return order.contains(b);
		}


		@Override
		public void clean ()
		{
			order.clear();
			order = null;
		}
	}



}
