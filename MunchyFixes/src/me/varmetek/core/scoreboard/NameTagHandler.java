package me.varmetek.core.scoreboard;

import me.varmetek.core.util.BasicMap;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.scoreboard.Team;

import java.util.Optional;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * Created by XDMAN500 on 2/21/2017.
 */
public class NameTagHandler
{
	private final Scoreboardx scoreboard;
	private final BasicMap<NameTag> map = new BasicMap<>();
	private RenderOrder order = new RenderOrder();
	private int slot = 0;

	NameTagHandler (Scoreboardx scoreboard){
		Validate.notNull(scoreboard);
		this.scoreboard = scoreboard;


	}

	public NameTag add(String id){
		if(map.isRegistered(id)){
			throw new IllegalArgumentException("The entry is already full");
		}else{
			NameTag sb = new NameTag(id,this);
			map.register(sb);
			return sb;
		}

	}
	public void remove(String id){
		map.unregister(id);
	}

	public boolean has(String id){
		return map.isRegistered(id);
	}

	public Optional<NameTag> get(String id){
		return map.get(id);
	}

	public void clear(){
		map.clean();
	}

	public void render(){
		if(order.isEmpty())return;
		scoreboard.getScoreboard().getTeams().forEach(Team::unregister);

		order.order.forEach((tag) -> {
			Team t = scoreboard.getScoreboard().getTeam(tag.ID());
			t.setSuffix(tag.getSuffix());
			t.setPrefix(tag.getPrefix());
			t.setCanSeeFriendlyInvisibles(tag.canSeeInvis());
			tag.getPlayers().forEach((p)-> t.addEntry(Bukkit.getPlayer(p).getName()));


		});
	}
	public class RenderOrder
	{
		private Queue<NameTag> order = new PriorityQueue<>();

		public void request(NameTag b){
			if(!hasRequested(b)){
				order.add(b);
			}
		}

		public void leave(NameTag b){
			order.remove(b);

		}
		public boolean isEmpty(){
			return order.isEmpty();
		}
		public Optional<NameTag> getCurrent(){
			if(order.isEmpty()) return Optional.empty();

			return Optional.ofNullable(order.peek());
		}

		public  boolean hasRequested(NameTag b){
			return order.contains(b);
		}





	}

}
