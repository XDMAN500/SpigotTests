package me.varmetek.munchymc.util;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Optional;

/**
 * Created by XDMAN500 on 12/30/2016.
 */
public final class UtilReflection
{
		private UtilReflection(){}
		private static String version;
		public static final String NMS, OBC;


		static{
			version =Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
			NMS = "net.minecraft.server."+version+".";
			OBC = "org.bukkit.craftbukkit."+version+".";
		}


		public static String getVersion()
		{

			return version;
		}

		public Class<?> getNMSClass(String name) {
			try {
				return Class.forName(NMS + name);
			} catch(ClassNotFoundException e)
			{
				e.printStackTrace();
				return null;
			}
		}
		public Class<?> getOBCClass(String name)
		{
			try
			{
				return Class.forName(OBC + name);
			} catch (ClassNotFoundException e)
			{
				e.printStackTrace();
				return null;
			}
		}
		public Optional<Class<?>> getClass(String name)
		{
			try
			{
				return Optional.of(Class.forName(name));
			} catch (ClassNotFoundException e)
			{
				e.printStackTrace();
				return Optional.empty();
			}

		}

		public Optional<Field> getField(Class<?> obj, String name)
		{
			Validate.notNull(obj, "Class cannot be null");
			Validate.notNull(name, "Field cannot be null");
			Validate.notEmpty(name, "Field cannot be empty");

				try
			{
				return Optional.of(obj.getDeclaredField(name));
			} catch (NoSuchFieldException e)
			{
				e.printStackTrace();
				return Optional.empty();
			}
		}

		public Optional<Method> getMethod(Class<?> obj, String name, Class<?>... parameters)
		{
			Validate.notNull(obj, "Class cannot be null");
			Validate.notNull(name, "Method cannot be null");
			Validate.notEmpty(name, "Method cannot be empty");
			Validate.notNull(parameters, "Parameters cannot be empty");
			try
			{
				return Optional.of(obj.getDeclaredMethod(name,parameters));
			} catch (NoSuchMethodException e)
			{
				e.printStackTrace();
				return Optional.empty();
			}
		}

	public Optional<Constructor> getConstructor(Class<?> obj, Class<?>... parameters)
	{
		Validate.notNull(obj, "Class cannot be null");
		Validate.notNull(parameters, "Parameters cannot be empty");
		try
		{
			return Optional.of(obj.getDeclaredConstructor(parameters));
		} catch (NoSuchMethodException e)
		{
			e.printStackTrace();
			return Optional.empty();
		}
	}
}
