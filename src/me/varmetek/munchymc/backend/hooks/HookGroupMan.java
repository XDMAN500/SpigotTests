package me.varmetek.munchymc.backend.hooks;

import me.varmetek.core.hook.BaseHook;
import org.anjocaido.groupmanager.GroupManager;
import org.anjocaido.groupmanager.data.User;
import org.anjocaido.groupmanager.dataholder.OverloadedWorldHolder;

/**
 * Created by XDMAN500 on 4/22/2017.
 */
public class HookGroupMan extends BaseHook<GroupManager>
{
	public HookGroupMan (GroupManager plugin, boolean needed)
	{
		super(plugin, needed);
	}

	public User getUser(Player name){

			OverloadedWorldHolder handler = plugin.getWorldsHolder().getWorldData(Player name)

	}
}
