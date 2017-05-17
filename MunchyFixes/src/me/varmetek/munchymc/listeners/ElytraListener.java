package me.varmetek.munchymc.listeners;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.entity.Player;
import org.bukkit.entity.SpectralArrow;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

import java.text.DecimalFormat;

/**
 * Created by XDMAN500 on 12/30/2016.
 */
public class ElytraListener implements Listener
{
	private static final double speedLimit = 1.2;
	private static final DecimalFormat decimalF = new DecimalFormat("%#.###");
	private static final Vector down = new Vector().setY(2);




	@EventHandler
	public void boost(PlayerMoveEvent ev)
	{
		Player pl = ev.getPlayer();
		if(!pl.isGliding()){
			return;
		}

		Vector looking = pl.getLocation().getDirection();
		Vector motion = pl.getVelocity();
		//double lookingLength = looking.length();
		double motionSpeed = motion.length();

		boolean isMaxSpeed = motionSpeed  > speedLimit;
		double angle =  looking.angle(motion);
		boolean sameDir =  angle <  (3.14159 / 6.0);
		if(!isMaxSpeed )
		{
			motion.add(looking.clone().multiply(1.0/30.0));
			pl.setVelocity(motion);

		}
		if(pl.isSneaking()){
			pl.setGliding(false);
		}
		if(pl.isHandRaised())
		{

			SpectralArrow e = pl.launchProjectile(SpectralArrow.class, pl.getVelocity().setY(0));
			e.setGlowing(true);
			e.setCritical(true);
			e.setGlowingTicks(2);
			e.setGravity(true);

		}
		pl.spigot().sendMessage(ChatMessageType.ACTION_BAR, new ComponentBuilder("Speed: " + decimalF.format(motionSpeed/ speedLimit)).create());
		//	Messenger.send(pl, " ");
		//Messenger.send(pl, ev.getEventName() + "Linear: " + sameDir);
		//	Messenger.send(pl, ev.getEventName() + "Speed: " + decimalF.format(motionSpeed));


	}
}
