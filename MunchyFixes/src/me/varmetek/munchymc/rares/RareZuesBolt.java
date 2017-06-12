package me.varmetek.munchymc.rares;

import me.varmetek.core.util.Messenger;
import me.varmetek.munchymc.MunchyMax;
import me.varmetek.munchymc.backend.RareItemListener;
import me.varmetek.munchymc.backend.test.CustomItemRare;
import org.apache.commons.lang.Validate;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.util.Vector;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.List;


/**
 * Created by XDMAN500 on 12/30/2016.
 */
public class RareZuesBolt extends RareItemListener
{

	//static Map<UUID,Collection<LivingEntity>> nearTargets = nearTargets = new HashMap<>();
	private static final double range = 50.0;
	private static final double baseFov=  1.0;
	private static final double bulletSize = 0.2;
	private static final boolean useRayCollision = true;
	private static final DecimalFormat decimalF = new DecimalFormat("#.###");
	public RareZuesBolt(CustomItemRare rare){

		super(rare);

	}

//	@EventHandler
	public void onRightClick (PlayerInteractEntityEvent ev){
		Player player = ev.getPlayer();
		LivingEntity entity;
		if(ev.getRightClicked() instanceof LivingEntity)
		{
			entity = (LivingEntity)ev.getRightClicked();
		}else{
			return;
		}

		Location start = player.getLocation();
		Vector vec = start.getDirection();
		Location finish0  = entity.getLocation();


		Vector direction0 =  finish0.clone().subtract(start).toVector();

		double distance0 = finish0.distance(start);

		double range0 =  baseFov/distance0;

		double angle0 = vec.angle(direction0);

		boolean inline0 = angle0 <= range0;


		Messenger.send(player," ");
		Messenger.send(player,"&1Zues &7Coords: &r< " + decimalF.format(start.getX())+ ", "
				                      +decimalF.format(start.getY())+", "
				                      +decimalF.format(start.getZ())+" >");
		Messenger.send(player,"&1Zues &7Coords: &r" + decimalF.format(finish0.getX())+ ", "
				                      +decimalF.format(finish0.getY())+", "
				                      +decimalF.format(finish0.getZ()));


		/*Messenger.send(player,"&1Zues &7Eye angle: &r" +angle1);
		Messenger.send(player,"&1Zues &7Foot Angle angle: &r" +angle0);
		Messenger.send(player,"&1Zues &7Eye Distance: &r"+ distance1);
		Messenger.send(player,"&1Zues &7Foot Distance: &r" + distance0 );
		Messenger.send(player,"&1Zues &7Eye Fov: &r"+ range1);
		Messenger.send(player,"&1Zues &7Foot Fov: &r" + range0);
		Messenger.send(player,"&1Zues &7Eye Valid &r" + inline1);
		Messenger.send(player,"&1Zues &7Foot Valid &r" + inline0);*/
	}

	private boolean inSight(Player player, LivingEntity entity)
	{


		Vector finish0  = entity.getLocation().toVector().midpoint(entity.getEyeLocation().toVector());
		Vector start = player.getEyeLocation().toVector();
		Vector vec =  player.getLocation().getDirection();
		double distance0 = finish0.distance(start);
		Vector finish =  start.clone().add(vec.clone().multiply(distance0));

		if(useRayCollision)
		{
		/*	AxisAlignedBB box = ((CraftEntity)entity).getHandle().getBoundingBox().g(bulletSize);
			Vector top = new Vector(box.a,box.b,box.c);
			Vector bottom = new Vector(box.d,box.e,box.f);*/
			//Messenger.send(player,box.toString());

			//Messenger.send(player," ");
			//Messenger.send(player,entity.getName() +"   "+ vectorString(finish));
			//Messenger.send(player, "-> "+ vectorString(top));
			//Messenger.send(player, "-> "+ vectorString(bottom));
			BoundingBox bb =  BoundingBox.fromEntity(entity).grow(bulletSize);
			return finish.isInAABB(bb.getMinPoint(),bb.getMaxPoint());
		}else{
			Vector direction0 = finish0.clone().subtract(start);


			double range0 = baseFov / distance0;

			double angle0 = vec.angle(direction0);

			boolean inline0 = angle0 <= range0;


			if (inline0)
			{

				Messenger.send(player, " ");
				//Messenger.send(player,"&1Zues &7Eye angle: &r" + decimalF.format(angle1) +" / " + decimalF.format(range1));
				Messenger.send(player, "&1Zues &7Foot Angle angle: &r" + decimalF.format(angle0) + " / " + decimalF.format(range0));
				//Messenger.send(player,"&1Zues &7Eye Distance: &r"+ distance1);
				//Messenger.send(player,"&1Zues &7Foot Distance: &r" + distance0 );

				return true;

			} else
			{
				//	Messenger.send(player," ");
				//Messenger.send(player,"&1Zues &7Eye angle: &r" +vec.angle(direction1));
				//Messenger.send(player,"&1Zues &7Foot Angle angle: &r" +vec.angle(direction0));
				//Messenger.send(player,"&1Zues &7Eye Fov: &r"+ baseFov/distance1);
				//	Messenger.send(player,"&1Zues &7Foot Fov: &r" + baseFov/distance0 );
			}
			return false;

		}
	}
	private static String vectorString(Vector vector)
	{
		return "< " + decimalF.format(vector.getX())+ ", "
				       +decimalF.format(vector.getY())+", "
				       +decimalF.format(vector.getZ())+" >";
	}

	@Override
	public boolean check (Player pl)
	{
		return isEquiped(pl, EquipmentSlot.HAND);
	}

	@Override
	public void clean (){

	}

