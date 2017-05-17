package me.varmetek.munchymc.util;

import me.varmetek.munchymc.MunchyMax;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;

import java.io.File;
import java.io.IOException;

/**
 * Created by XDMAN500 on 1/14/2017.
 */
public final class UtilFile
{

	private UtilFile()throws Exception{throw new UnsupportedOperationException("Cannot initiate");}

	public final static String __ = File.separator;
	public static File getFile(String name){
		return new  File(MunchyMax.getInstance().getDataFolder(),name);
	}

	public static File getFile(String... path){


			StringBuilder e = new StringBuilder();
			for(int i = 0; i< path.length; i++){
				e.append(__+path[i]);
			}
		return new  File(MunchyMax.getInstance().getDataFolder(),e.toString());
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



	public void throwIO(boolean c, String reason) throws IOException{
		if (!c) throw new IOException(reason);
	}



	public enum CoreFile
	{
		WARPS("", ".yml"), LOC("", ".yml"), CONFIG("", ".yml"), USER("", __), USERREG(USER.getDirectory(), ".yml"),
		KITS("", __), Points("", ".yml"),MINES("",".yml");

		private String path;
		private File file = null;
		private String suffix;

		CoreFile (String argPath, String suffix){
			this.suffix = suffix;
			this.path = argPath;
			file = getFile();
			//file =new File(Main.get().getDataFolder(), name() + suffix);

			/*TaskHandler.Async.run(()->{

					try
					{
						file.createNewFile();
					} catch (IOException e)
					{
						e.printStackTrace();
					}


				} );*/
		}

		public String getPath (){
			return path;
		}

		public String getName (){
			return name() + suffix;
		}

		public String getDirectory (){

			return path + __ + getName();
		}

		public boolean isFile (){
			return UtilFile.isFile(getName());
		}

		public File getFile (){
			return file == null ?
				       (isFile()
					        ? new File(MunchyMax.getInstance().getDataFolder(), getDirectory()) : null) : file;
		}


		public static void createFiles (){

			for (CoreFile cf : values()) {


				if (!cf.isFile()){

					continue;
				}

				if (!cf.getFile().exists())
					try {
						UtilFile.create(cf.getFile());


					} catch (Exception e) {
						try {
							Bukkit.getLogger().warning("Could not create file " + cf.name() + " | " + e.getMessage());
							Bukkit.getLogger().warning(cf.name() + ": " + cf.getFile().getAbsolutePath() + " | " + e.getMessage());
							Bukkit.getLogger().warning(cf.name() + ":" + cf.path);
							e.printStackTrace();
						} catch (Exception ex) {
							Bukkit.getLogger().warning("WTF is this? - " + ex.getMessage());
						}
					}

			}
		}
	}

}
