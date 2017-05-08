package me.varmetek.core.scoreboard;

import me.varmetek.core.util.SimpleMapEntry;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * Created by XDMAN500 on 2/21/2017.
 */
public class NameTag implements SimpleMapEntry
{

	protected final String id;
	protected final NameTagHandler  handle;
	protected final Set<UUID> players = new HashSet<>();
	private String prefix = "", suffix = "";
	private boolean seeInvis = false;
	private boolean enabled = true;

	NameTag(String id, NameTagHandler  handle){
		this.id = id;
		this.handle = handle;
	}

	public void setEnabled(boolean on){
		enabled = on;
	}

	public boolean isEnabled(){
		return enabled;
	}
	@Override
	public String ID ()
	{
		return id;
	}

	public String getPrefix(){
		return prefix;
	}

	public String getSuffix(){
		return suffix;
	}

	public boolean canSeeInvis(){
		return seeInvis;
	}

	public NameTag setPrefix(String prefix){
		this.prefix = prefix;
		return this;
	}

	public NameTag setSuffix(String suffix){
		this.suffix = suffix;
		return this;
	}

	public NameTag seeInvis(boolean on){
		this.seeInvis = on;
		return this;
	}

	public void addPlayer(Player pl){
		players.add(pl.getUniqueId());
	}
	public void addPlayers(Player... pl){
		addPlayers(Arrays.asList(pl));
	}
	public void addPlayers(Collection<Player> pl){
		pl.forEach((p)->{addPlayer(p);});
	}

	public void remove(UUID id){
		players.remove(id);
	}

	public Set<UUID> getPlayers(){
		return new HashSet<>(players);
	}






}
