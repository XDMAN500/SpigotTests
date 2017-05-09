package me.varmetek.munchymc.backend;

import org.apache.commons.lang3.Validate;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.player.PlayerTeleportEvent;

/**
 * Created by XDMAN500 on 1/14/2017.
 */
public class Point {


	protected final Location location;
	protected Point(Location loc){
		Validate.isTrue(loc!= null);
		location = loc;
	}
  public void teleport(LivingEntity entity){
	  entity.teleport(location, PlayerTeleportEvent.TeleportCause.PLUGIN);
  }
	public Location getLocation(){return location;}

	public static class Builder{
	  private Location location;

	  public Builder(Location loc){
	    location = loc;
    }

    public Builder setLocation(Location loc){
      Validate.notNull(location, "Location of a point cannot be null");
	    location = loc;
	    return this;
    }
    public Point build(){
      return new Point(location);
    }


  }


}