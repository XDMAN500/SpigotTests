package me.varmetek.core.util;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginBase;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

/**
 * Created by XDMAN500 on 1/9/2017.
 */
public class TaskHandler implements Cleanable
{

	protected final Plugin plugin;
	protected final Async async;
	protected final BukkitScheduler schedule;

	public  TaskHandler(PluginBase plugin){
		this.plugin = plugin;
		schedule = plugin.getServer().getScheduler();
			async = new Async();
	}

	public abstract class  Task extends BukkitRunnable{
			public Task runNow(){
				this.runTask(plugin);
				return this;
			}

			public Task runLater(long delay){
				this.runTaskLater(plugin, delay);
				return this;
			}

			public Task runTimer(long delay, long period){
				this.runTaskTimer(plugin,delay,period);
				return this;
			}

	}

	public abstract class  AsyncTask extends BukkitRunnable{
		public AsyncTask runNow(){
			this.runTaskAsynchronously(plugin);
			return this;
		}

		public AsyncTask runLater(long delay){
			this.runTaskLaterAsynchronously(plugin, delay);
			return this;
		}

		public AsyncTask runTimer(long delay, long period){
			this.runTaskTimerAsynchronously(plugin,delay,period);
			return this;
		}

	}

  @Override
  public void clean ()
  {
   // async = null;

  }




	public  BukkitTask run(Runnable run)
	{
		return schedule.runTaskLater(plugin,run,1L);
	}


	public  BukkitTask runTask( Runnable run, long time)
	{
		return schedule.runTaskLater(plugin,run,time);
	}


	public  BukkitTask runTimer( Runnable run, long time, long delay){
		return schedule.runTaskTimer(plugin,run,time,delay);
	}


	public Async asAsync(){
		return async;
	}

	public class Async
	{

		private Async(){}


		public BukkitTask run (Runnable run)
		{
			return schedule.runTaskLaterAsynchronously(plugin, run, 1L);
		}


		public BukkitTask runTask (Runnable run, long time)
		{
			return schedule.runTaskLaterAsynchronously(plugin, run, time);
		}


		public BukkitTask runTimer (Runnable run, long time, long delay)
		{
			return schedule.runTaskTimerAsynchronously(plugin, run, time, delay);
		}
	}





}
