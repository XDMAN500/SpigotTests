package me.varmetek.munchymc.util;

import me.varmetek.munchymc.Main;
import org.apache.commons.lang.Validate;

import java.io.File;
import java.io.IOException;

/**
 * Created by XDMAN500 on 1/14/2017.
 */
public final class UtilFile
{

	private UtilFile()throws Exception{throw new UnsupportedOperationException("Cannot initiate");}

	private final static String __ = File.separator;
	public static File getFile(String name){
		return new  File(Main.get().getDataFolder(),name);
	}

	public static File getFile(String... path){


			StringBuilder e = new StringBuilder();
			for(int i = 0; i< path.length; i++){
				e.append(__+path[i]);
			}
		return new  File(Main.get().getDataFolder(),e.toString());
	}
	public static boolean isFile(String name){
		return name.contains(".");
	}
	public static File create(File f) throws IOException
	{
		Validate.notNull(f);
		if(!f.exists() &&  isFile(f.getName())){

			f.getParentFile().mkdirs();
			f.createNewFile();


		}
		return f;
	}

	public static File getAndCreate(String name) throws IllegalArgumentException, IOException{
		//if(!isFile(name)) throw new IllegalArgumentException("Paramerter must be a file.");
			File file =  getFile(name);
			create(file);
			return file;
	}

	public static File getAndCreate(String... name) throws IllegalArgumentException, IOException{
		//if(!isFile(name)) throw new IllegalArgumentException("Paramerter must be a file.");
		File file =  getFile(name);
		create(file);
		return file;
	}


}
