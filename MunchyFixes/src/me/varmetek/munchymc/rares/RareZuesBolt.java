package me.varmetek.munchymc.rares;

import me.varmetek.core.util.Messenger;
import me.varmetek.munchymc.MunchyMax;
import me.varmetek.munchymc.backend.RareItemListener;
import me.varmetek.munchymc.backend.test.CustomItemRare;
import net.minecraft.server.v1_11_R1.AxisAlignedBB;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftEntity;
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
			AxisAlignedBB box = ((CraftEntity)entity).getHandle().getBoundingBox().g(bulletSize);
			Vector top = new Vector(box.a,box.b,box.c);
			Vector bottom = new Vector(box.d,box.e,box.f);
			//Messenger.send(player,box.toString());

			//Messenger.send(player," ");
			//Messenger.send(player,entity.getName() +"   "+ vectorString(finish));
			//Messenger.send(player, "-> "+ vectorString(top));
			//Messenger.send(player, "-> "+ vectorString(bottom));
			return finish.isInAABB(top,bottom);
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
}
