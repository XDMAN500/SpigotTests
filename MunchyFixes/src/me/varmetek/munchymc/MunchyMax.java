package me.varmetek.munchymc;

import me.varmetek.core.item.ItemMap;
import me.varmetek.core.util.TaskHandler;
import me.varmetek.munchymc.backend.ChatPlaceholderMap;
import me.varmetek.munchymc.backend.DataManager;
import me.varmetek.munchymc.backend.UserHandler;
import me.varmetek.munchymc.backend.KitHandler;
import org.apache.commons.lang.Validate;
import org.bukkit.command.ConsoleCommandSender;

import java.util.Optional;

/**
 * Created by XDMAN500 on 2/18/2017.
 */
public final class MunchyMax
{
	private static Optional<Main>  main = Optional.empty();

	void setMain(Main main){
		Validate.isTrue(!this.main.isPresent(), "Main has already been set");
		this.main  = Optional.of(main);
	}


	public Main getPlugin(){
		return main.orElseThrow( ()->{return new IllegalStateException("Singleton has not been set");});
	}

	public ChatPlaceholderMap getChatPlaceholderMap(){
		Validate.isTrue(this.main.isPresent(),"Singleton has not been set");
		return main.get().getChatPlaceholderMap();
	}

	public ConsoleCommandSender getConsole ()
	{
		Validate.isTrue(this.main.isPresent(),"Singleton has not been set");
		return main.get().getConsole();
	}

	public UserHandler getUserHandler ()
	{
		Validate.isTrue(this.main.isPresent(),"Singleton has not been set");
		return main.get().getUserHandler();
	}


	public TaskHandler getTaskHandler ()
	{
		Validate.isTrue(this.main.isPresent(),"Singleton has not been set");
		return main.get().getTaskHandler();
	}

	public ItemMap getItemMap ()
	{
		Validate.isTrue(this.main.isPresent(),"Singleton has not been set");
		return main.get().getItemMap();
	}

	public DataManager getDataManager ()
	{
		Validate.isTrue(this.main.isPresent(),"Singleton has not been set");
		return main.get().getDataManager();
	}

	public KitHandler getKitHandler ()
	{
		Validate.isTrue(this.main.isPresent(),"Singleton has not been set");
		return main.get().getKitHandler();
	}



}
