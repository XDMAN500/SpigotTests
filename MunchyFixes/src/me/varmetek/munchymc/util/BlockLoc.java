package me.varmetek.munchymc.util;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.BlockVector;
import org.bukkit.util.Vector;

/**
 * Created by XDMAN500 on 1/3/2017.
 */
public class BlockLoc
{
	final World world;
	final int x , y, z ;

	public BlockLoc(World world, BlockVector vector){
		this(world,vector.getBlockX(),vector.getBlockY(),vector.getBlockZ());
	}
	public BlockLoc(World world, int x, int y, int z){
		this.world = world;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public BlockLoc( int x, int y, int z){
		this.world = null;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public BlockLoc( com.sk89q.worldedit.Vector vector){
		this(null,vector.getBlockX(),vector.getBlockY(),vector.getBlockZ());
	}

	public BlockLoc(Vector vector){
		this(null,vector.getBlockX(),vector.getBlockY(),vector.getBlockZ());
	}

	public BlockLoc(Location loc){
			this(loc.getWorld(),loc.toVector().toBlockVector());
	}

	public World getWorld(){
		return world;
	}

	public Location getLocation(){
		return new Location(world,x,y,z);
	}

	public int getX(){
		return x;
	}


	public int getY(){
		return y;
	}

	public int getZ(){
		return z;
	}

	@Override
	public int hashCode() {
		return (x >> 13) ^ (y >> 7) ^ z;
	}


	@Override
	public String toString(){
		return  "BlockLoc{" + "world=\"" + world + "\" (" +x+", "+y+", "+z+")}";
	}
}
