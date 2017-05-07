package me.varmetek.munchymc.util;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.BlockVector;

/**
 * Created by XDMAN500 on 1/3/2017.
 */
public class BlockLoc implements Cloneable
{
	final World world;
	final BlockVector vect;

	public BlockLoc(World world, BlockVector vector){
		this.world = world;
		this.vect = vector;
	}
	public BlockLoc(World world, int x, int y, int z){
		this(world,new BlockVector(x,y,z));
	}
	public BlockLoc(Location loc){
			this(loc.getWorld(),loc.toVector().toBlockVector());
	}

	public World getWorld(){
		return world;
	}
	public BlockVector getVector(){
		return vect.clone();
	}

	public Location getLocation(){
		return new Location(world,vect.getBlockX(),vect.getY(),vect.getZ());
	}
	@Override
	public int hashCode(){
		return world.getUID().hashCode()+ vect.hashCode();
	}
	@Override
	public BlockLoc clone(){

		try {
			return (BlockLoc) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new Error(e);
		}
	}

	@Override
	public String toString(){
		return  "BlockLoc{" + "world=" + world + vect.toString()+"} "+ Integer.toString(hashCode(), Character.MAX_RADIX);
	}
}
