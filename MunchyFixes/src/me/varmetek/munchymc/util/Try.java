package me.varmetek.munchymc.util;

import java.util.concurrent.Callable;

/**
 * Created by XDMAN500 on 1/14/2017.
 */
public class Try<T>
{
	private Callable<T> task;
	private  Try(Callable<T> task){
		this.task = task;

	}

	public static  Try of(Callable<?> task){
		return new Try<>(task);
	}
	public T getOrElse(T def){
		try
		{
			return task.call();
		}catch(Exception e){
			return def;
		}
	}
	public T get(){
		return getOrElse(null);

	}
}