	@Override
	public Listener supplyListener (){
		return new Listener(){
			@EventHandler
			public void onClick(PlayerInteractEvent ev){
				Player pl = (Player)ev.getPlayer();
				if(!check(pl))return;
				if( (ev.getAction().equals(Action.LEFT_CLICK_AIR) ||ev.getAction().equals(Action.LEFT_CLICK_BLOCK))  )
				{
					Vector vec = pl.getLocation().getDirection();
					List<Entity> list = pl.getNearbyEntities(range, range, range);
					pl.getNearbyEntities(range, range, range).forEach(entity ->
					{


						if (entity instanceof LivingEntity)
						{
							{
								LivingEntity le = (LivingEntity) entity;
								if (inSight(pl, le))
								{
									le.setGlowing(true);
									le.setCustomNameVisible(true);
									double dmg = pl.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).getValue();
									le.damage(dmg, pl);
									//Messenger.send( pl,"Damage:"+ decimalF.format(dmg));
									///le.setHealth(0.0);
									//le.damage(1,pl);
									MunchyMax.getTaskHandler().runTask(() ->
									{
										le.setGlowing(false);
										le.setCustomNameVisible(false);


									}, 1L);
								}


							}
						}


					});
				}else{
					if( (ev.getAction().equals(Action.RIGHT_CLICK_AIR) ||ev.getAction().equals(Action.RIGHT_CLICK_BLOCK))  )
					{
						Snowball e = pl.launchProjectile(Snowball.class, pl.getLocation().getDirection().multiply(5));
						//e.setGlowing(true);
						//e.setCritical(true);
						e.setBounce(false);
						//	e.setGlowingTicks(2);
						e.setGravity(false);
					}
				}
		/*for(Entity e: list){
			if(e instanceof LivingEntity){
				{
					LivingEntity le = (LivingEntity)e;
					if(inSight(pl, le)){
						le.setGlowing(true);
						le.setHealth(0.0);
						le.damage(1,pl);
					}


				}
			}
		}*/

			}
		};
	}

	private static class BoundingBox{
	/*	public static Method
			met_handle = null,
			met_box = null;
		public static Field
			fd_a = null,
		  fd_b = null,
			fd_c = null,
			fd_d = null,
			fd_e = null,
			fd_f= null

			  ;*/


		public static boolean virgin = true;


		public final double xMin;
		public final double yMin;
		public final double zMin;
		public final double xMax;
		public final double yMax;
		public final double zMax;

		public BoundingBox (double _x1, double _y1, double _z1, double _x2,double _y2, double _z2){
			xMin = Math.min(_x1,_x2);
			yMin = Math.min(_y1,_y2);
			zMin = Math.min(_z1,_z2);
			xMax = Math.max(_x1,_x2);
			yMax = Math.max(_y1,_y2);
			zMax = Math.max(_z1,_z2);


		}

		public BoundingBox(Vector max, Vector min){
			this(max.getX(),max.getY(),max.getZ(),min.getX(),min.getY(),min.getZ());
		}
		public BoundingBox g(double dx, double dz, double dy) {

			return new BoundingBox(this.xMin - dx,
				                        this.yMin - dz,
				                        this.zMin - dy,
				                        this.xMax + dx,
				                        this.yMax + dz,
				                        this.zMax + dy);
		}

		public BoundingBox grow(double diff) {
			return this.g(diff, diff, diff);
		}

		public Vector getMaxPoint(){
			return new Vector(xMax,yMax,zMax);
		}

		public Vector getMinPoint(){
			return new Vector(xMin,yMin,zMin);
		}

		public static BoundingBox fromEntity(LivingEntity entity){
		//Example:
		//	        AxisAlignedBB box = ((CraftEntity)entity).getHandle().getBoundingBox()
		//	        Vector top = new Vector(box.a,box.b,box.c);
		//	        Vector bottom = new Vector(box.d,box.e,box.f);
			Validate.notNull(entity);

      Method
        met_handle = null,
        met_box = null;
       Field
        fd_a = null,
        fd_b = null,
        fd_c = null,
        fd_d = null,
        fd_e = null,
        fd_f= null;
      try{
				met_handle = met_handle == null  ? entity.getClass().getMethod("getHandle") : met_handle;
				met_handle.setAccessible(true);

				Object handle = met_handle.invoke(entity);
				met_box = met_box == null ? handle.getClass().getMethod("getBoundingBox") : met_box;
				met_box.setAccessible(true);
				Object box = met_box.invoke(handle);

				fd_a = fd_a == null ? box.getClass().getField("a") : fd_a;
				fd_a.setAccessible(true);

				fd_b = fd_b == null ? box.getClass().getField("b") : fd_b;
				fd_b.setAccessible(true);

				fd_c = fd_c == null ? box.getClass().getField("c") : fd_c;
				fd_c.setAccessible(true);

				fd_d = fd_d == null ? box.getClass().getField("d") : fd_d;
				fd_d.setAccessible(true);

				fd_e = fd_e == null ? box.getClass().getField("e") : fd_e;
				fd_e.setAccessible(true);

				fd_f = fd_f == null ? box.getClass().getField("f") : fd_f;
				fd_f.setAccessible(true);
				BoundingBox bb = new BoundingBox(fd_a.getDouble(box),
					                             fd_b.getDouble(box),
					                             fd_c.getDouble(box),
					                             fd_d.getDouble(box),
					                             fd_e.getDouble(box),
					                             fd_f.getDouble(box)
				);
				virgin = false;
				return bb;


			}catch(Exception ex){
			/*	if(virgin){
					met_box = null;
					met_handle = null;
					fd_a = null;
					fd_b = null;
					fd_c = null;
					fd_d = null;
					fd_e = null;
					fd_f= null;
				}*/
				throw new IllegalArgumentException("Entity does not have a bounding box",ex);
			}



		}


	}
}
